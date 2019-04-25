package com.cn.mapreduce.wordcount;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * Combiner是在每一个MapTask所在的节点运行
 * Reduce是接收全局所有的Mapper的输出结果
 *
 * Combiner属于MapTask工作机制
 */
public class WordcountCombiner extends Reducer<Text, IntWritable, Text	, IntWritable>{
	
	IntWritable v = new IntWritable();

	@Override
	protected void reduce(Text key, Iterable<IntWritable> values,
			Context context) throws IOException, InterruptedException {
		
		int sum = 0;
		// 1 累加求和
		for (IntWritable value : values) {
			sum += value.get();
		}
	
		v.set(sum);
		
		// 2 写出
		context.write(key, v);
	}
}
