package com.cn.demo.func

/**
  * Created by MOZi on 2019/5/14.
  */
class HighOrderFuncDemo {


}

object HighOrderFuncDemo{

  /**
    * 高阶函数
    * 函数的参数是函数
    * 函数的返回是函数
    * 函数的参数和返回都是函数
    */
  //函数的参数是函数
  def hightFun(f : (Int,Int) =>Int, a:Int ) : Int = {
    f(a,100)
  }
  def f(v1 :Int,v2: Int):Int  = {
    v1+v2
  }

  //函数的返回是函数
  //1，2,3,4相加
  def hightFun2(a : Int,b:Int) : (Int,Int)=>Int = {
    def f2 (v1: Int,v2:Int) :Int = {
      v1+v2+a+b
    }
    f2
  }


  //函数的参数是函数，函数的返回是函数
  def hightFun3(f : (Int ,Int) => Int) : (Int,Int) => Int = {
    f
  }

  /**
    * 柯里化函数
    *
    * 柯里化(Currying)指的是将原来接受两个参数的函数变成新的接受一个参数的函数的过程。
    * 新的函数返回一个以原有第二个参数为参数的函数。
    */
  def fun7(a :Int,b:Int)(c:Int,d:Int) = {
    a+b+c+d
  }

  def main(args: Array[String]): Unit = {

    /**
      * 函数的参数是函数
      */
    //println(hightFun(f, 1))


    /**
      *函数的返回是函数
      */
    //println(hightFun2(1,2)(3,5))


    /**
      *函数的参数和返回都是函数
      */
    println(hightFun3(f)(100,200))
//    println(hightFun3((a,b) =>{a+b})(200,200))
//    //以上这句话还可以写成这样
//    //如果函数的参数在方法体中只使用了一次 那么可以写成_表示
//    println(hightFun3(_+_)(200,200))


    /**
      * 柯里化函数
      */
    //println(fun7(1,2)(3,4))

  }

}
