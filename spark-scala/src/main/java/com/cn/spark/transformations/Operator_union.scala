package com.cn.spark.transformations

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object Operator_union {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf()
    conf.setMaster("local").setAppName("union")
//    conf.setMaster("local").setMaster("union")
    val sc = new SparkContext(conf)
    val rdd1 = sc.makeRDD(List(1,2,3,4))
    val rdd2 = sc.makeRDD(Array(5,6,7,8))
    rdd1.union(rdd2).foreach { println }
    sc.stop()
  }
}