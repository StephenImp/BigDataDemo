package com.cn.sparkstreaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.Durations
import org.apache.spark.streaming.StreamingContext

object Operator_UpdateStateByKey {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setMaster("local[2]").setAppName("updateStateByKey")
    val jsc = new StreamingContext(conf,Durations.seconds(5))
    //设置日志级别
    jsc.sparkContext.setLogLevel("WARN")
    //设置checkpoint路径
    jsc.checkpoint("hdfs://node1:9000/spark/checkpoint")
    
    val lineDStream = jsc.socketTextStream("node5", 9999)
    val wordDStream = lineDStream.flatMap { _.split(" ") }
    val pairDStream = wordDStream.map { (_,1)}
    
    val result = pairDStream.updateStateByKey((seq:Seq[Int],option:Option[Int])=>{
      var value = 0
      value += option.getOrElse(0)
      for(elem <- seq){
        value +=elem
      }
      
     Option(value)
    })
    
    result.print()
    jsc.start()
    jsc.awaitTermination()
    jsc.stop()
  }
}