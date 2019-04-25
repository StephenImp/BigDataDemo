package com.cn.etl;

/**
 * Created by MOZi on 2019/4/2.
 *
 * 1.过滤脏数据
 * 2.替换类别字段中的空格
 * 3.替换关联资源中的分隔符
 */
public class ETLUtil {

    /**
     * 将所有的类别用“&”分割，同时去掉两边空格，多个相关视频id也使用“&”进行分割。
     * @param ori
     * @return
     */
    public static String oriString2ETLString(String ori){

        StringBuilder etlString = new StringBuilder();

        //切割数据
        String[] splits = ori.split("\t");

        //小于9个字段，为脏数据，忽略
        if(splits.length < 9){
            return null;
        }

        //去除 视频类别 中 的空格
        splits[3] = splits[3].replace(" ", "");


        //这里  每一行数据中 每个字段  都是用  空格来区分的了
        for(int i = 0; i < splits.length; i++){

            //等于 9个字段  最后一个不要空格
            if(i < 9){
                if(i == splits.length - 1){
                    etlString.append(splits[i]);
                }else{
                    etlString.append(splits[i] + "\t");
                }
            }else{//大于9个字段，最后的 相关视频id 集合 用 & 拼接

                if(i == splits.length - 1){
                    etlString.append(splits[i]);
                }else{
                    etlString.append(splits[i] + "&");
                }
            }
        }

        return etlString.toString();
    }

}
