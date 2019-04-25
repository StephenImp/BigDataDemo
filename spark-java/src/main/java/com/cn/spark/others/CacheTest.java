package com.cn.spark.others;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.storage.StorageLevel;

/**
 * 持久化算子:
 *
 * 		cache()默认将RDD中的数据存在内存中，懒执行算子
 *
 * 		persist() 可以手动的指定持久化节点.
 *
 * 		persist(StorageLevel.MEMORY_ONLY()) = cache() = persist()
 *
 */
public class CacheTest{

	public static void main(String[] args) {

		SparkConf conf = new SparkConf();
		conf/*.setMaster("local")*/.setAppName("CacheTest");
		JavaSparkContext jsc = new JavaSparkContext(conf);
		JavaRDD<String> lines = jsc.textFile("hdfs://node1:9000/test/NASA_access_log_Aug95");


//		lines = lines.cache();
//		lines = lines.persist(StorageLevel.MEMORY_ONLY_2());
		lines = lines.persist(new StorageLevel(false, true, false, true, 3));
		long startTime = System.currentTimeMillis();
		long count = lines.count();
		long endTime = System.currentTimeMillis();
		System.out.println("共"+count+ "条数据，"+"初始化时间+cache时间+计算时间="+ (endTime-startTime));

		/*
		long countStartTime = System.currentTimeMillis();
		long countrResult = lines.count();
		long countEndTime = System.currentTimeMillis();
		System.out.println("共"+countrResult+ "条数据，"+"计算时间="+ (countEndTime-countStartTime));
		lines.unpersist();

		long countStartTime2 = System.currentTimeMillis();
		long countrResult2 = lines.count();
		long countEndTime2 = System.currentTimeMillis();
		System.out.println("共"+countrResult2+ "条数据，"+"计算时间="+ (countEndTime2-countStartTime2));
		lines.unpersist();
		*/

		while(true){
			
		}
		
//		jsc.stop();
	}
}
