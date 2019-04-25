package com.cn.Case.weibo;

import java.util.List;

/**
 * Created by MOZi on 2019/4/10.
 */
public class WeiBoUtils {

    /**
     * 发布微博内容
     */
    public void testPublishContent(WeiBo wb){
        wb.publishContent("0001", "今天买了一包空气，送了点薯片，非常开心！！");
        wb.publishContent("0001", "今天天气不错。");
    }

    /**
     * 添加关注
     * @param wb
     */
    public void testAddAttend(WeiBo wb){
        wb.publishContent("0008", "准备下课！");
        wb.publishContent("0009", "准备关机！");
        wb.addAttends("0001", "0008", "0009");
    }

    /**
     * 取消关注
     * @param wb
     */
    public void testRemoveAttend(WeiBo wb){
        wb.removeAttends("0001", "0008");
    }

    /**
     * 展示内容
     * @param wb
     */
    public void testShowMessage(WeiBo wb){
        List<Message> messages = wb.getAttendsContent("0001");
        for(Message message : messages){
            System.out.println(message);
        }
    }
}
