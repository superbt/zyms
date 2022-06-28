package com.bt.ms.rabbitmq;

import com.bt.ms.conf.RabbitMQConfig;
import com.bt.ms.conf.RabbitMQDireConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQSender {

    @Autowired
    RabbitTemplate rabbitTemplate ;

    public void send(Object msg){
        log.info("发送消息："+msg);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EX_FANOUT,"",msg);
    }

    public void send01(Object msg){
        log.info("RED发送消息："+msg);
        rabbitTemplate.convertAndSend(RabbitMQDireConfig.EX_DIRECT,RabbitMQDireConfig.ROUTKEY_01,msg);
    }
    public void send02(Object msg){
        log.info("GREEN发送消息："+msg);
        rabbitTemplate.convertAndSend(RabbitMQDireConfig.EX_DIRECT,RabbitMQDireConfig.ROUTKEY_02,msg);
    }
}
