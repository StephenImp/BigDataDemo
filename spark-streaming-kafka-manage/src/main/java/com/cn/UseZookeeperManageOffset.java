package com.cn;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import com.google.common.collect.ImmutableMap;
import com.manage.getOffset.GetTopicOffsetFromKafkaBroker;
import com.manage.getOffset.GetTopicOffsetFromZookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.api.java.JavaStreamingContextFactory;
import org.apache.spark.streaming.kafka.HasOffsetRanges;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.apache.spark.streaming.kafka.OffsetRange;
import kafka.cluster.Broker;

import com.fasterxml.jackson.databind.ObjectMapper;

import kafka.api.PartitionOffsetRequestInfo;
import kafka.common.TopicAndPartition;
import kafka.javaapi.OffsetRequest;
import kafka.javaapi.OffsetResponse;
import kafka.javaapi.PartitionMetadata;
import kafka.javaapi.TopicMetadata;
import kafka.javaapi.TopicMetadataRequest;
import kafka.javaapi.TopicMetadataResponse;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.message.MessageAndMetadata;
import kafka.serializer.StringDecoder;
import scala.Tuple2;

public class UseZookeeperManageOffset {
	/**
	 * 使用log4j打印日志，“UseZookeeper.class” 设置日志的产生类
	 */
	static final Logger logger = Logger.getLogger(UseZookeeperManageOffset.class);
	
	
	public static void main(String[] args) {
		/**
		 * 加载log4j的配置文件，方便打印日志
		 */
		ProjectUtil.LoadLogConfig();
		logger.info("project is starting...");
		
		/**
		 * 从kafka集群中得到topic每个分区中生产消息的最大偏移量位置
		 */
		Map<TopicAndPartition, Long> topicOffsets = GetTopicOffsetFromKafkaBroker.getTopicOffsets("node1:9092,node2:9092,node3:9092", "mytopic");
		
		/**
		 * 从zookeeper中获取当前topic每个分区 consumer 消费的offset位置
		 */
		Map<TopicAndPartition, Long> consumerOffsets = 
				GetTopicOffsetFromZookeeper.getConsumerOffsets("node3:2181,node4:2181,node5:2181","zhy","mytopic");
		
		/**
		 * 合并以上得到的两个offset ，
		 * 	思路是：
		 * 		如果zookeeper中读取到consumer的消费者偏移量，那么就zookeeper中当前的offset为准。
		 * 		否则，如果在zookeeper中读取不到当前消费者组消费当前topic的offset，就是当前消费者组第一次消费当前的topic，
		 * 			offset设置为topic中消息的最大位置。
		 */
		if(null!=consumerOffsets && consumerOffsets.size()>0){
            topicOffsets.putAll(consumerOffsets);
        }
		/**
		 * 如果将下面的代码解开，是将topicOffset 中当前topic对应的每个partition中消费的消息设置为0，就是从头开始。
		 */
//		for(Map.Entry<TopicAndPartition, Long> item:topicOffsets.entrySet()){
//          item.setValue(0l);
//		}
		
		/**
		 * 构建SparkStreaming程序，从当前的offset消费消息
		 */
		JavaStreamingContext jsc = SparkStreamingDirect.getStreamingContext(topicOffsets,"zhy");
		jsc.start();
		jsc.awaitTermination();
		jsc.close();
		
	}
}
