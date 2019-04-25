package com.cn.etl;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 * Created by MOZi on 2019/4/2.
 */
public class VideoETLRunner implements Tool {

    private Configuration conf = null;

    @Override
    public void setConf(Configuration conf) {
        this.conf = conf;
    }

    @Override
    public Configuration getConf() {
        return this.conf;
    }

    /**
     * mapper 最终调用 run 方法来执行的。
     * @param args
     * @return
     * @throws Exception
     */
    @Override
    public int run(String[] args) throws Exception {


        conf = this.getConf();
        conf.set("inpath", args[0]);
        conf.set("outpath", args[1]);

        //1.获取job对象。
        Job job = Job.getInstance(conf);

        //2.封装driver类
        job.setJarByClass(VideoETLRunner.class);

        //3.关联Mapper
        job.setMapperClass(VideoETLMapper.class);


        //5.Mapper 输出KV类型
        job.setMapOutputKeyClass(NullWritable.class);
        job.setMapOutputValueClass(Text.class);

        job.setNumReduceTasks(0);

        //输入输出路径
        this.initJobInputPath(job);
        this.initJobOutputPath(job);

        //提交任务
        return job.waitForCompletion(true) ? 0 : 1;
    }


    private void initJobOutputPath(Job job) throws IOException {
        Configuration conf = job.getConfiguration();
        String outPathString = conf.get("outpath");

        FileSystem fs = FileSystem.get(conf);

        Path outPath = new Path(outPathString);
        if(fs.exists(outPath)){
            fs.delete(outPath, true);
        }

        FileOutputFormat.setOutputPath(job, outPath);

    }


    private void initJobInputPath(Job job) throws IOException {

        Configuration conf = job.getConfiguration();
        String inPathString = conf.get("inpath");

        FileSystem fs = FileSystem.get(conf);

        Path inPath = new Path(inPathString);
        if(fs.exists(inPath)){
            FileInputFormat.addInputPath(job, inPath);
        }else{
            throw new RuntimeException("HDFS中该文件目录不存在：" + inPathString);
        }
    }

    public static void main(String[] args) {

        try {
            int resultCode = ToolRunner.run(new VideoETLRunner(), args);
            if(resultCode == 0){
                System.out.println("Success!");
            }else{
                System.out.println("Fail!");
            }
            System.exit(resultCode);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
