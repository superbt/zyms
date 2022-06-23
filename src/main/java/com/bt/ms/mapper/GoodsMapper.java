package com.bt.ms.mapper;

import com.bt.ms.pojo.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bt.ms.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author bt
 * @since 2022-06-23
 */
public interface GoodsMapper extends BaseMapper<Goods> {
    public List<GoodsVo> findGoodsVo();
}
