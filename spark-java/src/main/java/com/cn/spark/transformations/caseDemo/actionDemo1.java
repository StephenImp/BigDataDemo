package com.cn.spark.transformations.caseDemo;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.VoidFunction;

import scala.Tuple2;

public class actionDemo1 {

	public static void main(String[] args) {

		SparkConf conf = new SparkConf().setMaster("local").setAppName("day03test");
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaPairRDD<String, String> rdd1 = sc.parallelizePairs(Arrays.asList(

				new Tuple2<String,String>("zhangsan","18"),
				new Tuple2<String,String>("zhangsan","18"),
				new Tuple2<String,String>("lisi","19"),
				new Tuple2<String,String>("lisi","190"),
				new Tuple2<String,String>("wangwu","100")
			),2);

		JavaPairRDD<String, Integer> rdd2 = sc.parallelizePairs(Arrays.asList(
				new Tuple2<String,Integer>("zhangsan",100),
				new Tuple2<String,Integer>("zhangsan",200),
				new Tuple2<String,Integer>("lisi",300),
				new Tuple2<String,Integer>("lisi",400),
				new Tuple2<String,Integer>("wangwu",500),
				new Tuple2<String,Integer>("wangwu",600)
		));

		/**
		 * countByValue()
		 *  Action 算子，对RDD中相同的元素计数
		 *
		 *  key = (zhangsan,18),value = 2
		 *  key = (lisi,190),value = 1
		 */
		Map<Tuple2<String, String>, Long> countByValue = rdd1.countByValue();
		Set<Entry<Tuple2<String, String>, Long>> entrySet = countByValue.entrySet();
		for(Entry<Tuple2<String, String>, Long> entry :entrySet) {
			Tuple2<String, String> key = entry.getKey();
			Long value = entry.getValue();
			System.out.println("key = "+key+",value ="+value);
		}
		
		
		
		/**
		 * countByKey()
		 *  Action 算子，对RDD中相同的key的元素计数
		 *
		 *  key = zhangsan,value = 2
		 */
		Map<String, Long> countByKey = rdd1.countByKey();

		Set<Entry<String, Long>> entrySet1 = countByKey.entrySet();
		for(Entry<String, Long> entry :entrySet1) {
			String key = entry.getKey();
			Object value = entry.getValue();
			System.out.println("key = "+key+",value ="+value);
		}
		
		
		/**
		 * reduce  Action算子，对RDD中的每个元素 使用传递的逻辑去处理
		 *
		 * 15
		 */
		
		JavaRDD<Integer> rdd3 = sc.parallelize(Arrays.asList(1,2,3,4,5));
		Integer reduce = rdd3.reduce(new Function2<Integer, Integer, Integer>() {

			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Integer call(Integer v1, Integer v2) throws Exception {
				return v1+v2;
			}
		});
		System.out.println(reduce);


		/**
		 * zipWithIndex 
		 * 	给RDD中的每个元素与当前元素的下标压缩成一个K，V格式的RDD
		 *
		 * 	将每一组元素加index
		 *
		 * 	((zhangsan,18),0)
		 * 	((zhangsan,180),1)
		 */
		JavaPairRDD<Tuple2<String, String>, Long> zipWithIndex = rdd1.zipWithIndex();
		zipWithIndex.foreach(new VoidFunction<Tuple2<Tuple2<String,String>,Long>>() {

			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void call(Tuple2<Tuple2<String, String>, Long> arg0) throws Exception {
				System.out.println(arg0);
			}
		});


		/**
		 * zip 将两个RDD压缩成一个K,V格式的RDD
		 * 两个RDD中每个分区的数据要一致
		 *
		 * ((zhangsan,18),(zhangsan,100))
		 * ((zhangsan,180),(zhangsan,200))
		 */
		JavaPairRDD<Tuple2<String, String>, Tuple2<String, Integer>> zip = rdd1.zip(rdd2);

		zip.foreach(new VoidFunction<Tuple2<Tuple2<String,String>,Tuple2<String,Integer>>>() {

			private static final long serialVersionUID = 1L;

			@Override
			public void call(Tuple2<Tuple2<String, String>, Tuple2<String, Integer>> arg0) throws Exception {
				System.out.println(arg0);
			}
		});


		/**
		 * groupByKey
		 *  将RDD中相同的Key分组
		 *
		 *  (zhangsan,[18,80])
		 */
		JavaPairRDD<String, Iterable<String>> groupByKey = rdd1.groupByKey();
		groupByKey.foreach(new VoidFunction<Tuple2<String,Iterable<String>>>() {

			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void call(Tuple2<String, Iterable<String>> arg0) throws Exception {
				System.out.println(arg0);
			}
		});
		sc.stop();
	}
}
