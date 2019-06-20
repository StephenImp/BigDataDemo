package com.cn.demo.array

/**
  * Created by MOZi on 2019/5/16.
  */
class ArrayDemo {

  /**
    * 创建数组两种方式：
    * 1.new Array[String](3)
    * 2.直接Array
    */

  //创建类型为Int 长度为3的数组
  val arr1 = new Array[Int](3)
  //创建String 类型的数组，直接赋值
  val arr2 = Array[String]("s100","s200","s300")
  //赋值
  arr1(0) = 100
  arr1(1) = 200
  arr1(2) = 300


  /**
    * 遍历两种方式
    */
    for(i <- arr1){
      println(i)
    }
    arr1.foreach(i => {
      println(i)
    })

    for(s <- arr2){
      println(s)
    }
    arr2.foreach {
      x => println(x)
    }

  /**
    * 创建二维数组和遍历
    */
    val arr3 = new Array[Array[String]](3)
    arr3(0)=Array("1","2","3")
    arr3(1)=Array("4","5","6")
    arr3(2)=Array("7","8","9")
    for(i <- 0 until arr3.length){
      for(j <- 0 until arr3(i).length){
        print(arr3(i)(j)+"	")
      }
      println()
    }

    var count = 0
    for(arr <- arr3 ;i <- arr){
      if(count%3 == 0){
        println()
      }
      print(i+"	")
      count +=1
    }

    arr3.foreach { arr  => {
      arr.foreach { println }
    }}


    val arr4 = Array[Array[Int]](Array(1,2,3),Array(4,5,6))
    arr4.foreach { arr => {
      arr.foreach(i => {
        println(i)
      })
    }}
    println("-------")
    for(arr <- arr4;i <- arr){
      println(i)
    }


}
object ArrayDemo{

  def main(args: Array[String]): Unit = {

    val listDemo = new ArrayDemo

  }
}
