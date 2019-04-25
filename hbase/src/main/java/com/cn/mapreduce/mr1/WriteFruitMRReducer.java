package com.cn.mapreduce.mr1;

import java.io.IOException;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.io.NullWritable;

/**
 * Created by MOZi on 2019/4/9.
 *
 * 用于将读取到的fruit表中的数据写入到fruit_mr表中
 *
 * valueout  指定类型为  Mutation
 *
 * Put是实现类之一，所以在Mapper端指定valueout 为 Put类型
 *
 * Put 存放的是 一个 rowKey 下的所有 cell
 */
public class WriteFruitMRReducer extends TableReducer<ImmutableBytesWritable, Put, NullWritable> {

    @Override
    protected void reduce(ImmutableBytesWritable key, Iterable<Put> values, Context context)
            throws IOException, InterruptedException {

        //读出来的每一行数据写入到fruit_mr表中
        for(Put put: values){
            context.write(NullWritable.get(), put);
        }
    }
}
