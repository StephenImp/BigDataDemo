package com.cn.hdfs;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.junit.Test;

public class HDFSClient {

	public static void main(String[] args) throws IOException, Exception, URISyntaxException {
		
		Configuration conf = new Configuration();
//		conf.set("fs.defaultFS", "hdfs://hadoop102:9000");
		
		// 1 获取hdfs客户端对象
//		FileSystem fs = FileSystem.get(conf );
		FileSystem fs = FileSystem.get(new URI("hdfs://hadoop102:9000"), conf, "atguigu");
		
		
		// 2 在hdfs上创建路径
		fs.mkdirs(new Path("/0529/dashen/banzhang"));
		
		// 3 关闭资源
		fs.close();
		
		System.out.println("over");
	}
	
	// 1 文件上传
	@Test
	public void testCopyFromLocalFile() throws IOException, InterruptedException, URISyntaxException{
		
		// 1 获取fs对象
		Configuration conf = new Configuration();
		conf.set("dfs.replication", "2");
		FileSystem fs = FileSystem.get(new URI("hdfs://hadoop102:9000"), conf , "atguigu");

		/**
		 * 第一个路径，需要上传文件的路径
		 * 第二个路径，上传文件到哪里
		 */
		// 2 执行上传API
		fs.copyFromLocalFile(new Path("e:/banzhang.txt"), new Path("/xiaohua.txt"));
		
		// 3 关闭资源
		fs.close();
	}
	
	// 2 文件下载
	@Test
	public void testCopyToLocalFile() throws IOException, InterruptedException, URISyntaxException{
		
		// 1 获取对象
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(new URI("hdfs://hadoop102:9000"), conf , "atguigu");
		
		// 2 执行下载操作
//		fs.copyToLocalFile(new Path("/banhua.txt"), new Path("e:/banhua.txt"));

		/**
		 * 第一个参数，是否删除原数据
		 *
		 * 第四个参数，是否产生crc校验，crc校验是什么？ture是不校验  默认false 好像是，用的时候百度下
		 * 校验数据的可靠性
		 */
		fs.copyToLocalFile(false, new Path("/banhua.txt"), new Path("e:/xiaohua.txt"), true);
		
		// 3 关闭资源
		fs.close();
	}
	
	
	// 3 文件删除
	@Test
	public void testDelete() throws IOException, InterruptedException, URISyntaxException{
		
		// 1 获取对象
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(new URI("hdfs://hadoop102:9000"), conf , "atguigu");


		/**
		 *
		 * 如果需要删除的文件是目录的话，则需要递归删除
		 * true 代表 递归删除
		 */
		// 2 文件删除
		fs.delete(new Path("/0529"), true);
		
		// 3 关闭资源
		fs.close();
	}
	
	// 4 文件更名
	@Test
	public void testRename() throws IOException, InterruptedException, URISyntaxException{
		
		// 1 获取对象
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(new URI("hdfs://hadoop102:9000"), conf , "atguigu");
		
		// 2 执行更名操作
		fs.rename(new Path("/banzhang.txt"), new Path("/yanjing.txt"));
		
		// 3 关闭资源
		fs.close();
	}
	
	// 5 文件详情查看
	@Test
	public void testListFiles() throws IOException, InterruptedException, URISyntaxException{
		
		// 1 获取对象
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(new URI("hdfs://hadoop102:9000"), conf , "atguigu");

		/**
		 * 迭代查找文件信息
		 */
		// 2 查看文件详情
		RemoteIterator<LocatedFileStatus> listFiles = fs.listFiles(new Path("/"), true);
		
		while(listFiles.hasNext()){
			/**
			 * 文件的状态信息
			 */
			LocatedFileStatus fileStatus = listFiles.next();
			
			// 查看文件名称、权限、长度、块信息
			System.out.println(fileStatus.getPath().getName());// 文件名称
			System.out.println(fileStatus.getPermission());// 文件权限
			System.out.println(fileStatus.getLen());// 文件长度

			/**
			 * 文件位子，多个是因为存在多个副本
			 */
			BlockLocation[] blockLocations = fileStatus.getBlockLocations();
			
			for (BlockLocation blockLocation : blockLocations) {

				/**
				 * 这个副本存在哪一个主机上
				 */
				String[] hosts = blockLocation.getHosts();
				
				for (String host : hosts) {
					System.out.println(host);
				}
			}
			
			System.out.println("------班长分割线--------");
		}
		
		// 3 关闭资源
		fs.close();
	}
	
	
	// 6 判断是文件还是文件夹
	@Test
	public void testListStatus() throws IOException, InterruptedException, URISyntaxException{
		
		// 1 获取对象
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(new URI("hdfs://hadoop102:9000"), conf , "atguigu");
		
		// 2 判断操作
		FileStatus[] listStatus = fs.listStatus(new Path("/"));
		
		for (FileStatus fileStatus : listStatus) {
			
			if (fileStatus.isFile()) {
				// 文件
				System.out.println("f:"+fileStatus.getPath().getName());
			}else{
				// 文件夹
				System.out.println("d:"+fileStatus.getPath().getName());
			}
		}
		
		// 3 关闭资源
		fs.close();
	}

}
