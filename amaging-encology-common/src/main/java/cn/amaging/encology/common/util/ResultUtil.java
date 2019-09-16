package cn.amaging.encology.common.util;

import cn.amaging.encology.common.util.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * Created by DuQiyu on 2018/12/18 15:02.
 */
public class ResultUtil {

    private static final Logger logger = LoggerFactory.getLogger(FtpUtil.class);

    public static String result(String code, String message) {
        return result(code, null, message);
    }

    public static String result(String code, Object data, String message ) {
        Result result = new Result(code, message);
        result.setData(data);
        result.setTimestamp(new Date().getTime());
        return JsonUtil.toJsonString(result);
    }
}
