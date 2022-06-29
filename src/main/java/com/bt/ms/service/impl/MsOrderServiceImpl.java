package com.bt.ms.service.impl;
import java.beans.Transient;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bt.ms.mapper.MsGoodsMapper;
import com.bt.ms.mapper.OrderMapper;
import com.bt.ms.pojo.MsGoods;
import com.bt.ms.pojo.MsOrder;
import com.bt.ms.mapper.MsOrderMapper;
import com.bt.ms.pojo.Order;
import com.bt.ms.pojo.User;
import com.bt.ms.service.IGoodsService;
import com.bt.ms.service.IMsGoodsService;
import com.bt.ms.service.IMsOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bt.ms.service.IOrderService;
import com.bt.ms.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author bt
 * @since 2022-06-23
 */
@Service
public class MsOrderServiceImpl extends ServiceImpl<MsOrderMapper, MsOrder> implements IMsOrderService {

    @Autowired
    IMsGoodsService msGoodsService ;

    @Autowired
    IOrderService orderService ;

    @Autowired
    RedisTemplate redisTemplate ;


    @Autowired
    IGoodsService iGoodsService ;

    @Autowired
    MsOrderMapper msOrderMapper ;



    @Transient
    @Override
    public Order doMs(User user, Long goodsId) {
        //减库存
        MsGoods msGoods = msGoodsService.getOne(new QueryWrapper<MsGoods>().
                eq("goods_id", goodsId));
        GoodsVo goodsVo = iGoodsService.findGoodsVoByGoodsId(goodsId);
        msGoods.setStockCount(msGoods.getStockCount()-1);
        //加条件 库存》0
        msGoodsService.updateById(msGoods);

        //生成订单
        Order order = new Order();
        order.setUserId(Long.parseLong(user.getId()));
        order.setGoodsId(goodsId);
        order.setDeliverAddrId(0L);
        order.setGoodsName(goodsVo.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(goodsVo.getGoodsPrice());
        order.setOrderChannel(1);
        order.setOrderStatus(0);
        order.setCreateTime(new Date());
        order.setPayTime(new Date());
        orderService.save(order);
        System.out.println(order.toString());


        //生成秒杀订单
        MsOrder msOrder = new MsOrder();
        msOrder.setUserId(Long.parseLong(user.getId()));
        msOrder.setGoodsId(goodsVo.getGoodsid());
        msOrder.setOrderId(order.getOrderId());
        save(msOrder);
        redisTemplate.opsForValue().set("order:"+user.getId()+":"+goodsVo.getGoodsid(),msOrder);
        return order;
    }

    @Override
    public String getResult(User user, Long goodsId) {
        //查询
        //new QueryWrapper<>().eq("user_id", user.getId()).
        //                eq("goods_id", goodsId)
        MsOrder msOrder = msOrderMapper.selectOne(new QueryWrapper<MsOrder>().eq("user_id", user.getId()).
                       eq("goods_id", goodsId));
        if(msOrder!=null){
            return String.valueOf(msOrder.getOrderId()) ;
        }else if(redisTemplate.hasKey("isStorkEmpty:"+goodsId)){
            return "-1";
        }{
            return  "0" ;
        }
    }
}
