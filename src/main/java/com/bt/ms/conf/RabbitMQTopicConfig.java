package com.bt.ms.conf;

import com.rabbitmq.client.AMQP;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTopicConfig {

    public static final String QUEUE = "msQueue";
    public static final String EXCHANGE = "msExchange";

    @Bean
    public Queue queue(){
        return  new Queue(QUEUE);
    }

    @Bean
    public TopicExchange topicExchange(){
       return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue()).to(topicExchange()).with("ms.#");
    }


}
