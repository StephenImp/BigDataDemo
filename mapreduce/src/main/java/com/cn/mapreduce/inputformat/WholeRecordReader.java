package com.cn.mapreduce.inputformat;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

/**
 * WholeRecordReader类对象
 *
 * 假如一个切片中有三个文件(a.txt,b.txt,c.txt)
 * 读a.txt 的时候	new一个WholeRecordReader类对象
 * 读b.txt 的时候	再new一个WholeRecordReader类对象
 * c同理
 *
 *
 *
 * 在Mapper的源码中
 * 		run()方法，
 * 		三个文件，三个MapTask，读取三个key(文件路径)
 * 		结合这里。当文件都读完时（及key都读完）
 * 		那么就需要return false 来跳出循环。（nextKeyValue()）
 *
 */
public class WholeRecordReader extends RecordReader<Text, BytesWritable>{

	FileSplit split;
	Configuration configuration;

	Text k = new Text();//文件的路径+名称
	BytesWritable v = new BytesWritable();//文件内容

	/**
	 * 标记
	 */
	boolean isProgress = true;

	/**
	 * 初始化
	 * @param split		切片
	 * @param context	上下文
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Override
	public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
		// 初始化
		this.split = (FileSplit) split;
		
		configuration = context.getConfiguration();
	}

	/**
	 * 核心业务逻辑处理
	 *
	 * 将多个文件合并成一个文件
	 *
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException {
		// 核心业务逻辑处理
		
		if (isProgress) {

			/**
			 * 缓冲区的大小为切片的大小
			 */
			byte[] buf = new byte[(int) split.getLength()];
			
			// 1 获取fs对象(fs对象可以用来获取切片信息)
				//切片大小，决定开几个mapTask
			Path path = split.getPath();
			FileSystem fs = path.getFileSystem(configuration);
			
			// 2 获取输入流
			FSDataInputStream fis = fs.open(path);
			
			// 3 拷贝
			/**
			 * 把输入流拷贝到一个缓冲区中
			 */
			IOUtils.readFully(fis, buf, 0, buf.length);

			/**
			 * 将buf中的内容写到v中去
			 */
			// 4 封装v
			v.set(buf, 0, buf.length);
			
			// 5 封装k
			k.set(path.toString());
			
			// 6 关闭资源
			IOUtils.closeStream(fis);

			/**
			 * 每读一个切片中的文件时，都会new一个WholeRecordReader类对象
			 * 当切片中的文件都读完时，不会去newWholeRecordReader类对象
			 * 所以isProgress = false;不会继续变成true
			 *
			 * 不进循环就return	false了。
			 */
			isProgress = false;
			
			return true;
		}
		
		return false;
	}

	/**
	 * 获取当前的key(Text)
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Override
	public Text getCurrentKey() throws IOException, InterruptedException {
		return k;
	}

	/**
	 * 获取当前的值
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Override
	public BytesWritable getCurrentValue() throws IOException, InterruptedException {
		return v;
	}

	/**
	 * 获取正在处理的进程(进度条)
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Override
	public float getProgress() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
	}

}
