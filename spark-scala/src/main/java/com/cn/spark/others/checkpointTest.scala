package com.cn.spark.others

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object checkpointTest {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf()
    conf.setMaster("local").setAppName("checkpoint")
    val sc = new SparkContext(conf)

    sc.setCheckpointDir("./checkpoint")
    val lines = sc.textFile("./words.txt")
    lines.checkpoint()
    lines.foreach { println}
    sc.stop()
  }
}