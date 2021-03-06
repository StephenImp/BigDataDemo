package com.cn.spark.transformations

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object Operator_flatMap {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setMaster("local").setAppName("flatMap")
    val sc = new SparkContext(conf)
    val lines = sc.textFile("./words.txt")
    val result = lines.flatMap { _.split(" ")}
    result.foreach { println}
    sc.stop()
  }
}