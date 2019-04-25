package com.cn.mapreduce.wordcount;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

// map阶段
// Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT>
// KEYIN 输入数据的key 
// VALUEIN 输入数据的value
// KEYOUT 输出数据的key的类型   atguigu,1   ss,1
// VALUEOUT 输出的数据的value类型
/**
 *  LongWritable 	行的偏移量。	eg: 文件第一行 偏移量 0-19	第二行的偏移量  20-39
 *  Text			输入数据的key
 *  Text			输出数据的key的类型
 *  IntWritable		输出的数据的value类型
 */
public class WordcountMapper extends Mapper<LongWritable, Text, Text, IntWritable>{

	/**
	 * 自定义 k,v
	 */
	Text k = new Text();
	IntWritable v = new IntWritable(1);

	/**
	 *
	 * @param key	行的偏移量
	 * @param value		每一行的内容
	 * @param context
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Override
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {

//		atguigu atguigu

		System.out.println(key.toString());
		
		// 1 获取一行
		String line = value.toString();
		
		// 2 切割单词
		String[] words = line.split(" ");
		
		// 3 循环写出
		for (String word : words) {
			// atguigu
			k.set(word);
			context.write(k, v);//写到缓冲池?
		}
	}
}
