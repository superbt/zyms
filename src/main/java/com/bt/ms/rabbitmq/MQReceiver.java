package com.bt.ms.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQReceiver {

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
}
