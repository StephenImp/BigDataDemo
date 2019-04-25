package com.cn.spark.others.partitioner

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.Partitioner
import scala.collection.mutable.ListBuffer
/**
 * 自定义分区器
 */
class MyPartitioner(num:Int) extends Partitioner{ 
	  override def numPartitions = num
    override def getPartition(key: Any): Int = {
      key.asInstanceOf[Int]%num
    }
}
object partitionerTest {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setMaster("local").setAppName("partitioner")
    val sc = new SparkContext(conf)
    val nameRDD = sc.parallelize(List(
        (1,"zhangsan"),
        (2,"lisi"),
        (3,"wangwu"),
        (4,"maliu"),
        (5,"zhaoqi"),
        (6,"shunba")
        ), 2);
    
    nameRDD.mapPartitionsWithIndex((index,iter)=>{
      val list = new ListBuffer[String]()
      while(iter.hasNext){
        println("nameRDD partitionID = "+index+",value = "+iter.next())
      }
      list.iterator
    }, true).collect()
    
    println("*****************************************")
    val partitionRDD = nameRDD.partitionBy(new MyPartitioner(2))
    
    partitionRDD.mapPartitionsWithIndex((index,iter)=>{
      val list = new ListBuffer[String]()
      while(iter.hasNext){
        println("partitionRDD partitionID = "+index+",value = "+iter.next())
      }
      list.iterator
    }, true).collect()
    
    sc.stop()
  }
}