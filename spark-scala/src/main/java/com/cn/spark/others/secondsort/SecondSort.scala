package com.cn.spark.others.secondsort

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD.rddToOrderedRDDFunctions

object SecondSort {
  def main(args: Array[String]): Unit = {
    
      val sconf=new SparkConf().setAppName("SecondSort").setMaster("local")
      val sc=new SparkContext(sconf)
      val lines=sc.textFile("secondSort.txt")
      
      val pairs=  lines.map { x=>(new SecondSortKey(x.split(" ")(0).toInt,x.split(" ")(1).toInt),x) }
      val sortedPairs= pairs.sortByKey(false)
//      val sortedPairs = pairs.sortBy(_._1, false)
      sortedPairs.map(_._2).foreach {println }
  }
}

class SecondSortKey(val first:Int,val second:Int) extends  Ordered[SecondSortKey] with Serializable {
  def compare(that: SecondSortKey): Int = {
    if(this.first-that.first==0)
      this.second- that.second 
    else 
      this.first-that.first
  }
}