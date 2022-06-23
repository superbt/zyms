package com.bt.ms.service;

import com.bt.ms.pojo.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bt.ms.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author bt
 * @since 2022-06-23
 */
public interface IGoodsService extends IService<Goods> {

     List<GoodsVo> findGoodsVo();
}
