package com.bt.ms.common.vo;

import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
public enum RespBeanEnum {
    SUCCESS(200,"SUCCESS"),
    ERROR(500,"服务异常"),
    BIND_ERROR (500212,"参数绑定异常"),
    BADREQUEST(500213,"非法请求"), SESSION_ERROR(500213, "会话失效");
    private final Integer code ;
    private final String message;

    RespBeanEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
