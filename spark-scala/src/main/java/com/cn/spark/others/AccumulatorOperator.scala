package com.cn.spark.others

import org.apache.spark.Accumulable
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object AccumulatorOperator {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setMaster("local").setAppName("accumulator")
    val sc = new SparkContext(conf)
    val accumulator = sc.accumulator(0)
    sc.textFile("./words.txt").foreach { x =>{accumulator.add(1)}}
    println(accumulator.value)
    sc.stop()
  }
}