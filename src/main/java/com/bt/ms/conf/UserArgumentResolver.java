package com.bt.ms.conf;

import com.bt.ms.pojo.User;
import com.bt.ms.service.IUserService;
import com.bt.ms.utils.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    IUserService userService ;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> parameterType = parameter.getParameterType();
        return parameterType == User.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

       User user = UserContext.getUser() ;
       if(user ==null){
           System.out.println("解析器===处理user用户对象");
           HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
           HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
           String ticket = CookieUtil.getCookieValue(request,"userTicket");
           System.out.println("getuserTicket:"+ticket);
           if (StringUtils.isEmpty(ticket)){
               return  null ;
           }
           user =  userService.getUserByCookie(ticket,request,response);
           UserContext.setUser(user);
       }
        return  UserContext.getUser();
    }
}
