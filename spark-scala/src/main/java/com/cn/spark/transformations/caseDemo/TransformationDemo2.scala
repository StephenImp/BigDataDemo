package com.cn.spark.transformations.caseDemo

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import scala.collection.mutable.ListBuffer

object TransformationDemo2 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("test")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(Array(
          (1,"a"),(2,"b"),(3,"c"),(3,"d")
        ),3);
    
    rdd1.countByKey().foreach(println)
    
//    rdd1.countByValue().foreach(println)
    
    
//    val rdd2 = sc.parallelize(Array(
//    		"love1","love2","love3","love4",
//    		"love5","love6","love7","love8",
//    		"love9","love10","love11","love12"
//    		),3);
//    rdd1.zipWithIndex().foreach(println)
    
    
//    rdd1.zip(rdd2).foreach(println)
//    val rdd2 = rdd1.mapPartitionsWithIndex((index,iter)=>{
//      val list = new ListBuffer[String]()
//      while(iter.hasNext){
//    	  list.+=("rdd1 partition index = "+index+",value = "+iter.next())
//      }
//      list.iterator
//    }, true)
//    rdd2.coalesce(4, false)
    
//    rdd2.foreach(println)
    sc.stop()
    
  }
}