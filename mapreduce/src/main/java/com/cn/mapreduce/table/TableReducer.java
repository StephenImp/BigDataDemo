package com.cn.mapreduce.table;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * 这里主要是join
 *
 * ReduceTask  会对  相同的key(Text---pid)  进行排序   这里是默认排序
 *
 * 相同的key(Text---pid) 为一组数据，一定会进入同一个reduce()方法中去
 */
public class TableReducer extends Reducer<Text, TableBean, TableBean, NullWritable>{


	/**
	 *
	 * @param key	(Text---pid)
	 * @param values	TableBean
	 * @param context
	 * @throws IOException
	 * @throws InterruptedException
	 *
	 * pid(key)
	 * 01			1001		1		order
	 * 01			1001		1		order
	 * 01			小米		 		pd
	 *
	 *
	 * 02			1002		2		order
	 * 02			1002		2		order
	 * 02			华为				pd
	 *
	 *
	 * 03			1003		3		order
	 * 03			1003		3		order
	 * 03			格力				pd
	 *
	 *
	 * 相同的key(Text---pid) 为一组数据，一定会进入同一个reduce()方法中去
	 */
	@Override
	protected void reduce(Text key, Iterable<TableBean> values,
			Context context) throws IOException, InterruptedException {
		
		// 存储所有订单集合
		ArrayList<TableBean> orderBeans = new ArrayList<>();
		// 存储产品信息
		TableBean pdBean = new TableBean();
		
		for (TableBean tableBean : values) {
			
			if ("order".equals(tableBean.getFlag())) {// 订单表
				
				TableBean tmpBean = new TableBean();
				
				try {
					BeanUtils.copyProperties(tmpBean, tableBean);
					
					orderBeans.add(tmpBean);
					
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}else {//产品表
				try {
					BeanUtils.copyProperties(pdBean, tableBean);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}


		/**
		 * 遍历每一个订单表，为产品名称进行赋值
		 *
		 * 因为相同的key(Text---pid) 为一组数据，才会进入同一个reduce()方法中去
		 *
		 * 所以pid一定相同，所以同一个key的话，产品名称一定相同
		 *
		 *
		 * 注意输出的格式与  Bean的 toString() 方法有关
		 */
		for (TableBean tableBean : orderBeans) {
			tableBean.setPname(pdBean.getPname());
			
			context.write(tableBean, NullWritable.get());
		}
	}
}
