package com.bt.ms.service.impl;
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
import com.bt.ms.service.IMsGoodsService;
import com.bt.ms.service.IMsOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bt.ms.service.IOrderService;
import com.bt.ms.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
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



    @Override
    public Order doMs(User user, GoodsVo goodsVo) {
        //减库存
        MsGoods msGoods = msGoodsService.getOne(new QueryWrapper<MsGoods>().
                eq("goods_id", goodsVo.getGoodsid()));
        msGoods.setStockCount(msGoods.getStockCount()-1);
        msGoodsService.updateById(msGoods);

        //生成订单
        Order order = new Order();
        order.setUserId(Long.parseLong(user.getId()));
        order.setGoodsId(goodsVo.getGoodsid());
        order.setDeliverAddrId(0L);
        order.setGoodsName(goodsVo.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(goodsVo.getGoodsPrice());
        order.setOrderChannel(1);
        order.setOrderStatus(0);
        order.setCreateTime(new Date());
        order.setPayTime(new Date());
        orderService.save(order);


        //生成秒杀订单
        MsOrder msOrder = new MsOrder();
        msOrder.setUserId(Long.parseLong(user.getId()));
        msOrder.setGoodsId(goodsVo.getGoodsid());
        msOrder.setOrderId(order.getOrderId());
        save(msOrder);
        return order;
    }
}
