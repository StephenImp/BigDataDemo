package com.cn.Case.weibo;

/**
 * Created by MOZi on 2019/4/10.
 */
public class TestWeiBo {

    public static void main(String[] args) {

        WeiBo weibo = new WeiBo();
        weibo.initNamespace();

        WeiBoUtils weiBoUtils = new WeiBoUtils();

        weiBoUtils.testPublishContent(weibo);
        weiBoUtils.testAddAttend(weibo);
        weiBoUtils.testShowMessage(weibo);
        weiBoUtils.testRemoveAttend(weibo);
        weiBoUtils.testShowMessage(weibo);
    }
}
