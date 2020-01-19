package com.cn.demo.map

/**
  * Created by MOZi on 2019/5/22.
  */
class MapDemo {

    /**
      * 1.map创建
        Map（1 –>”bjsxt’）
        Map((1,”bjsxt”))
        注意：创建map时，相同的key被后面的相同的key顶替掉，只保留一个
      */
    val map = Map(
      "1" -> "bjsxt",
      2 -> "shsxt",
      (3,"xasxt")
    )


  /**
    * 2.获取map的值
        map.get("1").get
        map.get(100).getOrElse(“no value”)：如果map中没有对应项，赋值为getOrElse传的值。
    */
    //获取值
    println(map.get("1").get)
    val result = map.get(8).getOrElse("no value")
    println(result)


  /**
    * 3.遍历map
        for,foreach
    */
    //map遍历
    for(x <- map){
      println("====key:"+x._1+",value:"+x._2)
    }
    map.foreach(f => {
      println("key:"+ f._1+" ,value:"+f._2)
    })


  /**
    * 4.遍历key
        map.keys
    */
    //遍历key
    val keyIterable = map.keys
    keyIterable.foreach { key => {
      println("key:"+key+", value:"+map.get(key).get)
    } }
    println("---------")


  /**
    * 5.遍历value
        map.values
    */
    //遍历value
    val valueIterable = map.values
    valueIterable.foreach { value => {
      println("value: "+ value)
    }}

  /**
    * 6.合并map
        ++  例：map1.++(map2)  --map1中加入map2
        ++:  例：map1.++:(map2) –map2中加入map1
        注意：合并map会将map中的相同key的value替换
    */

    //合并map
    val map1 = Map(
      (1,"a"),
      (2,"b"),
      (3,"c")
    )
    val map2 = Map(
      (1,"aa"),
      (2,"bb"),
      (2,90),
      (4,22),
      (4,"dd")
    )
    map1.++:(map2).foreach(println)


  /**
    * 7.map中的方法举例
        filter:过滤，留下符合条件的记录
        count:统计符合条件的记录数
        contains：map中是否包含某个key
        exist：符合条件的记录存在不存在
    */

    /**
      * map方法
      */
    //count
    val countResult  = map.count(p => {
      p._2.equals("shsxt")
    })
    println(countResult)

    //filter
    map.filter(_._2.equals("shsxt")).foreach(println)

    //contains
    println(map.contains(2))

    //exist
    println(map.exists(f =>{
      f._2.equals("xasxt")

    }))



}
object MapDemo{

}
