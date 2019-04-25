package com.cn.mapreduce.inputformat;

import java.io.IOException;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class SequenceFileReducer extends Reducer<Text, BytesWritable, Text, BytesWritable>{

	@Override
	protected void reduce(Text key, Iterable<BytesWritable> values,
			Context context) throws IOException, InterruptedException {

		/**
		 * 这个key 和 value啊，都在WholeRecordReader 中定义好了。
		 */
		// 循环写出
		for (BytesWritable value : values) {
			context.write(key, value);
		}
	}
}
