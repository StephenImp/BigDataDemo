package com.cn.mapreduce.inputformat;

import java.io.IOException;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class SequenceFileMapper extends Mapper<Text, BytesWritable, Text, BytesWritable>{

	@Override
	protected void map(Text key, BytesWritable value, Context context)
			throws IOException, InterruptedException {

		/**
		 * a.txt,	读完后，放在缓冲区中
		 * b.txt,	读完后，放在缓冲区中
		 * c.txt 	都读完后，放在缓冲区中
		 *
		 * 然后同意由Reduce将缓冲区中的数据读走。
		 */
		context.write(key, value);
	}
}
