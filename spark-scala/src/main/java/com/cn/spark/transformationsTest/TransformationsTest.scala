package com.cn.spark.transformationsTest

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object TransformationsTest {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf()
    conf.setMaster("local").setAppName("test")
    val sc = new SparkContext(conf)


    val lines : RDD[String]= sc.textFile("./words")//这里还阔以指定分区数
    
    val result: Array[String] = lines.take(1)
    result.foreach(println)


/**
  * first   取 RDD 的第一行
  * take(1)  等价于   first
  */
//    val result: String = lines.first()
//    println(result)


    /**
      * 回收  action
    */
//    val result: Array[String] = lines.collect()
//    
//    result.foreach(println)


    /**
      * count   action
      */
//    val result = lines.count
//    println(result)


    /**
      * filter
      */
//    lines.filter(s=>{
//      !s.equals("hello scala")
//    }).foreach(println)
//    
//    
    /**
     * sample 随机抽样
     * true 代表有无放回抽样    抽出去的数据是否还放回来
     * 0.1 抽样的比例
     *
     * seed:Long类型的种子，针对的同一批数据，
      * 只要种子相同，每次抽样的数据结果一样。
      *
     */
//    val result = lines.sample(true, 0.1,100)
//    
//    result.foreach(println)
    
    sc.stop()
  }
}