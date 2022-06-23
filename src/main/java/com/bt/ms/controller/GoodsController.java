package com.bt.ms.controller;

import com.bt.ms.pojo.User;
import com.bt.ms.service.IGoodsService;
import com.bt.ms.service.IUserService;
import com.bt.ms.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    IUserService userService ;

    @Autowired
    IGoodsService goodsService ;

    @RequestMapping("/toList")
    public String toGoodsList(Model model,User user){
        if(user==null){
            return "login";
        }
        model.addAttribute("user",user);
        List<GoodsVo> list = goodsService.findGoodsVo();
        System.out.println("goodslist:"+list);
        model.addAttribute("goodsList",list);
        return "goodsList";
    }

    @RequestMapping("/toDetail")
    public String toDetail(Model model ,User user){
        return "" ;
    }
}
