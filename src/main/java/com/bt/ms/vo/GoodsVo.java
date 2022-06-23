package com.bt.ms.vo;

import com.bt.ms.pojo.Goods;

import java.math.BigDecimal;
import java.util.Date;

public class GoodsVo extends Goods {

    /**
     * 秒杀价格
     */
    private BigDecimal msPrice;

    /**
     * 库存数量
     */
    private Integer stockCount;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    public GoodsVo(BigDecimal msPrice, Integer stockCount, Date startTime, Date endTime) {
        this.msPrice = msPrice;
        this.stockCount = stockCount;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public GoodsVo() {
    }

    public BigDecimal getMsPrice() {
        return msPrice;
    }

    public void setMsPrice(BigDecimal msPrice) {
        this.msPrice = msPrice;
    }

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
