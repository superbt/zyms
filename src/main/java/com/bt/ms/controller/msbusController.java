package com.bt.ms.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bt.ms.common.vo.RespBean;
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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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


    @RequestMapping(value = "/doMs")
    @ResponseBody
    public RespBean doMs(Model model, User user,Long goodsId){
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

    // 1 sucdess  -1 fail  0 ing
    @RequestMapping(value = "/result")
    @ResponseBody
    public RespBean result(Model model, User user,Long goodsId){
        if(user==null){
            return  RespBean.error("用户失效");
        }
        Long status =  msOrderService.getResult(user ,goodsId);
        return  RespBean.success(status);
    }


    @RequestMapping(value = "/details")
    @ResponseBody
    public RespBean details(Model model, User user,Long orderId){
        if(user==null){
            return RespBean.error("用户失效");
        }
        OrderDetailVo orderDetail = new OrderDetailVo();
        Order order= orderService.getById(orderId);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(order.getGoodsId());
        orderDetail.setOrder(order);
        orderDetail.setGoodsVo(goodsVo);
        return  RespBean.success(orderDetail);
    }

    public static void main(String[] args) {
        String t = "1541587348153995266";
        System.out.println(t.length());
    }

    //预加载
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list = goodsService.findGoodsVo();
        if(CollectionUtils.isEmpty(list)){
          return;
        }
        list.forEach(goodsVo -> {
            redisTemplate.opsForValue().set("msgoods:"+goodsVo.getGoodsid(),goodsVo.getStockCount());
            EmptyStorkMap.put(goodsVo.getGoodsid(),false);
        });
    }
}
