package com.bt.ms.controller;


import com.bt.ms.common.vo.RespBean;
import com.bt.ms.pojo.User;
import com.bt.ms.rabbitmq.MQSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author bt
 * @since 2022-06-22
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    MQSender mqSender ;

    @GetMapping("/info")
    public RespBean info(User user){
        return RespBean.success(user);
    }

    @GetMapping("/mq")
    @ResponseBody
    public RespBean mq(String info){
        mqSender.send(info);
        return RespBean.success();
    }
}
