package com.cn.kafka.high;

import java.util.Properties;

import org.apache.kafka.clients.producer.*;

/**
 * 回调生产者
 */
public class CallBackProducer {

	public static void main(String[] args) throws InterruptedException {

		//ProducerConfig	属性在这里都有文档

		Properties props = new Properties();
		// Kafka服务端的主机名和端口号		***
		props.put("bootstrap.servers", "hadoop102:9092,hadoop103:9092,hadoop104:9092");
		// 等待所有副本节点的应答	应答级别
		props.put("acks", "all");
		// 消息发送最大尝试次数
		props.put("retries", 0);


		/**
		 * 当数据量到达16K或者提交时间延迟到达后。
		 */
		// 一批消息处理大小
		props.put("batch.size", 16384);
		// 增加服务端请求延时
		props.put("linger.ms", 1);

		// 发送缓存区内存大小   整个的Producer 总共能缓存的数据。
		props.put("buffer.memory", 33554432);

		// key序列化	***
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		// value序列化	***
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		// 自定义分区
		props.put("partitioner.class", "com.atguigu.kafka.CustomPartitioner");

		//创建生产者对象
		KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(props);

		for (int i = 0; i < 50; i++) {

			Thread.sleep(500);
			kafkaProducer.send(new ProducerRecord<String, String>("test1", "hh" + i), new Callback() {

				@Override
				public void onCompletion(RecordMetadata metadata, Exception exception) {

					//确认是否发送成功
					if (metadata != null) {
						System.out.println(metadata.partition() + "---" + metadata.offset());
					}
				}
			});
		}

		kafkaProducer.close();

	}

}
