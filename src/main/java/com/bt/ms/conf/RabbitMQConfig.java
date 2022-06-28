package com.bt.ms.conf;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//@Configuration
public class RabbitMQConfig {

    public static String QUEUE01 = "queue_fanout01";
    public static String QUEUE02 = "queue_fanout02";
    public static String EX_FANOUT = "ex_fanout";

    @Bean
    public Queue queue(){
        return new Queue("queue",true) ;
    }

    @Bean
    public Queue queue01(){
        return new Queue(QUEUE01,true) ;
    }

    @Bean
    public Queue queue02(){
        return new Queue(QUEUE02,true) ;
    }

    @Bean
    public FanoutExchange fanoutExchange(){
        return  new FanoutExchange(EX_FANOUT);
    }

    @Bean
    public Binding binding01(){
        return BindingBuilder.bind(queue01()).to(fanoutExchange());
    }
    @Bean
    public Binding binding02(){
        return BindingBuilder.bind(queue02()).to(fanoutExchange());
    }

}
