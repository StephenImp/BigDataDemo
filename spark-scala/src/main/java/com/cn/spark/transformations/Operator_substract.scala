package com.cn.spark.transformations

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object Operator_substract {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setMaster("local").setAppName("substract")
    
    val sc = new SparkContext(conf)
    val rdd1 = sc.makeRDD(List(1,2,3,4))
    val rdd2 = sc.makeRDD(List(1,2,5,6))
//    val result = rdd1.subtract(rdd2)
    val result = rdd2.subtract(rdd1)
    result.foreach { println}
    sc.stop()
    
  }
}