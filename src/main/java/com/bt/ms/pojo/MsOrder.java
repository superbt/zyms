package com.bt.ms.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author bt
 * @since 2022-06-23
 */
@TableName("t_ms_order")
public class MsOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 秒杀订单id
     */
    private Long msOrderId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 订单id
     */
    private Long orderId;

    public Long getMsOrderId() {
        return msOrderId;
    }

    public void setMsOrderId(Long msOrderId) {
        this.msOrderId = msOrderId;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "MsOrder{" +
            "msOrderId=" + msOrderId +
            ", userId=" + userId +
            ", goodsId=" + goodsId +
            ", orderId=" + orderId +
        "}";
    }
}
