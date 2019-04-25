package com.cn.spark.transformations

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object Operator_mapPartition {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setMaster("local").setAppName("mapPartition")
    val sc = new SparkContext(conf)
    val rdd1 = sc.makeRDD(Array(1,2,3,4,5,6),4)
    
    val mapResult  = rdd1.mapPartitions(iter=>{
      println("插入数据库")
      iter
    },false)
    mapResult.foreach { println }
    
    sc.stop()
  }
}