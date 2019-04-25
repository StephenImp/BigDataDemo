package com.cn.etl;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by MOZi on 2019/4/2.
 */
public class VideoETLMapper extends Mapper<Object, Text, NullWritable, Text> {


    private Text text = new Text();

    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {

        //1.获取一行数据，
        String line = value.toString();

        //2.清洗数据
        String etlString = ETLUtil.oriString2ETLString(line);

        //由于 字段 数 < 9 为脏数据， 直接 返回null. 这里要处理一下
        if(StringUtils.isBlank(etlString)){
            return;
        }

        text.set(etlString);

        //写出
        context.write(NullWritable.get(), text);

    }
}
