package com.bt.ms.pojo;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author bt
 * @since 2022-06-23
 */
@TableName("t_ms_goods")
public class MsGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 秒杀订单id
     */
    @TableId
    private Long msGoodsId;

    /**
     * 商品id
     */
    private Long goodsId;

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

    public Long getMsGoodsId() {
        return msGoodsId;
    }

    public void setMsGoodsId(Long msGoodsId) {
        this.msGoodsId = msGoodsId;
    }
    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
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



    @Override
    public String toString() {
        return "MsGoods{" +
            "msGoodsId=" + msGoodsId +
            ", goodsId=" + goodsId +
            ", msPrice=" + msPrice +
            ", stockCount=" + stockCount +
            ", startTime=" + startTime +
            ", endTime=" + endTime +
        "}";
    }
}
