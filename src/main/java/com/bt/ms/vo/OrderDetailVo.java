package com.bt.ms.vo;

import com.bt.ms.pojo.Order;

public class OrderDetailVo {

    GoodsVo goodsVo;

    Order order ;

    public GoodsVo getGoodsVo() {
        return goodsVo;
    }

    public void setGoodsVo(GoodsVo goodsVo) {
        this.goodsVo = goodsVo;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
