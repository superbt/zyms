package com.bt.ms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bt.ms.common.vo.RespBean;
import com.bt.ms.mapper.UserMapper;
import com.bt.ms.pojo.User;
import com.bt.ms.service.IUserService;
import com.bt.ms.utils.CookieUtil;
import com.bt.ms.utils.UUIDUtil;
import com.bt.ms.utils.vali.ValidatorUtil;
import com.bt.ms.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author bt
 * @since 2022-06-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    UserMapper userMapper ;

    @Autowired
    RedisTemplate redisTemplate ;


    @Override
    public RespBean doLogin(LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        if(StringUtils.isEmpty(mobile)){
            return  RespBean.error("用户名错误");
        }else if(ValidatorUtil.isMobile(mobile)){
            return  RespBean.error("手机号格式有误");
        }
        User user =  userMapper.selectById(mobile);
        if(user==null){
            return  RespBean.error("用户不存在");
        }
        if(user.getPassword()!=null&&!password.equals(user.getPassword())){
            return  RespBean.error("用户密码错误");
        }
        return RespBean.success();
    }

    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        if(StringUtils.isEmpty(mobile)){
            return  RespBean.error("用户名错误");
        }else if(ValidatorUtil.isMobile(mobile)){
            return  RespBean.error("手机号格式有误");
        }
        User user =  userMapper.selectById(mobile);
        if(user==null){
            return  RespBean.error("用户不存在");
        }
        if(user.getPassword()!=null&&!password.equals(user.getPassword())){
            return  RespBean.error("用户密码错误");
        }
        String ticket = UUIDUtil.uuid();
        // request.getSession().setAttribute(ticket,user);
        redisTemplate.opsForValue().set("user:"+ticket,user, Duration.ofMinutes(30));
        //redisTemplate.opsForValue().set("user:"+ticket,user);
        CookieUtil.setCookie(request,response,"userTicket",ticket);
        return RespBean.success();
    }

    @Override
    public User getUserByCookie(String userTicket ,
                                HttpServletRequest request ,
                                HttpServletResponse response) {
        if(StringUtils.isEmpty(userTicket)){
            return null ;
        }
        User user = (User) redisTemplate.opsForValue().get("user:"+userTicket);
        if(user!=null){
            CookieUtil.setCookie(request,response,"userTicket",userTicket);
        }
        return user;
    }
}
