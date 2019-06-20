package com.cn.klh

/**
  * Created by MOZi on 2019/5/13.
  *
  * 柯里化(Currying)指的是将原来接受两个参数的函数变成新的接受一个参数的函数的过程。
  * 新的函数返回一个以原有第二个参数为参数的函数。
  *
  *
  * 一般的，多参数函数定义为def f(args1)...(argsn) = E,
    当n > 1时，等同于def f(args1)...(args n-1) = {def g(argsn) = E; g}或者def f(args1)...(args n-1) = (argsn => E)。
    如果重复这个过程n次，得到def f = (args1 => (args2 => ... (argsn => E) ) )。
    这种函数定义称为柯里化(Currying)。
  */
class klh {


}


object klh{

  /**
    * 两个数区间中的所有整数求和
    * @param a
    * @param b
    * @return
    */
  def sumInts(a: Int, b: Int): Int ={
    if(a > b) 0 else a + sumInts(a + 1, b)
  }



  /**
    * 连续整数的平方和
    */
  def square(x: Int): Int = {
    x * x
  }
  def sumSquares(a: Int, b: Int): Int ={
    if(a > b) 0 else square(a) + sumSquares(a + 1, b)
  }


  /**
    * 2的幂次的和
    * @param x
    * @return
    */
  def powerOfTwo(x: Int): Int = {
    if(x == 0) 1 else 2 * powerOfTwo(x-1)
  }
  def sumPowersOfTwo(a: Int, b: Int): Int ={
    if(a > b) 0 else powerOfTwo(a) + sumPowersOfTwo(a+1, b)
  }


  //***************************************************************************************************************

  /**
    * 上面的函数都是从a到b的f(n)的累加形式，
    * 我们可以抽取这些函数中共同的部分重新编写函数sum，
    * 其中定义的f作为一个参数传入到高阶函数sum中
    */

  def sum(f: Int => Int, a: Int, b: Int): Int ={
    if(a > b) 0 else f(a) + sum(f, a+1, b)
  }

  def id(x: Int): Int = x
  def square1(x: Int): Int = x * x
  def powerOfTwo1(x: Int): Int = if(x == 0) 1 else 2 * powerOfTwo(x-1)

  def sumInts1(a: Int, b: Int): Int = sum(id, a, b)
  def sumSquared(a: Int, b: Int): Int = sum(square, a, b)
  def sumPowersOfTwo1(a: Int, b: Int): Int = sum(powerOfTwo, a, b)

  //***************************************************************************************************************

  /**
    * 柯里化
    * 将原来接受两个参数的函数变成新的接受一个参数的函数的过程。
    * 新的函数返回一个以原有第二个参数为参数的函数。
    */
  def sum(f: Int => Int): (Int, Int) => Int = {
    def sumF(a: Int, b: Int): Int =
      if(a > b) 0
      else f(a) + sumF(a+1, b)
    sumF
  }

  //于是得到如下定义，这样就简化了参数
  def sumInts2: (Int, Int) => Int = sum(x => x)
  def sumSquared2: (Int, Int) => Int = sum(x => x * x)
  def sumPowersOfTwo2: (Int, Int) => Int = sum(powerOfTwo)


  /**
    * 柯里化函数
    */
  def fun7(a :Int,b:Int)(c:Int,d:Int) = {
    a+b+c+d
  }

  /**
    * 这里相当于main()函数
    *
    * @param args
    */
  def main(args: Array[String]): Unit ={

    //println(sumInts(1,1));
    //println(sumSquares(1,2))

    /**
      * map方法将一个函数应用到某个集合的所有元素并返回结果；
      * foreach将函数应用到每个元素。
      */
    //(1 to 9).map("^" * _).foreach(println _)
    //(1 to 2).map("&" * _).foreach(println _)


    /**
      * filter方法输出所有匹配某个特定条件的元素：
      */
    //(1 to 9).filter(_ % 2 == 0).foreach(println _)

    /**
      * 柯里化
      */
    //println(sumInts2(1,2))

    //来个简单的吧，我日了狗了
    println(fun7(1,2)(3,4))

  }

}
