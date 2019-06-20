import java.util.{Date, UUID}

import commons.conf.ConfigurationManager
import commons.constant.Constants
import commons.model.UserVisitAction
import commons.utils.{DateUtils, ParamUtils, StringUtils}
import net.sf.json.JSONObject
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

/**
  * 聚合信息获取
  */
object SessionStat {

  def main(args: Array[String]): Unit = {

    // 获取筛选条件
    val jsonStr = ConfigurationManager.config.getString(Constants.TASK_PARAMS)
    // 获取筛选条件对应的JsonObject
    val taskParam = JSONObject.fromObject(jsonStr)

    // 创建全局唯一的主键
    val taskUUID = UUID.randomUUID().toString

    // 创建sparkConf
    val sparkConf = new SparkConf().setAppName("session").setMaster("local[*]")

    // 创建sparkSession（包含sparkContext）
    val sparkSession = SparkSession.builder().config(sparkConf).enableHiveSupport().getOrCreate()

    // 获取原始的动作表数据
    // actionRDD: RDD[UserVisitAction]
    val actionRDD = getOriActionRDD(sparkSession, taskParam)

    // sessionId2ActionRDD: RDD[(sessionId, UserVisitAction)]
    val sessionId2ActionRDD = actionRDD.map(item => (item.session_id, item))

    // session2GroupActionRDD: RDD[(sessionId, iterable_UserVisitAction)]
    val session2GroupActionRDD = sessionId2ActionRDD.groupByKey()

    session2GroupActionRDD.cache()

    val userId2AggrInfoRDD = getSessionFullInfo(sparkSession, session2GroupActionRDD)

    userId2AggrInfoRDD.foreach(println(_))
  }

  /**
    * 将统计出来的聚合数据进行聚合
    * @param sparkSession
    * @param session2GroupActionRDD
    * @return
    */
  def getSessionFullInfo(sparkSession: SparkSession,
                         session2GroupActionRDD: RDD[(String, Iterable[UserVisitAction])]) = {

    // userId2AggrInfoRDD: RDD[(userId, aggrInfo)]
    val userId2AggrInfoRDD = session2GroupActionRDD.map{
      case (sessionId, iterableAction) =>
        var userId = -1L

        var startTime:Date = null
        var endTime:Date = null

        var stepLength = 0

        val searchKeywords = new StringBuffer("")
        val clickCategories = new StringBuffer("")

        for(action <- iterableAction){
          if(userId == -1L){
            userId = action.user_id
          }

          val actionTime = DateUtils.parseTime(action.action_time)
          if(startTime == null || startTime.after(actionTime)){
            startTime = actionTime
          }
          if(endTime == null || endTime.before(actionTime)){
            endTime = actionTime
          }

          val searchKeyword = action.search_keyword
          if(StringUtils.isNotEmpty(searchKeyword) && !searchKeywords.toString.contains(searchKeyword)){
            searchKeywords.append(searchKeyword + ",")
          }

          val clickCategoryId = action.click_category_id
          if(clickCategoryId != -1 && !clickCategories.toString.contains(clickCategoryId)){
            clickCategories.append(clickCategoryId + ",")
          }

          stepLength += 1
        }

        // searchKeywords.toString.substring(0, searchKeywords.toString.length)
        val searchKw = StringUtils.trimComma(searchKeywords.toString)
        val clickCg = StringUtils.trimComma(clickCategories.toString)

        val visitLength = (endTime.getTime - startTime.getTime) / 1000

        val aggrInfo = Constants.FIELD_SESSION_ID + "=" + sessionId + "|" +
          Constants.FIELD_SEARCH_KEYWORDS + "=" + searchKw + "|" +
          Constants.FIELD_CLICK_CATEGORY_IDS + "=" + clickCg + "|" +
          Constants.FIELD_VISIT_LENGTH + "=" + visitLength + "|" +
          Constants.FIELD_STEP_LENGTH + "=" + stepLength + "|" +
          Constants.FIELD_START_TIME + "=" + DateUtils.formatTime(startTime)

        /**
          * 返回
          */
        (userId, aggrInfo)
    }

    /**
      * 返回
      */
    userId2AggrInfoRDD
  }


  def getOriActionRDD(sparkSession: SparkSession, taskParam: JSONObject) = {
    val startDate = ParamUtils.getParam(taskParam, Constants.PARAM_START_DATE)
    val endDate = ParamUtils.getParam(taskParam, Constants.PARAM_END_DATE)

    val sql = "select * from user_visit_action where date>='" + startDate + "' and date<='" + endDate + "'"

    import sparkSession.implicits._
    sparkSession.sql(sql).as[UserVisitAction].rdd
  }


}
