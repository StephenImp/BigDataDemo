package com.cn.mapreduce.inputformat;

import java.io.IOException;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

/**
 * FileInputFormat<K, V>
 *     K：文件的路径和名称
 *     V: 文件
 */
public class WholeFileInputformat extends FileInputFormat<Text, BytesWritable>{


	@Override
	public RecordReader<Text, BytesWritable> createRecordReader(InputSplit split, TaskAttemptContext context)
			throws IOException, InterruptedException {

		/**
		 * 自定义RecordReader并初始化
		 */
		WholeRecordReader recordReader = new WholeRecordReader();
		recordReader.initialize(split, context);
		
		return recordReader;
	}

}
