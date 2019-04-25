package com.cn.sparkstreaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.Durations
import org.apache.spark.streaming.StreamingContext

object Operator_Window {
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
    val mapDStream = wordDStream.map { (_,1)}
    
    
    //window没有优化后的
    val result = mapDStream.reduceByKeyAndWindow((v1:Int,v2:Int)=>{
        v1+v2
      }, Durations.seconds(60), Durations.seconds(10))
      
   //优化后的
//   val result = mapDStream.reduceByKeyAndWindow((v1:Int,v2:Int)=>{
//       v1+v2
//     }, (v1:Int,v2:Int)=>{
//       v1-v2
//     }, Durations.seconds(60), Durations.seconds(10))

    result.print()
    jsc.start()
    jsc.awaitTermination()
    jsc.stop()
  }
}