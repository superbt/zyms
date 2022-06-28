package com.bt.ms.vo;

import com.bt.ms.pojo.User;

public class MsMessgaeVo {
    private User user;
    private Long goodsId;

    public MsMessgaeVo(User user, Long goodsId) {
        this.user = user;
        this.goodsId = goodsId;
    }

    public MsMessgaeVo() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }
}
