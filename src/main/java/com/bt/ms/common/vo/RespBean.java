package com.bt.ms.common.vo;

import lombok.Data;


public class RespBean {

    private long code ;
    private String message ;
    private Object obj ;

    public RespBean() {
    }

    public RespBean(long code, String message, Object obj) {
        this.code = code;
        this.message = message;
        this.obj = obj;
    }

    public static RespBean success(){
        return new RespBean(RespBeanEnum.SUCCESS.getCode(), RespBeanEnum.SUCCESS.getMessage(), null);
    }

    public static RespBean success(Object obj){
        return new RespBean(RespBeanEnum.SUCCESS.getCode(), RespBeanEnum.SUCCESS.getMessage(), obj);
    }


    public static RespBean error(){
        return new RespBean(RespBeanEnum.ERROR.getCode(), RespBeanEnum.ERROR.getMessage(), null);
    }

    public static RespBean error(Object obj){
        return new RespBean(RespBeanEnum.ERROR.getCode(), RespBeanEnum.ERROR.getMessage(), obj);
    }

    @Override
    public String toString() {
        return "RespBean{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", obj=" + obj +
                '}';
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
