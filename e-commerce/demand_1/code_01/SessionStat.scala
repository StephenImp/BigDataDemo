import java.util.UUID

import commons.conf.ConfigurationManager
import commons.constant.Constants
import commons.model.UserVisitAction
import commons.utils.ParamUtils
import net.sf.json.JSONObject
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object SessionStat {

  def main(args: Array[String]): Unit = {

    // 获取筛选条件(这里就获取了json窜)
    val jsonStr = ConfigurationManager.config.getString(Constants.TASK_PARAMS)
    // 获取筛选条件对应的JsonObject
    val taskParam = JSONObject.fromObject(jsonStr)

    // 创建全局唯一的主键(同一个job的主键是相同的)
    val taskUUID = UUID.randomUUID().toString

    // 创建sparkConf
    val sparkConf = new SparkConf().setAppName("session").setMaster("local[*]")

    // 创建sparkSession（包含sparkContext）
    val sparkSession = SparkSession.builder().config(sparkConf)
      .enableHiveSupport()//因为要操作Hive,所以这里需要hive的支持
      .getOrCreate()

    // 获取原始的动作表数据(通过sql就直接从hive中把数据查出来了),这里就转化成RDD了。
    // actionRDD   =>   RDD[UserVisitAction]
    val actionRDD = getOriActionRDD(sparkSession, taskParam)

    /**
      * 将RDD转换从tuple(K,V)结构,key是sessionId,值就是整个对象了
      */
    // sessionId2ActionRDD: RDD[(sessionId, UserVisitAction)]
    val sessionId2ActionRDD = actionRDD.map(item => (item.session_id, item))

    /**
      * 把同一个session的action  全部聚合在一起
      */
    // session2GroupActionRDD: RDD[(sessionId, iterable_UserVisitAction)]
    val session2GroupActionRDD = sessionId2ActionRDD.groupByKey()

    session2GroupActionRDD.cache()

    session2GroupActionRDD.foreach(println(_))
  }

  def getOriActionRDD(sparkSession: SparkSession, taskParam: JSONObject) = {

    //这里感觉有点像写JDBC的感觉

    //获取指定参数
    val startDate = ParamUtils.getParam(taskParam, Constants.PARAM_START_DATE)
    val endDate = ParamUtils.getParam(taskParam, Constants.PARAM_END_DATE)

    val sql = "select * from user_visit_action where date>='" + startDate + "' and date<='" + endDate + "'"

    //将Row 类型的 dateSet  转化成   实体类类型的 dateSet?
    import sparkSession.implicits._  //隐私转换
    sparkSession.sql(sql).as[UserVisitAction].rdd
  }


}
