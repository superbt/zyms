package com.bt.ms.exception;

import com.bt.ms.common.vo.RespBean;
import com.bt.ms.common.vo.RespBeanEnum;

public class GlobalException extends  RuntimeException {


    private RespBeanEnum respBeanEnum ;


    public GlobalException(RespBeanEnum respBeanEnum) {
        this.respBeanEnum = respBeanEnum;
    }

    public GlobalException() {
    }

    public RespBeanEnum getRespBeanEnum() {
        return respBeanEnum;
    }

    public void setRespBeanEnum(RespBeanEnum respBeanEnum) {
        this.respBeanEnum = respBeanEnum;
    }
}
