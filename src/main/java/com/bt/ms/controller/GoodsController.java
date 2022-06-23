package com.bt.ms.controller;

import com.bt.ms.pojo.User;
import com.bt.ms.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    IUserService userService ;

/*    @RequestMapping("/toList")
    public String toGoodsList(HttpSession session
    , Model model
    , @CookieValue("userTicket") String userTicket,
                              HttpServletRequest request ,
                              HttpServletResponse response){
        if(StringUtils.isEmpty(userTicket)){
            return "login";
        }
        //User user = (User) session.getAttribute(userTicket);
        User user = userService.getUserByCookie(userTicket,request,response) ;
        if(user==null){
            return "login";
        }
        System.out.println(user.toString());
        model.addAttribute("user",user);
        return "goodsList";
    }*/



    @RequestMapping("/toList")
    public String toGoodsList(Model model,User user){
        if(user==null){
            return "login";
        }
        model.addAttribute("user",user);

        return "goodsList";
    }

    @RequestMapping("/toDetail")
    public String toDetail(Model model ,User user){
        return "" ;
    }
}
