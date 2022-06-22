package com.bt.ms.controller;

import com.bt.ms.common.vo.RespBean;
import com.bt.ms.service.IUserService;
import com.bt.ms.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Autowired
    IUserService userService ;

    @RequestMapping("/tologin")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/dologin")
    @ResponseBody
    public String doLogin(@Validated LoginVo loginVo){
        System.out.println(loginVo);
        RespBean respBean = userService.doLogin(loginVo);

        System.out.println(respBean.toString());
        return respBean.toString();
    }
}
