package com.bt.ms.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bt.ms.common.vo.RespBean;
import com.bt.ms.pojo.MsOrder;
import com.bt.ms.pojo.User;
import com.bt.ms.service.IGoodsService;
import com.bt.ms.service.IMsOrderService;
import com.bt.ms.vo.GoodsVo;
import com.bt.ms.vo.MsMessgaeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQReceiver {

    @Autowired
    IGoodsService iGoodsService ;

    @Autowired
    RedisTemplate redisTemplate ;

    @Autowired
    IMsOrderService msOrderService ;

    @RabbitListener(queues = "queue")
    public void receive(Object msg){
        log.info("接受消息："+msg);
    }

    @RabbitListener(queues = "queue_fanout01")
    public void receve01(Object msg){
        log.info("queue_fanout01接受消息："+msg);
    }

    @RabbitListener(queues = "queue_fanout02")
    public void receve02(Object msg){
        log.info("queue_fanout02接受消息："+msg);
    }

    @RabbitListener(queues = "qu_direct01")
    public void receve03(Object msg){
        log.info("qu_direct01接受消息："+msg);
    }

    @RabbitListener(queues = "qu_direct02")
    public void receve04(Object msg){
        log.info("qu_direct02接受消息："+msg);
    }

    @RabbitListener(queues = "msQueue")
    public void receveMs(Object msg){
        log.info("msQueue接受消息："+msg);
        //下单操作
        MsMessgaeVo msMessgaeVo = JSON.parseObject(String.valueOf(msg),MsMessgaeVo.class);
        Long goodsId = msMessgaeVo.getGoodsId();
        User user = msMessgaeVo.getUser();

        GoodsVo goodsVoByGoods= iGoodsService.findGoodsVoByGoodsId(goodsId);
        if(goodsVoByGoods.getStockCount()<1){
            redisTemplate.opsForValue().set( "isStorkEmpty:"+goodsId,"0");
            return;
        }

        MsOrder one = (MsOrder) redisTemplate.opsForValue().get("order:"+user.getId()+":"+goodsId);
        if(one==null){
            one = msOrderService.getOne(new QueryWrapper<MsOrder>().eq("user_id", user.getId())
                    .eq("goods_id", goodsId));
        }
        if(one!=null){
          return;
        }
        msOrderService.doMs(user,goodsId);
    }
}
