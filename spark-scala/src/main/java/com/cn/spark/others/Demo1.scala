package com.cn.spark.others

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object Demo1 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setMaster("local").setAppName("demo")
    val sc = new SparkContext(conf)
    val lines = sc.textFile("./records.txt")
    val filter = lines.sample(true, 0.5).flatMap { _.split(" ")}.map((_,1)).reduceByKey(_+_).sortBy(_._2,false).first()
    println("**********="+filter)
    val result = lines.flatMap { _.split(" ")}.map { (_,1) }.filter(!_._1.equals(filter._1)).reduceByKey(_+_)
    result.foreach(println)
    
    sc.stop()
  }
}