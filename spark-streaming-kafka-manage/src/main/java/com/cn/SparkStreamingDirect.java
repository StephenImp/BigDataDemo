package com.cn;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryUntilElapsed;
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
import org.apache.spark.streaming.kafka.HasOffsetRanges;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.apache.spark.streaming.kafka.OffsetRange;

import com.fasterxml.jackson.databind.ObjectMapper;

import kafka.common.TopicAndPartition;
import kafka.message.MessageAndMetadata;
import kafka.serializer.StringDecoder;
import scala.Tuple2;

public class SparkStreamingDirect {

	public static JavaStreamingContext getStreamingContext(Map<TopicAndPartition, Long> topicOffsets,String groupID){

		SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("SparkStreamingOnKafkaDirect");
		conf.set("spark.streaming.kafka.maxRatePerPartition", "10");
        JavaStreamingContext jsc = new JavaStreamingContext(conf, Durations.seconds(5));
//        jsc.checkpoint("/checkpoint");
        Map<String, String> kafkaParams = new HashMap<String, String>();
        kafkaParams.put("metadata.broker.list","node1:9092,node2:9092,node3:9092");
//        kafkaParams.put("group.id","MyFirstConsumerGroup");

        for(Map.Entry<TopicAndPartition,Long> entry:topicOffsets.entrySet()){
    		System.out.println(entry.getKey().topic()+"\t"+entry.getKey().partition()+"\t"+entry.getValue());
        }

        JavaInputDStream<String> message = KafkaUtils.createDirectStream(
			jsc,
	        String.class,
	        String.class, 
	        StringDecoder.class,
	        StringDecoder.class, 
	        String.class,
	        kafkaParams,
	        topicOffsets, 
	        new Function<MessageAndMetadata<String,String>,String>() {
	            /**
				 * 
				 */
				private static final long serialVersionUID = 1L;
	
				public String call(MessageAndMetadata<String, String> v1)throws Exception {
	                return v1.message();
	            }
	        }
		);

        final AtomicReference<OffsetRange[]> offsetRanges = new AtomicReference<>();

        JavaDStream<String> lines = message.transform(new Function<JavaRDD<String>, JavaRDD<String>>() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public JavaRDD<String> call(JavaRDD<String> rdd) throws Exception {
              OffsetRange[] offsets = ((HasOffsetRanges) rdd.rdd()).offsetRanges();
              offsetRanges.set(offsets);
              return rdd;
            }
          }
        );

        message.foreachRDD(new VoidFunction<JavaRDD<String>>(){
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public void call(JavaRDD<String> t) throws Exception {

                ObjectMapper objectMapper = new ObjectMapper();

                CuratorFramework  curatorFramework = CuratorFrameworkFactory.builder()
                        .connectString("node3:2181,node4:2181,node5:2181").connectionTimeoutMs(1000)
                        .sessionTimeoutMs(10000).retryPolicy(new RetryUntilElapsed(1000, 1000)).build();

                curatorFramework.start();

                for (OffsetRange offsetRange : offsetRanges.get()) {
                	long fromOffset = offsetRange.fromOffset();
                	long untilOffset = offsetRange.untilOffset();
                	final byte[] offsetBytes = objectMapper.writeValueAsBytes(offsetRange.untilOffset());
                    String nodePath = "/consumers/"+groupID+"/offsets/" + offsetRange.topic()+ "/" + offsetRange.partition();
                    System.out.println("nodePath = "+nodePath);
                    System.out.println("fromOffset = "+fromOffset+",untilOffset="+untilOffset);
                    if(curatorFramework.checkExists().forPath(nodePath)!=null){
                        curatorFramework.setData().forPath(nodePath,offsetBytes);
                    }else{
                        curatorFramework.create().creatingParentsIfNeeded().forPath(nodePath, offsetBytes);
                    }
                }

                curatorFramework.close();
            }

        });

        lines.print();

        return jsc;
    }
}
