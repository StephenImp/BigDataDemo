package com.cn.spark.transformations

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object Operator_distinct {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setMaster("local").setAppName("distinct")
    val sc = new SparkContext(conf)
    val rdd1 = sc.makeRDD(Array(1,2,3,4,4))
//    rdd1.map {(_,1) }.reduceByKey(_+_).map(_._1).foreach { println }
    rdd1.distinct().foreach { println }
    
    sc.stop()
    
  }
}