package com.cn.sparksql;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

/**
 * Created by MOZi on 2019/4/24.
 */
public class SqlTest {

    public static void main(String[] args) {

        SparkConf conf = new SparkConf();
        conf.setMaster("local").setAppName("sqlTest");
        JavaSparkContext sc = new JavaSparkContext(conf);

        SQLContext sqlContext = new SQLContext(sc);

        /**
         * DataFrame
         *
         * 这两种方式是一样的。
         */
        Dataset<Row> df = sqlContext.read().format("json").load("./json");
        Dataset<Row> df1 = sqlContext.read().json("./json");

        /**
         * DataFrame 有数据，有列的schema
         * sqlContext 读取json格式的文件，加载DataFrame   DataFrame中的列会按Acsii码排序
         * 写sql查询出来的字段，按查询的字段进行显示。
         *
         */
        df.show();
        df.printSchema();

        //select name,age from xxx where age>18
        Dataset<Row> age = df.select("name","age").where(df.col("age").gt(18));
        age.show();

        /**
         * 将DataFrame注册成临时表
         */
        df.registerTempTable("t1");
        Dataset<Row> sql = sqlContext.sql("select * from t1 where age>18");
        sql.show();


        sc.stop();
    }
}
