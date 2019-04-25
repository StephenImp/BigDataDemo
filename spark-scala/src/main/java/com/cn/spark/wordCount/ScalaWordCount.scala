package com.cn.spark.wordCount

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object ScalaWordCount {
  def main(args: Array[String]): Unit = {

    /**
      * 简写
      */
    //    val conf = new SparkConf().setMaster("local").setAppName("ScalaWordCount")
    //    new SparkContext(conf).textFile("./words").flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).foreach(println)
    
    
    val conf = new SparkConf()
    conf.setMaster("local").setAppName("ScalaWordCount")

    /**
      * SparkContext 是通往集群的唯一通道
      */
    val sc = new SparkContext(conf)
    val lines: RDD[String] = sc.textFile("./words")
    
    val words: RDD[String] = lines.flatMap(line=>{
      line.split(" ")
    })
    
    val pairWords: RDD[(String, Int)] = words.map(word=>{
      new Tuple2(word,1)
    })
    /**
     * reduceByKey 先分组，后对每一组内的key对应的value去聚合
     */
    val reduce: RDD[(String, Int)] = pairWords.reduceByKey((v1:Int,v2:Int)=>{v1+v2})

    /**
      * 对value 进行排序
      */
    //    val result = reduce.sortBy(tuple=>{tuple._2},false)

    /**
      *   对value进行排序
      *   false 降序
      *
      *   tuple.swap  将tuple 反转
      */
    val rdd1 : RDD[(Int, String)]= reduce.map(tuple=>{tuple.swap})
    rdd1.sortByKey(false).map(tuple=>{tuple.swap}).foreach(tuple=>{
      println(tuple)
    })
    
    sc.stop()
    
  }
}