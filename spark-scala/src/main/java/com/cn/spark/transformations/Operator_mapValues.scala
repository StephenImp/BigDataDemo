package com.cn.spark.transformations

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object Operator_mapValues {
  def main(args: Array[String]): Unit = {
     val conf = new SparkConf()
     conf.setMaster("local").setAppName("mapValues")
     val sc = new SparkContext(conf)
     val rdd = sc.makeRDD(Array(("a",100),("b",200),("c",300)))
     val result = rdd.mapValues(i =>{i+"~"})
     result.foreach(println)
     sc.stop()
  }
}