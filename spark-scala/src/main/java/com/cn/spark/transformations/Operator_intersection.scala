package com.cn.spark.transformations

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
/**
 * intersection
 * 取两个RDD的交集
 */
object Operator_intersection {
  def main(args: Array[String]): Unit = {
    
    val conf = new SparkConf()
    conf.setMaster("local").setAppName("intersection")
    val sc = new SparkContext(conf)
    val rdd1 = sc.makeRDD(Array(1,2,3,4))
    val rdd2 = sc.makeRDD(Array(1,2,7,8))
    //注意使用intersection时，RDD的类型要一致
    val result = rdd1.intersection(rdd2)
    result.foreach { println}
    sc.stop()
  }
}