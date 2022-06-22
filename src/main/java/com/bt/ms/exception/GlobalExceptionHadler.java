package com.bt.ms.exception;

import com.bt.ms.common.vo.RespBean;
import com.bt.ms.common.vo.RespBeanEnum;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHadler {

    @ExceptionHandler(Exception.class)
    public RespBean exceptionHalder(Exception e){
        System.out.println("进入全局异常处理。。。。。");
        if(e instanceof  GlobalException){
            GlobalException ex = (GlobalException) e ;
            return  RespBean.error(ex.getRespBeanEnum());
        }else if (e instanceof BindException) {
            BindException ex = (BindException) e ;
            RespBean respBean = RespBean.error(RespBeanEnum.BIND_ERROR);
            respBean.setMessage("校验异常："+e.getMessage());
            return respBean ;
        }
        return RespBean.error(RespBeanEnum.ERROR);
    }
}
