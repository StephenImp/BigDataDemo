package com.cn.demo.basic

/**
  * Created by MOZi on 2019/5/14.
  */
class Demo {

  /**
    * ③
    * 创建类
    */
  val name = "zhangsan"
  val age = 18
  def sayName() = {
    "my name is "+ name
  }

}

object Demo{

  /**
    * ①
    * 定义变量或者常量的时候，也可以写上返回的类型，一般省略
    */
  val a:Int = 10

  /**
    * ②
    * 定义变量和常量
    * 变量 :用 var 定义 ，可修改
    * 常量 :用 val 定义，不可修改
    */
  var name = "zhangSan"
  name ="lisi"
  val gender = "m"
  //gender = "f";  报错，不能再赋值



  def main(args: Array[String]): Unit = {

    /**
      * ③
      * 创建类
      */
//    val demo = new Demo()
//    println(demo.age)
//    println(demo.sayName())


    /**
      * ④ if else
      */
//    val age =18
//    if (age < 18 ){
//      println("no allow")
//    }else if (18<=age&&age<=20){
//      println("allow with other")
//    }else{
//      println("allow self")
//    }

    /**
      *
      * ⑤
      * to和until
      * 例：
      * 1 to 10 返回1到10的Range数组，包含10
      * 1 until 10 返回1到10 Range数组 ，不包含10
      */

//    println(1 to 10 )//打印 1, 2, 3, 4, 5, 6, 7, 8, 9, 10
//    println(1.to(10))//与上面等价，打印 1, 2, 3, 4, 5, 6, 7, 8, 9, 10
//
//    println(1 to (10 ,2))//步长为2，从1开始打印 ，1,3,5,7,9
//    println(1.to(10, 2))
//
//    println(1 until 10 ) //不包含最后一个数，打印 1,2,3,4,5,6,7,8,9
//    println(1.until(10))//与上面等价
//
//    println(1 until (10 ,3 ))//步长为2，从1开始打印，打印1,4,7


    /**
      * ⑥
      * for 循环
      */
//    for( i <- 1 to 10 ){
//      println(i)
//    }


    /**
      * ⑦
      * 创建多层for循环
      */
    //可以分号隔开，写入多个list赋值的变量，构成多层for循环
    //scala中 不能写count++ count-- 只能写count+
//    var count = 0;
//    for(i <- 1 to 10; j <- 1 until 10){
//      println("i="+ i +",	j="+j)
//      count += 1
//      println(count)
//    }
//    println(count);

    //例子： 打印小九九
//    for(i <- 1 until 10 ;j <- 1 until 10){
//      if(i>=j){
//        print(i +" * " + j + " = "+ i*j+"	")
//
//      }
//      if(i==j ){
//        println()
//      }
//
//    }


    /**
      * ⑧
      * for循环中可以加条件判断，可以使用分号隔开，也可以不使用分号
      */
    //可以在for循环中加入条件判断
//    for(i<- 1 to 10 ;if (i%2) == 0 ;if (i == 4) ){
//      println(i)
//    }

    /**
      * ⑨
        scala中不能使用count++，count—只能使用count = count+1 ，count += 1
        for循环用yield 关键字返回一个集合
        while循环，while（）{}，do {}while()
      */
    //将for中的符合条件的元素通过yield关键字返回成一个集合
//    val list = for(i <- 1 to 10  ; if(i > 5 )) yield i
//    for( w <- list ){
//      println(w)
//    }

    /**
      * while 循环
      */
    var index = 0
//    while(index < 100 ){
//      println("第"+index+"次while 循环")
//      index += 1
//    }
    index = 0
    do{
      index +=1
      println("第"+index+"次do while 循环")
    }while(index <100 )

  }

}
