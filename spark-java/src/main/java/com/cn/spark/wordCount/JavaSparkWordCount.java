package com.cn.spark.wordCount;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;

import scala.Tuple2;

/**
 * 2019.4.22
 */
public class JavaSparkWordCount {

	public static void main(String[] args) {
		/**
		 * conf 
		 * 	1.可以设置spark的运行模式
		 * 	2.可以设置spark在webui中显示的application的名称。
		 * 	3.可以设置当前spark application 运行的资源(内存+core)
		 *
		 * 	eg:	笔记本电脑	4核8线程
		 *		说明每个核支持双线程		一个核在某一个默认时刻可以跑2个线程		不是去争抢时间片
		 *
		 *		spark中  一个core只能跑一个task
		 *		所以4核8线程的笔记本最多可以设置	8个core
		 *
		 * 
		 * Spark运行模式：
		 * 	1.local --在eclipse ，IDEA中开发spark程序要用local模式，本地模式，多用于测试
		 *  2.stanalone -- Spark 自带的资源调度框架，支持分布式搭建,Spark任务可以依赖standalone调度资源
		 *  3.yarn -- hadoop 生态圈中资源调度框架。Spark 也可以基于yarn 调度资源
		 *  4.mesos -- 资源调度框架
		 */
		SparkConf conf = new SparkConf();
		conf.setMaster("local");//可以设置spark的运行模式
		conf.setAppName("JavaSparkWordCount");
		
		/**
		 * SparkContext 是通往集群的唯一通道
		 *
		 * Spark 底层操作的对象都是RDD
		 */
		JavaSparkContext sc  = new JavaSparkContext(conf);
		/**
		 * sc.textFile 读取文件
		 */
		JavaRDD<String> lines = sc.textFile("./words");//hello bjsxt
		
		/**
		 * flatMap 进一条数据出多条数据，一对多关系
		 *
		 * words	切分后的单词
		 *
		 */
		JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {

			private static final long serialVersionUID = 1L;

			/**
			 *
			 * @param line	需要切分的每一行数据
			 * @return
			 * @throws Exception
			 */
			@Override
			public Iterator<String> call(String line) throws Exception {

				List<String> list = Arrays.asList(line.split(" "));

				return (Iterator)list;
			}
		});

		/**
		 * 在java中 如果想让某个RDD转换成K,V格式 使用xxxToPair
		 * K,V格式的RDD:JavaPairRDD<String, Integer>
		 *
		 *     String 	进来的单词		kv调转的话  把 Tuple2 再 mapToPair 一次
		 *     							这里比较的话，还是scala 方便一些，api都封装好了。
		 *     String	K 的类型
		 *     Integer	value 的类型
		 *
		 */
		JavaPairRDD<String, Integer> pairWords = words.mapToPair(new PairFunction<String, String, Integer>() {

			private static final long serialVersionUID = 1L;

			/**
			 * Tuple2 是二元组
			 *
			 * 将单词进行处理
			 *
			 * @param word
			 * @return
			 * @throws Exception
			 */
			@Override
			public Tuple2<String, Integer> call(String word) throws Exception {
				return new Tuple2<String, Integer>(word,1);
			}
		});
		
		/**
		 * reduceByKey
		 * 1.先将相同的key分组
		 *
		 *
		 * (a,1)			(a,1)
		 * (a,1)			(a,1)
		 * (b,1)			(a,1)
		 * (c,1)	--->	(b,1)
		 * (a,1)			(b,1)
		 * (b,1)			(c,1)
		 *
		 *
		 * 2.对每一组的key对应的value去按照你的逻辑去处理
		 *
		 * 3个a跑一个reduceByKey	1+1+1
		 * 2个b跑一个reduceByKey	1+1
		 * 1个c不跑reduceByKey		直接返回1
		 *
		 * 这里wordCount的逻辑结束了
		 *
		 */
		JavaPairRDD<String, Integer> result = pairWords.reduceByKey(new Function2<Integer, Integer, Integer>() {
			
			private static final long serialVersionUID = 1L;

			/**
			 *
			 * @param v1
			 * @param v2
			 * @return
			 * @throws Exception
			 */
			@Override
			public Integer call(Integer v1, Integer v2) throws Exception {
				return v1+v2;
			}
		});


		/**
		 * 遍历查看结果
		 *
		 * 从二元组中查询结果
		 *
		 */
		result.foreach(new VoidFunction<Tuple2<String,Integer>>() {
			
			private static final long serialVersionUID = 639147013282066178L;

			@Override
			public void call(Tuple2<String, Integer> tuple) throws Exception {
				System.out.println(tuple);
			}
		});

		sc.stop();
		
	}
}
