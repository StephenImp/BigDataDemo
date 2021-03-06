package com.cn.spark.others.pvuv;

import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class Pv {
	public static void main(String[] args) {
		SparkConf conf = new SparkConf();
		conf.setMaster("local").setAppName("PV");
		JavaSparkContext sc = new JavaSparkContext(conf);
		
		JavaRDD<String> lines = sc.textFile("./pvuvdata");
		JavaPairRDD<String, Integer> ipWeb = lines.mapToPair(new PairFunction<String, String, Integer>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Tuple2<String, Integer> call(String line) throws Exception {
				
				return new Tuple2<String, Integer>(line.split("\t")[5],1);
			}
		});
		JavaPairRDD<String, Integer> mapToPair = ipWeb.reduceByKey(new Function2<Integer, Integer, Integer>() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Integer call(Integer v1, Integer v2) throws Exception {
				return v1+v2;
			}
		}).mapToPair(new PairFunction<Tuple2<String,Integer>, Integer, String>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Tuple2<Integer, String> call(Tuple2<String, Integer> t)
					throws Exception {
				return new Tuple2<Integer, String>(t._2,t._1);
			}
		}).sortByKey(false).mapToPair(new PairFunction<Tuple2<Integer,String>, String,Integer>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Tuple2<String, Integer> call(Tuple2<Integer, String> t)
					throws Exception {
				
				return new Tuple2<String, Integer>(t._2,t._1) ;
			}
		});
		List<Tuple2<String, Integer>> take = mapToPair.take(5);
		for(Tuple2<String, Integer> t:take){
			System.out.println(t);
		}
		sc.stop();
	}
}
