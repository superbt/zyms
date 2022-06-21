package com.bt.ms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/demo")
public class Test {

    @RequestMapping("/hello")
    public String hello(Model model){
        model.addAttribute("name","bt");
        return "hello" ;
    }
}
