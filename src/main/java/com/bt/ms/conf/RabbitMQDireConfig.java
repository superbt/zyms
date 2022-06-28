package com.bt.ms.conf;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQDireConfig {

    public static final String QU_O1 ="qu_direct01";
    public static final String QU_O2 ="qu_direct02";
    public static final String EX_DIRECT  ="ex_direct";
    public static final String ROUTKEY_01 ="qu_red";
    public static final String ROUTKEY_02 ="qu_green";


    @Bean
    public Queue queue01(){
        return  new Queue(QU_O1) ;
    }

    @Bean
    public Queue queue02(){
        return  new Queue(QU_O2) ;
    }

    @Bean
    public DirectExchange directExchange(){
        return  new DirectExchange(EX_DIRECT);
    }

    @Bean
    public Binding binding01(){
        return BindingBuilder.bind(queue01()).to(directExchange()).with(ROUTKEY_01);
    }

    @Bean
    public Binding binding02(){
        return BindingBuilder.bind(queue02()).to(directExchange()).with(ROUTKEY_02);
    }

}
