package com.markerhub.common.lang;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {

    private int code;
    private String message;
    private Object data;

    /**
     * 成功返回结果
     * @param data
     * @return
     */
    public static Result success(Object data){
        return success(200,"获取成功",data);
    }

    public static Result success(int code,String message,Object data){
        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 失败返回结果
     * @param message
     * @return
     */
    public static Result fail(String message){
        return fail(400,message,null);
    }
    public static Result fail(String message,Object data){
        return fail(400,message,data);
    }
    public static Result fail(int code,String message,Object data){
        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }
}
