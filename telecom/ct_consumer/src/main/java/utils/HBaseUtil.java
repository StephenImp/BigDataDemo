package utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeSet;

public class HBaseUtil {
    /**
     * 判断表是否存在
     * @param conf HBaseConfiguration
     * @param tableName
     * @return
     */
    public static boolean isExistTable(Configuration conf, String tableName) throws IOException {
        Connection connection = ConnectionFactory.createConnection(conf);
        Admin admin = connection.getAdmin();
        boolean result = admin.tableExists(TableName.valueOf(tableName));

        admin.close();
        connection.close();

        return result;
    }

    /**
     * 初始化命名空间
     * @param conf
     * @param namespace
     */
    public static void initNamespace(Configuration conf, String namespace) throws IOException {
        Connection connection = ConnectionFactory.createConnection(conf);
        Admin admin = connection.getAdmin();

        NamespaceDescriptor nd = NamespaceDescriptor
                .create(namespace)
                .addConfiguration("CREATE_TIME", String.valueOf(System.currentTimeMillis()))//创建时间
                .addConfiguration("AUTHOR", "wpw")//作者
                .build();

        admin.createNamespace(nd);

        admin.close();
        connection.close();
    }

    /**
     * 创建表
     * @param conf
     * @param tableName
     * @param columnFamily
     * @throws IOException
     */
    public static void createTable(Configuration conf, String tableName, int regions, String... columnFamily) throws IOException {
        Connection connection = ConnectionFactory.createConnection(conf);
        Admin admin = connection.getAdmin();

        if(isExistTable(conf, tableName)) return;

        HTableDescriptor htd = new HTableDescriptor(TableName.valueOf(tableName));

        //添加列族
        for(String cf: columnFamily){
            htd.addFamily(new HColumnDescriptor(cf));
        }
        htd.addCoprocessor("hbase.CalleeWriteObserver");

        admin.createTable(htd, genSplitKeys(regions));

        admin.close();
        connection.close();
    }

    /**
     * 自定义分区键(regionCode) --- 预分区
     * rowKey --- regionCode_call1_buildTime_call2_flag_duration
     *
     *
     * xxxx % 3    那么结果一定是0-2
     *
     * 预留一个分区---存放错误数据，或者防止出现异常情况。。。
     *
     * @param regions
     * @return
     */
    private static byte[][] genSplitKeys(int regions){

        //定义一个存放分区键的数组(根据经验预测需要几个分区)
        //一个分区的数据量(1-10G)
        String[] keys = new String[regions];
        //目前推算，region个数不会超过2位数，所以region分区键格式化为两位数字所代表的字符串

        //生成预分区号。
        DecimalFormat df = new DecimalFormat("00");
        for(int i = 0; i < regions; i ++){
            keys[i] = df.format(i) + "|";
        }

        byte[][] splitKeys = new byte[regions][];

        //生成byte[][]类型的分区键的时候，一定要保证分区键是有序的 ***
        TreeSet<byte[]> treeSet = new TreeSet<byte[]>(Bytes.BYTES_COMPARATOR);
        for(int i = 0; i < regions; i++){
            treeSet.add(Bytes.toBytes(keys[i]));
        }

        Iterator<byte[]> splitKeysIterator = treeSet.iterator();
        int index = 0;
        while(splitKeysIterator.hasNext()){
            byte[] b = splitKeysIterator.next();
            splitKeys[index ++] = b;
        }
        return splitKeys;
    }

    /**
     * 生成rowkey
     * regionCode_call1_buildTime_call2_flag_duration(持续时间)
     * @return
     */
    public static String genRowKey(String regionCode, String call1, String buildTime, String call2, String flag, String duration){
        StringBuilder sb = new StringBuilder();
        sb.append(regionCode + "_")
                .append(call1 + "_")
                .append(buildTime + "_")
                .append(call2 + "_")
                .append(flag + "_")
                .append(duration);
        return sb.toString();
    }

    /**
     * 手机号：15837312345
     * 通话建立时间：2017-01-10 11:20:30 -> 20170110112030
     *
     * 为什么要加手机号啊？还是没明白，根据日期不就行了吗
     * 根据startRow和stopRow 能查询出，某个人，某个时间段之内的话单
     *
     * @param call1
     * @param buildTime
     * @param regions
     * @return
     */
    public static String genRegionCode(String call1, String buildTime, int regions){
        int len = call1.length();
        //取出后4位号码
        String lastPhone = call1.substring(len - 4);
        //取出年月
        String ym = buildTime
                .replaceAll("-", "")
                .replaceAll(":", "")
                .replaceAll(" ", "")
                .substring(0, 6);

        //离散操作1
        Integer x = Integer.valueOf(lastPhone) ^ Integer.valueOf(ym);


        //这里是举例子，交换两个基本数据类型的变量的内容
//        int a = 10;
//        int b = 20;
//        a = a ^ b;
//        b = a ^ b;
//        a = a ^ b;

        //离散操作2
        int y = x.hashCode();

        //生成分区号
        int regionCode = y % regions;


        //格式化分区号
        DecimalFormat df = new DecimalFormat("00");
        return  df.format(regionCode);
    }

}
