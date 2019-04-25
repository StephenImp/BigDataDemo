package com.cn.spark.transformations.caseDemo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.Optional;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;

import scala.Tuple2;

/**
 * 其他的一些算子的例子
 */
public class TransformationDemo1 {

	public static void main(String[] args) {

		SparkConf conf = new SparkConf().setMaster("local").setAppName("test");
		JavaSparkContext sc = new JavaSparkContext(conf);


		JavaRDD<String> rdd = sc.parallelize(Arrays.asList("a","a","b","b","b","c","d"),4);
		System.out.println("rdd partition length = " + rdd.partitions().size());

		List<Tuple2<String, String>> list2 = Arrays.asList(new Tuple2<String, String>("p", "l"));


		/**
		 * 注意这两种方式的返回值的区别
		 *
		 * stringStringJavaPairRDD 才是返回的K,V格式的RDD
		 */
		//JavaRDD<Tuple2<String, String>> parallelize = sc.parallelize(list2, 2);
		JavaPairRDD<String, String> stringStringJavaPairRDD = sc.parallelizePairs(list2, 2);

		/**
		 * distinct 去重
		 */
		JavaRDD<String> distinct = rdd.distinct();
		distinct.foreach(new VoidFunction<String>() {

			private static final long serialVersionUID = 1L;

			@Override
			public void call(String arg0) throws Exception {
				System.out.println(arg0);
			}
		});


		/**
		 * 转换成KV 格式的RDD
		 *
		 * 以下是去重逻辑
		 */
		JavaPairRDD<String, Integer> mapToPair = rdd.mapToPair(new PairFunction<String, String, Integer>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Tuple2<String, Integer> call(String s) throws Exception {
				// TODO Auto-generated method stub
				return new Tuple2<String, Integer>(s,1);
			}
		});
		JavaPairRDD<String, Integer> reduceByKey = mapToPair.reduceByKey(new Function2<Integer, Integer, Integer>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Integer call(Integer v1, Integer v2) throws Exception {
				return v1*v2;
			}
		});
		JavaRDD<String> map = reduceByKey.map(new Function<Tuple2<String,Integer>, String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String call(Tuple2<String, Integer> tuple) throws Exception {
				return tuple._1;
			}
		});
		map.foreach(new VoidFunction<String>() {

			private static final long serialVersionUID = 1L;

			@Override
			public void call(String arg0) throws Exception {
				System.out.println(arg0);
			}
		});



		
		/**
		 * foreachPartition 一个分区一个分区的遍历数据，action算子
		 */
		rdd.foreachPartition(new VoidFunction<Iterator<String>>() {

			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void call(Iterator<String> iter) throws Exception {
				List<String> list = new ArrayList<String>();

				System.out.println("创建数据库连接。。。。");

				while(iter.hasNext()) {
					String s = iter.next();
					System.out.println("拼接sql。。。。"+s);
					list.add(s);
				}

				System.out.println("关闭数据库连接。。。。");
			}
		});
		
		/**
		 * foreach 一条条处理数据
		 *
		 * 	action 算子  不返回RDD了。
		 */
		rdd.foreach(new VoidFunction<String>() {

			private static final long serialVersionUID = 1L;

			@Override
			public void call(String s) throws Exception {

				System.out.println("创建数据库连接。。。。");
				System.out.println("插入数据库连接。。。。"+s);
				System.out.println("关闭数据库连接。。。。");
			}
		});
		
		
		/**
		 * mapPartitions	完全可以替代map
		 *
		 * 对比下面的map
		 *
		 * 每次处理一个分区中的数据
		 *
		 * 一个分区创建一次数据库连接。
		 *
		 */
		JavaRDD<String> mapPartitions = rdd.mapPartitions(new FlatMapFunction<Iterator<String>, String>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Iterable<String> call(Iterator<String> iter) throws Exception {
				List<String> list = new ArrayList<String>();

				System.out.println("创建数据库连接。。。。");

				while(iter.hasNext()) {
					String s = iter.next();
					System.out.println("拼接sql。。。。"+s);
					list.add(s);
				}

				System.out.println("关闭数据库连接。。。。");
				return list;
			}
		});
		mapPartitions.collect();


		/**
		 * map		一对一
		 *
		 * 有几条数据就会创建几条链接。
		 */
		JavaRDD<String> map1 = rdd.map(new Function<String, String>() {

			private static final long serialVersionUID = 1L;

			@Override
			public String call(String s) throws Exception {

				System.out.println("创建数据库连接。。。。");
				System.out.println("插入数据库连接。。。。"+s);
				System.out.println("关闭数据库连接。。。。");

				return s+"~";
			}
		});
		map1.collect();

		
		JavaPairRDD<String, String> rdd1 = sc.parallelizePairs(Arrays.asList(
				new Tuple2<String,String>("zhangsan","a"),
				new Tuple2<String,String>("zhangsan","aa"),
				new Tuple2<String,String>("zhangsan","aaa"),
				new Tuple2<String,String>("lisi","b"),
				new Tuple2<String,String>("lisi","bb"),
				new Tuple2<String,String>("wangwu","c")
				));
		JavaPairRDD<String, String> rdd2 = sc.parallelizePairs(Arrays.asList(
				new Tuple2<String,String>("zhangsan","10000"),
				new Tuple2<String,String>("lisi","1"),
				new Tuple2<String,String>("lisi","10"),
				new Tuple2<String,String>("lisi","1000"),
				new Tuple2<String,String>("wangwu","3"),
				new Tuple2<String,String>("wangwu","30"),
				new Tuple2<String,String>("maliu","200")
				));


//		JavaPairRDD<String, Integer> rdd2 = sc.parallelizePairs(Arrays.asList(
//				new Tuple2<String,Integer>("zhangsan",100),
//				new Tuple2<String,Integer>("lisi",200),
//				new Tuple2<String,Integer>("wangwu",300),
//				new Tuple2<String,Integer>("tianqi",400)
//		),2);

		JavaPairRDD<String, String> rdd3 = sc.parallelizePairs(Arrays.asList(
				new Tuple2<String,String>("zhangsan1","100"),
				new Tuple2<String,String>("lisi1","200"),
				new Tuple2<String,String>("wangwu1","300"),
				new Tuple2<String,String>("tianqi1","400")
		),2);


		/**
		 * cogroup 将两个RDD的key合并，每个RDD中的key对应一个value集合
		 *
		 * (zhangsan,([a,aa,aaa],[10000]))
		 * (wangwu,([c],[3,30]))
		 * (maliu,([],[200]))
		 * (lisi,([b,bb],[1,10,1000]))
		 *
		 */
		JavaPairRDD<String, Tuple2<Iterable<String>, Iterable<String>>> cogroup = rdd1.cogroup(rdd2);
		cogroup.foreach(new VoidFunction<Tuple2<String,Tuple2<Iterable<String>,Iterable<String>>>>() {

			private static final long serialVersionUID = 1L;

			@Override
			public void call(Tuple2<String, Tuple2<Iterable<String>, Iterable<String>>> arg0) throws Exception {
				System.out.println(arg0);
			}
		});
		
		
		/**
		 * subtract 取差集
		 */
		JavaPairRDD<String, String> subtract = rdd2.subtract(rdd1);

		subtract.foreach(new VoidFunction<Tuple2<String,String>>() {

			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void call(Tuple2<String, String> arg0) throws Exception {
				System.out.println(arg0);
			}
		});


		/**
		 * intersection 取交集
		 *
		 *
		 */
		JavaPairRDD<String, String> intersection = rdd1.intersection(rdd2);

		intersection.foreach(new VoidFunction<Tuple2<String,String>>() {

			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void call(Tuple2<String, String> arg0) throws Exception {
				System.out.println(arg0);
			}
		});


		/**
		 * union 合并RDD， 类型一致
		 *
		 * 分区数，两个RDD分区(partition)相加的 个数
		 *
		 * 逻辑上的分区合并
		 *
		 */
		JavaPairRDD<String, String> union = rdd1.union(rdd3);
		System.out.println("union partition length = "+union.partitions().size());

		union.foreach(new VoidFunction<Tuple2<String,String>>() {

			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void call(Tuple2<String, String> arg0) throws Exception {
				System.out.println(arg0);
			}
		});
		
		/**
		 *
		 * join
		 *
		 * 按照两个RDD的key去关联
		 *
		 * Join 后的RDD与父RDD分区多的那个分区数一致
		 *
		 * 相同的key join 在一起
		 *
		 *
		 * new Tuple2<String,String>("zhangsan","a")
		 * new Tuple2<String,Integer>("zhangsan",100)
		 *
		 * (zhangsan,(a,100))
		 *
		 */
		JavaPairRDD<String, Tuple2<String, Integer>> joinRDD = rdd1.join(rdd2);
		System.out.println("joinRDD partition legth = "+joinRDD.partitions().size());
		joinRDD.foreach(new VoidFunction<Tuple2<String,Tuple2<String,Integer>>>() {

			private static final long serialVersionUID = 1L;

			@Override
			public void call(Tuple2<String, Tuple2<String, Integer>> arg0) throws Exception {
				System.out.println(arg0);
			}
		});
		
		
		
		/**
		 * fullOuterJoin
		 *
		 * (zhangsan,(Optional.of(a),Optional.of(100)))
		 *
		 */
		JavaPairRDD<String, Tuple2<Optional<String>, Optional<Integer>>> fullOuterJoin = rdd1.fullOuterJoin(rdd2);
		fullOuterJoin.foreach(new VoidFunction<Tuple2<String,Tuple2<Optional<String>,Optional<Integer>>>>() {

			private static final long serialVersionUID = 1L;

			@Override
			public void call(Tuple2<String, Tuple2<Optional<String>, Optional<Integer>>> arg0) throws Exception {
				System.out.println(arg0);
			}
		});


		/**
		 * rightOuterJoin
		 *
		 *  (zhangsan,(Optional.of(a),100))
		 *	(maliu,(Optional.absent(),400))
		 */
		JavaPairRDD<String, Tuple2<Optional<String>, Integer>> rightOuterJoin = rdd1.rightOuterJoin(rdd2);
		rightOuterJoin.foreach(new VoidFunction<Tuple2<String,Tuple2<Optional<String>,Integer>>>(
				) {

			private static final long serialVersionUID = 1L;

			@Override
			public void call(Tuple2<String, Tuple2<Optional<String>, Integer>> arg0) throws Exception {
				System.out.println(arg0);
			}
		});
		
		/**
		 * leftOuterJoin
		 *
		 * Optional
		 *
		 * 			scala	some(有值)		none(无值)
		 *			java	of(有值)		absent(无值)
		 *
		 *	(zhangsan,(a,Optional.of(100)))
		 *	(maliu,(a,Optional.absent()))
		 *
		 */
		JavaPairRDD<String, Tuple2<String, Optional<Integer>>> leftOuterJoin = rdd1.leftOuterJoin(rdd2);
		leftOuterJoin.foreach(new VoidFunction<Tuple2<String,Tuple2<String,Optional<Integer>>>>() {

			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void call(Tuple2<String, Tuple2<String, Optional<Integer>>> tuple) throws Exception {
				String key = tuple._1;
				String value1 = tuple._2._1;
				Optional<Integer> option = tuple._2._2;
				if(option.isPresent()) {
					System.out.println("key = "+key +", value1 = "+value1+" ,value2 ="+option.get());
				}else {
					System.out.println("key = "+key +", value1 = "+value1+" ,value2 =Null");

				}
			}
		});
		
		
		sc.stop();
				
	}
}
