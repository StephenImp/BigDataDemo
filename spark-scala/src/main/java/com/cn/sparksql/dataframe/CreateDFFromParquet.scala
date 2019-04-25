package com.cn.sparksql.dataframe

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.SaveMode

object CreateDFFromParquet {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setMaster("local").setAppName("parquet")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    val jsonRDD = sc.textFile("sparksql/json")
    val df = sqlContext.read.json(jsonRDD)
    df.show()
    /**
      * 将DF保存为parquet文件
      */
    df.write.mode(SaveMode.Overwrite).format("parquet").save("./sparksql/parquet")
    df.write.mode(SaveMode.Overwrite).parquet("./sparksql/parquet")
    /**
     * 读取parquet文件
     */
    var result = sqlContext.read.parquet("./sparksql/parquet")
    result = sqlContext.read.format("parquet").load("./sparksql/parquet")
    result.show()
    sc.stop()
  }
}