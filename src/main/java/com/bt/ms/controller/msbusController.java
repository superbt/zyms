package com.bt.ms.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bt.ms.common.vo.RespBean;
import com.bt.ms.common.vo.RespBeanEnum;
import com.bt.ms.exception.GlobalException;
import com.bt.ms.pojo.MsOrder;
import com.bt.ms.pojo.Order;
import com.bt.ms.pojo.User;
import com.bt.ms.rabbitmq.MQSender;
import com.bt.ms.service.IGoodsService;
import com.bt.ms.service.IMsOrderService;
import com.bt.ms.service.IOrderService;
import com.bt.ms.vo.GoodsVo;
import com.bt.ms.vo.MsMessgaeVo;
import com.bt.ms.vo.OrderDetailVo;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Controller
@RequestMapping("/msbus")
public class msbusController implements InitializingBean {

    @Autowired
    IGoodsService goodsService ;

    @Autowired
    IMsOrderService msOrderService ;

    @Autowired
    IOrderService orderService;

    @Autowired
    RedisTemplate redisTemplate ;

    @Autowired
    MQSender mqSender;

    @Resource(name="defaultRedisScriptlong")
    RedisScript defaultRedisScriptlong ;


    private Map<Long, Boolean> EmptyStorkMap = new ConcurrentHashMap<>();

    @RequestMapping("/doMs2")
    public String doMs2(Model model, User user,Long goodsId) {
        if(user==null){
            return "login";
        }
        model.addAttribute("user",user);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        if(goodsVo.getStockCount()<1){
            model.addAttribute("errmsg", "商品库存不足");
            return "msFail";
        }

        MsOrder one = msOrderService.getOne(new QueryWrapper<MsOrder>().eq("user_id", user.getId())
                .eq("goods_id", goodsId));
        if(one!=null){
            model.addAttribute("errmsg", "订单不可重复");
            return "msFail";
        }

        Order order= msOrderService.doMs(user , goodsId);
        model.addAttribute("order",order);
        model.addAttribute("goods",goodsVo);

        return "orderDetail";
    }

    @RequestMapping(value = "/doMs3")
    @ResponseBody
    public RespBean doMs3(Model model, User user,Long goodsId){
        if(user==null){
            return RespBean.error("用户失效");
        }
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        if(goodsVo.getStockCount()<1){
            return RespBean.error("库存不足");
        }

        MsOrder one = (MsOrder) redisTemplate.opsForValue().get("order:"+user.getId()+":"+goodsVo.getGoodsid());
        if(one==null){
            one = msOrderService.getOne(new QueryWrapper<MsOrder>().eq("user_id", user.getId())
                    .eq("goods_id", goodsId));
        }
        if(one!=null){
            return RespBean.error("订单不可重复");
        }

        Order order= msOrderService.doMs(user , goodsId);
        System.out.println("=="+order.getOrderId());
        return  RespBean.success(String.valueOf(order.getOrderId()));
    }


    @RequestMapping(value = "/doMs4")
    @ResponseBody
    public RespBean doMs4(Model model, User user,Long goodsId){
        if(user==null){
            return RespBean.error("用户失效");
        }
        MsOrder one = (MsOrder) redisTemplate.opsForValue().get("order:"+user.getId()+":"+goodsId);
        if(one==null){
            one = msOrderService.getOne(new QueryWrapper<MsOrder>().eq("user_id", user.getId())
                    .eq("goods_id", goodsId));
        }
        if(one!=null){
            return RespBean.error("订单不可重复");
        }
        if(EmptyStorkMap.get(goodsId)){
            return RespBean.error("库存不足");
        }
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Long stock = valueOperations.decrement("msgoods:"+goodsId);
        if(stock<0){
            EmptyStorkMap.put(goodsId,true);
            valueOperations.increment("msgoods:"+goodsId);
            return RespBean.error("库存不足");
        }

        MsMessgaeVo msMessgaeVo = new MsMessgaeVo(user,goodsId);
        mqSender.sendMsMessage(JSON.toJSONString(msMessgaeVo));
        return RespBean.success(0) ;
    }


    @RequestMapping(value = "/doMs5")
    @ResponseBody
    public RespBean doMs5(Model model, User user,Long goodsId){
        if(user==null){
            return RespBean.error("用户失效");
        }
        MsOrder one = (MsOrder) redisTemplate.opsForValue().get("order:"+user.getId()+":"+goodsId);
        if(one==null){
            one = msOrderService.getOne(new QueryWrapper<MsOrder>().eq("user_id", user.getId())
                    .eq("goods_id", goodsId));
        }
        if(one!=null){
            return RespBean.error("订单不可重复");
        }
        if(EmptyStorkMap.get(goodsId)){
            return RespBean.error("库存不足");
        }
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //Long stock = valueOperations.decrement("msgoods:"+goodsId);
        Long stock = (Long)redisTemplate.execute(defaultRedisScriptlong,Collections.singletonList("msgoods:"+goodsId),
                Collections.EMPTY_LIST);
        if(stock<0){
            EmptyStorkMap.put(goodsId,true);
            valueOperations.increment("msgoods:"+goodsId);
            return RespBean.error("库存不足");
        }

        MsMessgaeVo msMessgaeVo = new MsMessgaeVo(user,goodsId);
        mqSender.sendMsMessage(JSON.toJSONString(msMessgaeVo));
        return RespBean.success(0) ;
    }

    @RequestMapping(value = "/{path}/doMs")
    @ResponseBody
    public RespBean doMs(@PathVariable String path, Model model, User user, Long goodsId){
        if(user==null){
            return RespBean.error("用户失效");
        }

       Boolean check = msOrderService.checkPath(user,goodsId,path);
       if(!check){
           return RespBean.error("非法请求");
       }

        MsOrder one = (MsOrder) redisTemplate.opsForValue().get("order:"+user.getId()+":"+goodsId);
        if(one==null){
            one = msOrderService.getOne(new QueryWrapper<MsOrder>().eq("user_id", user.getId())
                    .eq("goods_id", goodsId));
        }
        if(one!=null){
            return RespBean.error("订单不可重复");
        }
        if(EmptyStorkMap.get(goodsId)){
            return RespBean.error("库存不足");
        }
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //Long stock = valueOperations.decrement("msgoods:"+goodsId);
        Long stock = (Long)redisTemplate.execute(defaultRedisScriptlong,Collections.singletonList("msgoods:"+goodsId),
                Collections.EMPTY_LIST);
        if(stock<0){
            EmptyStorkMap.put(goodsId,true);
            valueOperations.increment("msgoods:"+goodsId);
            return RespBean.error("库存不足");
        }

        MsMessgaeVo msMessgaeVo = new MsMessgaeVo(user,goodsId);
        mqSender.sendMsMessage(JSON.toJSONString(msMessgaeVo));
        return RespBean.success(0) ;
    }

    // 1 sucdess  -1 fail  0 ing
    @RequestMapping(value = "/result")
    @ResponseBody
    public RespBean result(Model model, User user,Long goodsId){
        if(user==null){
            return  RespBean.error("用户失效");
        }
        String status =  msOrderService.getResult(user ,goodsId);
        return  RespBean.success(status);
    }


    @RequestMapping(value = "/details")
    @ResponseBody
    public RespBean details(Model model, User user,String orderId){

        if(user==null){
            return RespBean.error("用户失效");
        }
        Long orderIdL = Long.parseLong(orderId);
        OrderDetailVo orderDetail = new OrderDetailVo();
        Order order= orderService.getById(orderIdL);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(order.getGoodsId());
        orderDetail.setOrder(order);
        orderDetail.setGoodsVo(goodsVo);
        return  RespBean.success(orderDetail);
    }

    public static void main(String[] args) {
        String t = "1541587348153995266";
        System.out.println(t.length());

        User user = new User();
        user.setNickname("xbt");
        Long goodsId = 100L ;
        MsMessgaeVo msMessgaeVo = new MsMessgaeVo(user,goodsId);
        String msg = JSON.toJSONString(msMessgaeVo) ;
        Object obj = msg ;
        System.out.println(obj);
        //System.out.println(msg);
        //MsMessgaeVo msMessgaeVo2 = JSONObject.parseObject(msg,MsMessgaeVo.class);
    }

    //预加载
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list = goodsService.findGoodsVo();
        System.out.println("初始化商品信息。。。。。。。。。。。。。。。。。。");
        if(CollectionUtils.isEmpty(list)){
          return;
        }
        System.out.println("初始化商品信息。。。。。。。。。。。。。。。。。。"+list.toString());
        list.forEach(goodsVo -> {
            redisTemplate.opsForValue().set("msgoods:"+goodsVo.getGoodsid(),goodsVo.getStockCount());
            EmptyStorkMap.put(goodsVo.getGoodsid(),false);
        });
    }

    @RequestMapping("/path")
    @ResponseBody
    public RespBean getPath(User user , Long goodsId
            , String captch, HttpServletRequest request){
        if(user==null){
            return RespBean.error("用户失效");
        }

        ValueOperations operations = redisTemplate.opsForValue();
        //5s访问5次
        String uri = request.getRequestURI();
        Integer count = (Integer) operations.get(uri+":"+user.getId());
        if(count==null){
            operations.set(uri+":"+user.getId(),1,5,TimeUnit.SECONDS);
        }else if(count<5){
            operations.increment(uri+":"+user.getId());
        }else {
            return RespBean.error("您访问频繁，请稍后再试");
        }
        boolean check = msOrderService.checkCaptch(user,goodsId,captch);
        if(!check){
            return RespBean.error("验证码失败");
        }
        String str = msOrderService.createPath(user,goodsId);
        return  RespBean.success(str) ;
    }

    @RequestMapping("/captcha")
    public void getCaptcha(User user , Long goodsId, HttpServletResponse response){
        if(user==null||goodsId==null){
           throw  new GlobalException(RespBeanEnum.BADREQUEST);
        }
        // 算术类型
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 48);
        redisTemplate.opsForValue().set("captcha:"+user.getId()+goodsId,captcha.text(),300, TimeUnit.SECONDS);
        response.setContentType("image/jpg");
        response.setHeader("Param","No-cache");
        response.setHeader("Cache-Control","no-cache");
        response.setDateHeader("Expires",0);
        try {
            captcha.out(response.getOutputStream());
        } catch (IOException e) {
            log.info("验证码流错误");
            e.printStackTrace();
        }
    }

}
