import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import utils.HBaseUtil;

/**
 * 模拟查询
 */
public class ScanContentTest {
    String startTime = "2017-01-01";
    String stopTime = "2017-03-01";

    //15837312345(主叫) 13737399999(被叫)
    //所以一条记录需要生成两条数据。
    //浪费了空间，但是换取了效率。
    public static void main(String[] args) {
        Scan scan = new Scan();

        //regionCode_15837312345_20170110152430_13737399999_1_0360
        //regionCode_13737399999_20170110152430_15837312345_0_0360
        String regionCodeCaller = HBaseUtil.genRegionCode("15837312345", "201701", 6);
        String regionCodeCallee = HBaseUtil.genRegionCode("13737399999", "201701", 6);

        //根据web页面，传入的查询条件,设置扫描区间。
        //每一个region维护着startRow与endRowKey
        scan.setStartRow(Bytes.toBytes(regionCodeCaller + "_15837312345_201701"));
        scan.setStopRow(Bytes.toBytes(regionCodeCaller + "_15837312345_201702"));

        //多个分区，传入多个scan对象。得到多个集合。
    }
}
