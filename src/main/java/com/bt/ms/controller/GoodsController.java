package com.bt.ms.controller;

import com.bt.ms.common.vo.RespBean;
import com.bt.ms.pojo.Goods;
import com.bt.ms.pojo.User;
import com.bt.ms.service.IGoodsService;
import com.bt.ms.service.IUserService;
import com.bt.ms.vo.DetailVo;
import com.bt.ms.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    IUserService userService ;

    @Autowired
    IGoodsService goodsService ;

    @Autowired
    RedisTemplate redisTemplate ;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver ;

    @RequestMapping(value = "/toList",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toGoodsList(Model model,User user
    ,HttpServletRequest request, HttpServletResponse response ){
        if(user==null){
            return "login";
        }

        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsList");
        if(!StringUtils.isEmpty(html)){
            return html;
        }

        model.addAttribute("user",user);
        List<GoodsVo> list = goodsService.findGoodsVo();
        System.out.println("goodslist:"+list);
        model.addAttribute("goodsList",list);
       // return "goodsList";
        WebContext context = new WebContext(request,response,request.getServletContext(),
                request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList",context);
        if(!StringUtils.isEmpty(html)){
            valueOperations.set("goodsList",html,60, TimeUnit.SECONDS);
            return html;
        }

        return "goodsList";
    }

    @RequestMapping("/toDetail2/{goodsId}")
    public String toDetail2(Model model , User user, @PathVariable Long goodsId){
        if(user==null){
            return "login";
        }
        model.addAttribute("user",user);
        GoodsVo goods =  goodsService.findGoodsVoByGoodsId(goodsId);
        Date startTime = goods.getStartTime();
        Date endTime = goods.getEndTime();
        Date cunrentTime = new Date();
        int msStatus =  0 ;
        long remainSeconds = 0 ;
        if(cunrentTime.before(startTime)){
            msStatus = 0 ;
            remainSeconds =  (startTime.getTime()-cunrentTime.getTime())/1000 ;
        }else  if(cunrentTime.after(endTime)){
            msStatus=2;
        }else{
            msStatus = 1 ;
        }
        model.addAttribute("remainSeconds",remainSeconds);
        model.addAttribute("msStatus",msStatus);
        model.addAttribute("goods",goods);
        return "goodsDetail" ;
    }


    @RequestMapping("/details/{goodsId}")
    @ResponseBody
    public RespBean toDetail(Model model , User user, @PathVariable Long goodsId){
        if(user==null){
            return RespBean.error("用户失效");
        }
        model.addAttribute("user",user);
        GoodsVo goods =  goodsService.findGoodsVoByGoodsId(goodsId);
        Date startTime = goods.getStartTime();
        Date endTime = goods.getEndTime();
        Date cunrentTime = new Date();
        int msStatus =  0 ;
        long remainSeconds = 0 ;
        if(cunrentTime.before(startTime)){
            msStatus = 0 ;
            remainSeconds =  (startTime.getTime()-cunrentTime.getTime())/1000 ;
        }else  if(cunrentTime.after(endTime)){
            msStatus=2;
            remainSeconds = -1 ;
        }else{
            msStatus = 1 ;
            remainSeconds = 0 ;
        }
        DetailVo detailVo = new DetailVo();
        detailVo.setUser(user);
        detailVo.setGoodsVo(goods);
        detailVo.setMsStatus(msStatus);
        detailVo.setRemainSeconds(remainSeconds);
        return RespBean.success(detailVo)  ;
    }
}
