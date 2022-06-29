package com.bt.ms.intercept;

import com.alibaba.fastjson2.JSON;
import com.bt.ms.common.vo.RespBean;
import com.bt.ms.common.vo.RespBeanEnum;
import com.bt.ms.conf.AccessLimit;
import com.bt.ms.conf.UserContext;
import com.bt.ms.pojo.User;
import com.bt.ms.service.IUserService;
import com.bt.ms.utils.CookieUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

@Component
public class AccessLimitIntercept implements HandlerInterceptor {

    @Autowired
    IUserService userService;

    @Autowired
    RedisTemplate redisTemplate ;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
            if(accessLimit==null){
                return  true ;
            }
            int second = accessLimit.second();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();
            User user = getUser(request,response);
            String key = request.getRequestURI() ;
            UserContext.setUser(user);
            if(needLogin){
               if(user==null){
                   render(response, RespBeanEnum.SESSION_ERROR.getMessage());
                   return false ;
               }
                key += ":"+user.getId();
            }

            ValueOperations operations = redisTemplate.opsForValue();
            Integer count = (Integer) operations.get(key);
            if(count==null){
                operations.set(key,1,second, TimeUnit.SECONDS);
            }else  if(count<maxCount){
                operations.increment(key);
            }else{
                render(response, "访问次数频繁,请稍后再试");
                return false;
            }
        }
        return true;
    }

    private void render(HttpServletResponse response, String msg)
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        RespBean respBean = RespBean.error(msg);
        out.write(JSON.toJSONString(respBean));
        out.flush();
        out.close();
    }

    private User getUser(HttpServletRequest request, HttpServletResponse response) {
        String ticket = CookieUtil.getCookieValue(request,"userTicket");
        if (StringUtils.isEmpty(ticket)){
            return  null ;
        }
        return userService.getUserByCookie(ticket,request,response);
    }
}
