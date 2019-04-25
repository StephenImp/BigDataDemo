package com.cn.basic2

/**
  * Created by MOZi on 2019/4/19.
  *
  *
  * 支持嵌套，下层可以访问上层作用域中的名称
  * package com.hadoop{
  * //---------------------
  * package spark
  * }
  *
  * 重命名引入成员(xx=>yy)
  *import java.util.{HashMap==> JavaHashMap}
  *
  * 隐藏包
  * HashMap => _ 把HashMap隐藏掉了
  *
  * 自动引入(java.lang._  scala._)
  *
  *
  * 只能被bb下的句柄所引用
  * package aa.bb.cc.dd
  *
  *   class XXX{
  *     private[bb] def xxx = {}
  *   }
  *
  */
class PackageDemo {

}


object PackageDemo extends App{

}
