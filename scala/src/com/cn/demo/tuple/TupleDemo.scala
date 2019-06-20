package com.cn.demo.tuple

/**
  * Created by MOZi on 2019/5/22.
  * 1.元组定义
    与列表一样，与列表不同的是元组可以包含不同类型的元素。
    元组的值是通过将单个的值包含在圆括号中构成的。
  */
class TupleDemo {

  /**
    * 2.创建元组与取值
      val  tuple = new Tuple（1） 可以使用new
      val tuple2  = Tuple（1,2） 可以不使用new，也可以直接写成val tuple3 =（1,2,3）
      取值用”._XX” 可以获取元组中的值
      注意：tuple最多支持22个参数
    */
    //创建，最多支持22个
    val tuple = new Tuple1(1)
    val tuple2 = Tuple2("zhangsan",2)
    val tuple3 = Tuple3(1,2,3)
    val tuple4 = (1,2,3,4)
    val tuple18 = Tuple18(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18)
    val tuple22 = new Tuple22(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22)

    //使用
    println(tuple2._1 + "\t"+tuple2._2)
    val t = Tuple2((1,2),("zhangsan","lisi"))
    println(t._1._2)

  /**
    * 3.元组的遍历
      tuple.productIterator得到迭代器，进而遍历
    */
    //遍历
    val tupleIterator = tuple22.productIterator
    while(tupleIterator.hasNext){
      println(tupleIterator.next())
    }

  /**
    * 4.swap,toString方法
      注意：swap元素翻转，只针对二元组
    */
  /**
    * 方法
    */
  //翻转，只针对二元组
  println(tuple2.swap)

  //toString
  println(tuple3.toString())


}
object TupleDemo{

}
