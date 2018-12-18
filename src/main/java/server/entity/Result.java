package server.entity;

import java.util.HashMap;
import java.util.Map;

import static server.servlet.iface.Mservlet.RESULT_CODE.*;

/**
 * Created by user on 2017/11/29.
 */
public class Result<T extends Result> {

    private static Map<Integer,String> errorMap = new HashMap<>();

    static {
        errorMap.put(UNKNOWN,"未知错误");
        errorMap.put(SUCCESS,"操作成功");
        errorMap.put(EXCEPTION,"异常捕获");
        errorMap.put(PARAM_ERROR,"参数错误");
        errorMap.put(FILE_NOT_FOUNT,"找不到指定文件或目录");
    }

    public int code;

    public String message;

    public Result info(int code, String message){
        this.code = code;
        this.message = message;
        return this;
    }
    public Result info(int code){
        return info(code,errorMap.get(code));
    }
}
