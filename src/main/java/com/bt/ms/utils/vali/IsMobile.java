package com.bt.ms.utils.vali;

import javax.servlet.annotation.HttpConstraint;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
public @interface  IsMobile {
    boolean required() default  true ;
    String message() default  "手机号码格式有误";
}
