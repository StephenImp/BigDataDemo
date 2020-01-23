package modeling

import java.io.{File, PrintWriter}
import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

import org.apache.spark.mllib.classification.LogisticRegressionWithLBFGS
import org.apache.spark.mllib.evaluation.MulticlassMetrics
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.{SparkConf, SparkContext}
import utils.RedisUtil

import scala.collection.mutable.ArrayBuffer

/**
  * 数据建模
  */
object Train {
  def main(args: Array[String]): Unit = {
    //写入文件的输出流(中间建模的评估结果)
    val writer = new PrintWriter(new File("model_train.txt"))
    //初始化spark
    val sparkConf = new SparkConf()
      .setMaster("local[2]")
      .setAppName("TrafficModel")
    val sc = new SparkContext(sparkConf)

    //定义redis的数据库相关
    val dbIndex = 1
    //获取redis连接
    val jedis = RedisUtil.pool.getResource
    jedis.select(dbIndex)

    //设立目标监测点：你要对哪几个监测点进行建模
    val monitorIDs = List("0005", "0015")
    //取出相关监测点   数据挖掘工程师会告诉我们  0005 和 0015  这两个监测点和哪些监测点是有关系的
    val monitorRelations = Map[String, Array[String]](
      "0005" -> Array("0003", "0004", "0005", "0006", "0007"),
      "0015" -> Array("0013", "0014", "0015", "0016", "0017"))

    /**
      * eg：0005 中的 0005
      */
    //遍历上边所有的监测点，将这些检测点的相关数据进行读取
    monitorIDs.map(monitorID => {
      //得到当前“目标监测点”的相关监测点
      val monitorRelationArray = monitorRelations(monitorID)//得到的是Array（相关监测点） eg："0003", "0004", "0005", "0006", "0007"

      //初始化时间
      val currentDate = Calendar.getInstance().getTime
      //当前小时分钟数
      val hourMinuteSDF = new SimpleDateFormat("HHmm")
      //当前年月日
      val dateSDF = new SimpleDateFormat("yyyyMMdd")
      //以当前时间，格式化好年月日的时间
      val dateOfString = dateSDF.format(currentDate)

      /**
        * eg： 0005 中的 0005  一天中的所有数据
        */
      //根据“相关监测点”，取得当日的所有的监测点的平均车速
      //最终结果样式：(0005, {1033=93_2, 1034=1356_30})  eg: 0005这个监测点，10点33 平均车速93,2辆车
      val relationsInfo = monitorRelationArray.map(monitorID => { //封装map的过程
        (monitorID, jedis.hgetAll(dateOfString + "_" + monitorID))//
      })

      //确定使用多少小时内的数据进行建模
      val hours = 1
      //创建3个数组，一个数组用于存放特征向量，一数组用于存放Label向量，一个数组用于存放前两者之间的关联
      val dataX = ArrayBuffer[Double]()  //放3个特征向量
      val dataY = ArrayBuffer[Double]()  //放滴4个节点的目标向量

      //用于存放特征向量和特征结果的映射关系
      val dataTrain = ArrayBuffer[LabeledPoint]()

      //将时间拉回到1个小时之前，倒序，拉回单位：分钟
      //println(Range(60 * hours, 2, -1))  从 60 到 3            60-范围  2-截止到2，不包含  -1-步长是-1
      //Range(60,59,58.....5,4,3)
      //某一分钟的特征向量和某一分钟特征向量对应的答案 这里的3-60 是取的特征结果(结果向量)
      //取得 0005 中的 0005  一天中的某一个小时的数据
      for(i <- Range(60 * hours, 2, -1)){  //  [dataY-label(特征结果)] [dataX-特征向量]
        /**
          * 0,1,2  得到 3(label)  算完以后，清空，开始 1,2,3 得到 4
          */
        dataX.clear()
        dataY.clear()
        //以下内容包含：线性滤波(把数组求个平均值)

        /**
          *  0005 中的 0005拿到某三个特征向量的值
          */
        for(index <- 0 to 2){ // 拿到3个特征值
          //当前毫秒数 减去 1个小时之前的毫秒数 加上 1个小时之前的后 0分钟，1分钟，2分钟的毫秒数（第3分钟作为Label向量）
          val oneMoment = currentDate.getTime - 60 * i * 1000 + 60 * index * 1000   // index = 0 代表 一小时前的第 0 分钟
          //拼装出当前(当前for循环这一次的时间)的小时分钟数
          val oneHM = hourMinuteSDF.format(new Date(oneMoment))//1024 field(redis) --这个是程序执行到这里的时间记录一下
          //取得该时刻下里面数据
          //取出的数据形式距离：(0005, {1024=93_2, 1025=1356_30,1026=1336_20})
          for((k, v) <- relationsInfo){
            //如果index==2，意味着前3分钟的数据已经组装到了dataX中，那么下一时刻的数据，如果是目标卡口，则需要存放于dataY中
            if(k == monitorID && index == 2){
              val nextMoment = oneMoment + 60 * 1000  //label 时间点
              val nextHM = hourMinuteSDF.format(new Date(nextMoment))//1027

              /**
                * 这里判断第四分钟是否存在key是为什么？
                * 判断是否有数据  没有值的时候是一个稀疏矩阵，但是我们在使用牛顿迭代法的时候需要的是一个密集矩阵
                */
              //将第4分钟的label放在dataY中
              if(v.containsKey(nextHM)){
                val speedAndCarCount = v.get(nextHM).split("_")
                val valueY = speedAndCarCount(0).toFloat / speedAndCarCount(1).toFloat//得到第4分钟的平均车速
                dataY += valueY
              }
            }

            //  "0005" -> Array("0003", "0004", "0005", "0006", "0007"),
            //   0005 这个卡口的相关监测点是 5 个
            //组装前3分钟的数据放在dataX
            if(v.containsKey(oneHM)){
              val speedAndCarCount = v.get(oneHM).split("_")
              val valueX = speedAndCarCount(0).toFloat / speedAndCarCount(1).toFloat//得到当前这一分钟的特征值
              dataX += valueX //这他妈到底是放到集合里面去了，还是说就给了一个值？这个要验证一下  猜测：放到集合里面去了
            }else{
              dataX += 60.0F
            }
          }
        }

        /**
          * "0005" -> Array("0003", "0004", "0005", "0006", "0007")
          *
          * 0005  这5个相关节点的前三分钟(15个点)  共同导致了  0005的第四分钟
          *
          * 15 个数据点(dataX中的15个元素?) 得到 1个dataY
          *
          * 0,1,2  这三个是个的平均车速  放到 dataX中
          * 3      的平均车速放到  dataY中
          *
          * LFBGS 算法(牛顿迭代法)
          *
          */
        //准备训练模型  数据准备ok，准备建模
        //先将dataX和dataY映射于一个LabeledPoint对象中
        if(dataY.toArray.length == 1){  //这15个
          val label = dataY.toArray.head//答案的平均车速  取元素
          //label的取值范围是：0~15， 30~60  ----->  0, 1, 2, 3, 4, 5, 6
          //真实情况：0~120KM/H车速，划分7个级别，公式就如下：
          /**
            * dense  密集的
            * 将 dataY 变成级别
            */
          val record = LabeledPoint(
            if (label / 10 < 6){
            (label / 10).toInt
            }  else {
            6
            }, Vectors.dense(dataX.toArray))
          dataTrain += record
        }
      }

      //将数据集写入到文件中方便查看
      dataTrain.foreach(record => {
        println(record)
        writer.write(record.toString() + "\n")
      })

      /**
        * 将整个数据集分为两部分，一部分用来训练，一部分用来测试你训练好的模型
        * 训练集和测试集都是带答案的，都是历史数据
        *
        */
      //开始组装训练集(0.6)和测试集(0.4)   11L - 随机种子数:从哪一个数字开始，作为起点进行随机
      // eg: Random r1 = new Random(11);
      //     r1.nextInt(100)    每次结果都一样  是随机的，但是每一次的随机，但是参照系不同，导致结果相同
      //  反正就是他妈的这里  11 可以乱指
      val rddData = sc.parallelize(dataTrain)
      val randomSplits = rddData.randomSplit(Array(0.6, 0.4), 11L)
      //训练集
      val trainData = randomSplits(0)
      //测试集
      val testData = randomSplits(1)

      //使用训练集进行建模  上面将测速划分成7个等级，那么这个必须是 >=7
      val model = new LogisticRegressionWithLBFGS().setNumClasses(7).run(trainData)
      //完成建模之后，使用测试集，评估模型精确度
      /**
        * features  dataX
        */
      val predictionAndLabels = testData.map{
        case LabeledPoint(label, features) =>
          val prediction = model.predict(features)
          (prediction, label)//返回  预测值和目标值关联一下，肉眼评估一下
      }

      //得到当前评估值
      val metrics = new MulticlassMetrics(predictionAndLabels)
      val accuracy = metrics.accuracy//取值范围：0.0~1.0  越接近1.0越吻合 也不是说1.0就是好的

      println("评估值：" + accuracy)
      writer.write(accuracy + "\n")

      //设置评估阈值:超过多少精确度，则保存模型
      if(accuracy > 0.0){
        //将模型保存到HDFS
        val hdfsPath = "hdfs://linux01:8020/traffic/model/" +
          monitorID +
          "_" +
          new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(System.currentTimeMillis()))
        model.save(sc, hdfsPath)
        jedis.hset("model", monitorID, hdfsPath)
      }
    })

    RedisUtil.pool.returnResource(jedis)
    writer.flush
    writer.close
  }
}
