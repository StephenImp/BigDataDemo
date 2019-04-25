package com.cn.spark.actions

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object Operator_foreachPartition {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setMaster("local").setAppName("foreachpartition")
    val sc = new SparkContext(conf)
    val rdd1 = sc.makeRDD(Array(1,2,3,4,5))
    rdd1.foreachPartition { iter => {
      println("连接数据库。。。")
      while(iter.hasNext){
        println(iter.next())
      }
    } }
    sc.stop()
  }
}