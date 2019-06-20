import java.util.UUID

import commons.conf.ConfigurationManager
import commons.constant.Constants
import commons.model.UserVisitAction
import commons.utils.ParamUtils
import net.sf.json.JSONObject
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object PageConvertStat {

  def main(args: Array[String]): Unit = {

    // 获取任务限制条件
    val jsonStr = ConfigurationManager.config.getString(Constants.TASK_PARAMS)
    val taskParam = JSONObject.fromObject(jsonStr)

    // 获取唯一主键
    val taskUUID = UUID.randomUUID().toString

    // 创建sparkConf
    val sparkConf = new SparkConf().setAppName("pageConvert").setMaster("local[*]")

    // 创建sparkSession
    val sparkSession = SparkSession.builder().config(sparkConf).enableHiveSupport().getOrCreate()

    // 获取用户行为数据
    val sessionId2ActionRDD = getUserVisitAction(sparkSession, taskParam)

    // pageFlowStr: "1,2,3,4,5,6,7"
    val pageFlowStr = ParamUtils.getParam(taskParam, Constants.PARAM_TARGET_PAGE_FLOW)
    // pageFlowArray: Array[Long]  [1,2,3,4,5,6,7]
    val pageFlowArray = pageFlowStr.split(",")
    // pageFlowArray.slice(0, pageFlowArray.length - 1): [1,2,3,4,5,6]
    // pageFlowArray.tail: [2,3,4,5,6,7]
    // pageFlowArray.slice(0, pageFlowArray.length - 1).zip(pageFlowArray.tail): [(1,2), (2,3) , ..]
    // targetPageSplit: [1_2, 2_3, 3_4, ...]
    val targetPageSplit = pageFlowArray.slice(0, pageFlowArray.length - 1).zip(pageFlowArray.tail).map{
      case (page1, page2) => page1 + "_" + page2
    }

  }

  def getUserVisitAction(sparkSession: SparkSession, taskParam: JSONObject) = {
    val startDate = ParamUtils.getParam(taskParam, Constants.PARAM_START_DATE)
    val endDate = ParamUtils.getParam(taskParam, Constants.PARAM_END_DATE)

    val sql = "select * from user_visit_action where date>='" + startDate + "' and date<='" +
      endDate + "'"

    import sparkSession.implicits._
    sparkSession.sql(sql).as[UserVisitAction].rdd.map(item => (item.session_id, item))
  }

}
