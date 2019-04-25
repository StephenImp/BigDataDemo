package com.cn.mapreduce.mr1;

import java.io.IOException;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * Created by MOZi on 2019/4/9.
 *
 * 用于读取fruit表中的数据
 *
 * 这里只需要提供  KEYOUT, VALUEOUT
 */
public class ReadFruitMapper extends TableMapper<ImmutableBytesWritable, Put> {

    /**
     *
     * @param key   rowKey
     * @param value
     * @param context
     * @throws IOException
     * @throws InterruptedException
     *
     * 一个rowKey对应一个Result
     *
     * Result 下 每一个 version 对应着一个cell
     */
    @Override
    protected void map(ImmutableBytesWritable key, Result value, Context context)
            throws IOException, InterruptedException {

        //将fruit的name和color提取出来，相当于将每一行数据读取出来放入到Put对象中。
        Put put = new Put(key.get());
        //遍历添加column行
        for(Cell cell: value.rawCells()){
            //添加/克隆列族:info
            if("info".equals(Bytes.toString(CellUtil.cloneFamily(cell)))){
                //添加/克隆列：name   Qualifier列名
                if("name".equals(Bytes.toString(CellUtil.cloneQualifier(cell)))){
                    //将该列cell加入到put对象中
                    put.add(cell);
                    //添加/克隆列:color
                }else if("color".equals(Bytes.toString(CellUtil.cloneQualifier(cell)))){
                    //向该列cell加入到put对象中
                    put.add(cell);
                }
            }
        }
        //将从fruit读取到的每行数据写入到context中作为map的输出
        context.write(key, put);
    }
}
