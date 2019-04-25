package com.cn.kafka.high;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * 消费者
 */
public class CustomConsumer {

	public static void main(String[] args) {

		Properties props = new Properties();
		// 定义kakfa 服务的地址，不需要将所有broker指定上 
		props.put("bootstrap.servers", "hadoop102:9092");

		// 制定consumer group 
		props.put("group.id", "g1");//默认读的是最大的offset


		/**
		 * 新建CG，重新读取分区中的offset时，需要这个配置
		 * 为什么不从0开始读呢，因为offset在分区中默认保存7天。
		 *
		 * 消息重复消费的方式：
		 * 		①重建消费者
		 * 		②利用低级API指定offset
		 */
		//props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");

		// 是否自动确认offset 
		props.put("enable.auto.commit", "true");

		// 自动确认offset的时间间隔 	这里有可能会产生重复性消费。
		props.put("auto.commit.interval.ms", "1000");
		// key的序列化类
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		// value的序列化类 
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

		// 定义consumer 
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		
		// 消费者订阅的topic, 可同时订阅多个 
		consumer.subscribe(Arrays.asList("test1"));
		//consumer.subscribe(Collections.singletonList("test1"));


		/**
		 * 指定消费0号分区的，从哪个offset开始消费。
		 * 重置offset
		 */
		//consumer.assign(Collections.singletonList(new TopicPartition("second",0)));
		//consumer.seek(new TopicPartition("second",0),2);

		while (true) {
			// 读取数据，读取超时时间为100ms 	每隔100ms拉取一次数据
			ConsumerRecords<String, String> records = consumer.poll(100);

			/**
			 * consumer  是一个分区一个分区的数据进行获取的。
			 * 分区内有序
			 */
			for (ConsumerRecord<String, String> record : records)
				System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
		}
	}
}
