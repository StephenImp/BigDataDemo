package com.cn.Case.weibo;

/**
 * Created by MOZi on 2019/4/10.
 */
public class TestWeiBo {

    public static void main(String[] args) {

        WeiBo weibo = new WeiBo();
        weibo.initNamespace();

        WeiBoUtils weiBoUtils = new WeiBoUtils();

        //发布微博内容
        weiBoUtils.testPublishContent(weibo);

        //添加关注
        weiBoUtils.testAddAttend(weibo);

        //展示内容
        weiBoUtils.testShowMessage(weibo);

        //取关
        weiBoUtils.testRemoveAttend(weibo);

        //展示内容
        weiBoUtils.testShowMessage(weibo);
    }
}
