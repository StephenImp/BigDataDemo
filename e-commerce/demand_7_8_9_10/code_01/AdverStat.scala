import java.util.Date

import commons.conf.ConfigurationManager
import commons.constant.Constants
import commons.utils.DateUtils
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Duration, Minutes, Seconds, StreamingContext}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer


object AdverStat {

  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setAppName("adver").setMaster("local[*]")
    val sparkSession = SparkSession.builder().config(sparkConf).enableHiveSupport().getOrCreate()

    // val streamingContext = StreamingContext.getActiveOrCreate(checkpointDir, func)
    val streamingContext = new StreamingContext(sparkSession.sparkContext, Seconds(5))

    val kafka_brokers = ConfigurationManager.config.getString(Constants.KAFKA_BROKERS)
    val kafka_topics = ConfigurationManager.config.getString(Constants.KAFKA_TOPICS)

    val kafkaParam = Map(
      "bootstrap.servers" -> kafka_brokers,
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "group1",
      // auto.offset.reset
      // latest: 先去Zookeeper获取offset，如果有，直接使用，如果没有，从最新的数据开始消费；
      // earlist: 先去Zookeeper获取offset，如果有，直接使用，如果没有，从最开始的数据开始消费
      // none: 先去Zookeeper获取offset，如果有，直接使用，如果没有，直接报错
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false:java.lang.Boolean)
    )

    // adRealTimeDStream: DStream[RDD RDD RDD ...]  RDD[message]  message: key value
    val adRealTimeDStream = KafkaUtils.createDirectStream[String, String](streamingContext,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](Array(kafka_topics), kafkaParam)
    )

    // 取出了DSream里面每一条数据的value值
    // adReadTimeValueDStream: Dstram[RDD  RDD  RDD ...]   RDD[String]
    // String:  timestamp province city userid adid
    val adReadTimeValueDStream = adRealTimeDStream.map(item => item.value())

    val adRealTimeFilterDStream = adReadTimeValueDStream.transform{
      logRDD =>

        // blackListArray: Array[AdBlacklist]     AdBlacklist: userId
        val blackListArray = AdBlacklistDAO.findAll()

        // userIdArray: Array[Long]  [userId1, userId2, ...]
        val userIdArray = blackListArray.map(item => item.userid)

        logRDD.filter{
          // log : timestamp province city userid adid
          case log =>
            val logSplit = log.split(" ")
            val userId = logSplit(3).toLong
            !userIdArray.contains(userId)
        }
    }

    // 需求一：实时维护黑名单
    generateBlackList(adRealTimeFilterDStream)

    streamingContext.start()
    streamingContext.awaitTermination()
  }

  def generateBlackList(adRealTimeFilterDStream: DStream[String]) = {
    // adRealTimeFilterDStream: DStream[RDD[String]]    String -> log : timestamp province city userid adid
    // key2NumDStream: [RDD[(key, 1L)]]
    val key2NumDStream = adRealTimeFilterDStream.map{
      //  log : timestamp province city userid adid
      case log =>
        val logSplit = log.split(" ")
        val timeStamp = logSplit(0).toLong
        // yy-mm-dd
        val dateKey = DateUtils.formatDateKey(new Date(timeStamp))
        val userId = logSplit(3).toLong
        val adid = logSplit(4).toLong

        val key = dateKey + "_" + userId + "_" + adid

        (key, 1L)
    }

    val key2CountDStream = key2NumDStream.reduceByKey(_+_)

    // 根据每一个RDD里面的数据，更新用户点击次数表
    key2CountDStream.foreachRDD{
      rdd => rdd.foreachPartition{
        items =>
          val clickCountArray = new ArrayBuffer[AdUserClickCount]()

          for((key, count) <- items){
            val keySplit = key.split("_")
            val date = keySplit(0)
            val userId = keySplit(1).toLong
            val adid = keySplit(2).toLong

            clickCountArray += AdUserClickCount(date, userId, adid, count)
          }

          AdUserClickCountDAO.updateBatch(clickCountArray.toArray)
      }
    }

    // key2BlackListDStream: DStream[RDD[(key, count)]]
    val key2BlackListDStream = key2CountDStream.filter{
      case (key, count) =>
        val keySplit = key.split("_")
        val date = keySplit(0)
        val userId = keySplit(1).toLong
        val adid = keySplit(2).toLong

        val clickCount = AdUserClickCountDAO.findClickCountByMultiKey(date, userId, adid)

        if(clickCount > 100){
          true
        }else{
          false
        }
    }

    // key2BlackListDStream.map: DStream[RDD[userId]]
    val userIdDStream = key2BlackListDStream.map{
      case (key, count) => key.split("_")(1).toLong
    }.transform(rdd => rdd.distinct())

    userIdDStream.foreachRDD{
      rdd => rdd.foreachPartition{
        items =>
          val userIdArray = new ArrayBuffer[AdBlacklist]()

          for(userId <- items){
            userIdArray += AdBlacklist(userId)
          }

          AdBlacklistDAO.insertBatch(userIdArray.toArray)
      }
    }
  }



}
