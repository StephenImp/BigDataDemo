package com.cn.mapreduce.order;

import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

public class OrderReducer extends Reducer<OrderBean, NullWritable, OrderBean, NullWritable> {

	@Override
	protected void reduce(OrderBean key, Iterable<NullWritable> values, Context context)
			throws IOException, InterruptedException {

		/**
		 * 这里遍历之后，可以实现自己的逻辑
		 *
		 * 要前3名的，或者是什么的。
		 */
//		for (NullWritable nullWritable : values) {
//			context.write(key, NullWritable.get());
//		}


		context.write(key, NullWritable.get());

	}

}
