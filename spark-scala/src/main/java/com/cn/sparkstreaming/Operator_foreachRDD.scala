package com.cn.sparkstreaming

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.Durations

object Operator_foreachRDD {
   def main(args: Array[String]): Unit = {
     val conf = new SparkConf()
     conf.setMaster("local[2]").setAppName("foreachRDD")
     val sc = new SparkContext(conf)
     val jsc = new StreamingContext(sc,Durations.seconds(5))
     
     val socketDStream = jsc.socketTextStream("node5", 9999)
     socketDStream.foreachRDD { rdd => {
       rdd.foreach { elem => {
         println(elem)
       } }
     }}
     
     jsc.start()
     jsc.awaitTermination()
     jsc.stop(false)
   }
}