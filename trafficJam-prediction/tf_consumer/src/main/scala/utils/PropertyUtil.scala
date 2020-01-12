package utils

import java.util.Properties

object PropertyUtil {
  val properties = new Properties()

  try{
    //加载配置属性
    val inputStream = ClassLoader.getSystemResourceAsStream("kafka.properties")
    properties.load(inputStream)
  }catch {
    case ex:Exception => println(ex)
  }finally {}

  /**
    *  通过key得到kafka的属性值
    * @param key 入参
    * @return 返回String类型的数据
    */
  def getProperty(key: String): String = properties.getProperty(key)

}
