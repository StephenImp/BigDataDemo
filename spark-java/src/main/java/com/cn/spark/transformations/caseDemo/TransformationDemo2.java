package com.cn.spark.transformations.caseDemo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;

public class TransformationDemo2 {

	public static void main(String[] args) {

		SparkConf conf = new SparkConf().setMaster("local").setAppName("day03test");
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaRDD<String> rdd1 = sc.parallelize(Arrays.asList(
					"love1","love2","love3","love4",
					"love5","love6","love7","love8",
					"love9","love10","love11","love12"
				),3);
		
//		System.out.println("rdd1 partition length = "+rdd1.partitions().size());
		
		/**
		 * mapPartitionWithIndex:
		 *
		 * 	会将RDD中的partition索引下标带出来，index 是每个partition的索引下标
		 */
		JavaRDD<String> mapPartitionsWithIndex = rdd1.mapPartitionsWithIndex(new Function2<Integer, Iterator<String>, Iterator<String>>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Iterator<String> call(Integer index, Iterator<String> iter) throws Exception {
				List<String> list = new ArrayList<String>();
				
				while(iter.hasNext()) {
					String one = iter.next();
					list.add("rdd1 partition index = 【"+index+"】, value = 【"+one+"】");
				}
				return list.iterator();
			}
		}, true);

//		List<String> collect1 = mapPartitionsWithIndex.collect();
//		for (String s:collect1){
//			System.out.println(s);
//		}


		/**
		 * repartition
		 *  repartition 是有shuffle的算子，可以对RDD重新分区。可以增加分区，也可以减少分区。
		 *  repartition = coalesce(numPartitions,true)
		 */
		JavaRDD<String> rdd3 = mapPartitionsWithIndex.repartition(2);


		/**
		 * coalesce
		 * 	coalesce 与repartition一样，可以对RDD进行分区，可以增多分区，也可以减少分区。
		 * 	coalsece(numPartitions,shuffle [Boolean = false]) false不产生shuffle
		 */
		JavaRDD<String> rdd2 = mapPartitionsWithIndex.coalesce(4,false);
		
		System.out.println("rdd2 partition length = "+rdd2.partitions().size());
		
		JavaRDD<String> mapPartitionsWithIndex2 = rdd2.mapPartitionsWithIndex(new Function2<Integer, Iterator<String>, Iterator<String>>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Iterator<String> call(Integer index, Iterator<String> iter) throws Exception {
				List<String> list = new ArrayList<String>();
				
				while(iter.hasNext()) {
					String one = iter.next();
					list.add("rdd2 partition index = 【"+index+"】, value = 【"+one+"】");
				}
				return list.iterator();
			}
		}, true);
		List<String> collect = mapPartitionsWithIndex2.collect();
		for(String s :collect) {
			System.out.println(s);
		}
		sc.stop();
	}
}
