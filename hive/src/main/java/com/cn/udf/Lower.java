package com.cn.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

/**
 * Created by MOZi on 2019/4/1.
 */
public class Lower extends UDF {

    public String evaluate (final String s) {

        if (s == null) {
            return null;
        }

        return s.toLowerCase();
    }
}
