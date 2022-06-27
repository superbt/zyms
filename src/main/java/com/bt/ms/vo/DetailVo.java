package com.bt.ms.vo;

import com.bt.ms.pojo.User;

public class DetailVo {
    User user ;
    GoodsVo goodsVo ;
    int msStatus;
    Long remainSeconds ;

    public DetailVo() {
    }

    public DetailVo(User user, GoodsVo goodsVo, int msStatus, Long remainSeconds) {
        this.user = user;
        this.goodsVo = goodsVo;
        this.msStatus = msStatus;
        this.remainSeconds = remainSeconds;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GoodsVo getGoodsVo() {
        return goodsVo;
    }

    public void setGoodsVo(GoodsVo goodsVo) {
        this.goodsVo = goodsVo;
    }

    public int getMsStatus() {
        return msStatus;
    }

    public void setMsStatus(int msStatus) {
        this.msStatus = msStatus;
    }

    public Long getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(Long remainSeconds) {
        this.remainSeconds = remainSeconds;
    }

    @Override
    public String toString() {
        return "DetailVo{" +
                "user=" + user +
                ", goodsVo=" + goodsVo +
                ", msStatus=" + msStatus +
                ", remainSeconds=" + remainSeconds +
                '}';
    }
}
