package cn.amaging.encology.util;

import cn.amaging.encology.util.result.Result;
import com.alibaba.fastjson.JSON;

import java.util.Date;


/**
 * Created by DuQiyu on 2018/12/18 15:02.
 */
public class ResultUtil {

    public static String result(String code, String message) {
        return result(code, null, message);
    }

    public static String result(String code, Object data, String message ) {
        Result result = new Result(code, message);
        result.setData(data);
        result.setTimestamp(new Date().getTime());
        return JSON.toJSONString(result);
    }
}
