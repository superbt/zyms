package com.bt.ms.service;

import com.bt.ms.pojo.MsOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bt.ms.pojo.Order;
import com.bt.ms.pojo.User;
import com.bt.ms.vo.GoodsVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author bt
 * @since 2022-06-23
 */
public interface IMsOrderService extends IService<MsOrder> {

    Order doMs(User user, Long goodsId);

    String getResult(User user, Long goodsId);

    String createPath(User user, Long goodsId);

    Boolean checkPath(User user, Long goodsId,String path);

    boolean checkCaptch(User user, Long goodsId, String captch);
}
