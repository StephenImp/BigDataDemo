package com.cn.spark.transformations

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions

object Operator_join {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setMaster("local").setAppName("join")
    val sc = new SparkContext(conf)
    val rdd1 = sc.makeRDD(List(Tuple2(0,"000"),Tuple2(1,"a"),Tuple2(2,"b"),Tuple2(3,"c")))   
    val rdd2 = sc.makeRDD(List(Tuple2(1,"aa"),Tuple2(2,"bb"),Tuple2(3,"cc"),Tuple2(4,"444")))   
    rdd1.join(rdd2).foreach(println)
    rdd1.leftOuterJoin(rdd2).foreach(println)
//    rdd1.rightOuterJoin(rdd2).foreach(println)
//    rdd1.fullOuterJoin(rdd2).foreach(println)
    
    sc.stop()
  }
}