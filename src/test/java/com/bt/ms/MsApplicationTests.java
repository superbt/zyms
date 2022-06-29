package com.bt.ms;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class MsApplicationTests {

  @Autowired
    RedisTemplate redisTemplate ;

  @Autowired
    RedisScript script ;


    @Test
    void contextLoads() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Boolean  isLock = valueOperations.setIfAbsent("k1","v1");
        if(isLock){
            valueOperations.set("name","xxxx");
            String name = (String) valueOperations.get("name");
            System.out.println("name==="+name);
           // Integer.parseInt("ABC");
            redisTemplate.delete("k1");
        }else{
            System.out.println("线程被使用，请稍后再试");
        }
    }

    //@Test
    public void testLock01(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Boolean  isLock = valueOperations.setIfAbsent("k1","v1");
        if(isLock){
            valueOperations.set("name","xxxx");
            String name = (String) valueOperations.get("name");
            System.out.println("name==="+name);
            // Integer.parseInt("ABC");
            redisTemplate.delete("k1");
        }else{
            System.out.println("线程被使用，请稍后再试");
        }
    }

    //@Test
    public void testLock02(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Boolean  isLock = valueOperations.setIfAbsent("k1","v1",5, TimeUnit.SECONDS);
        if(isLock){
            valueOperations.set("name","xxxx");
            String name = (String) valueOperations.get("name");
            System.out.println("name==="+name);
             //Integer.parseInt("ABC");
            redisTemplate.delete("k1");
        }else{
            System.out.println("线程被使用，请稍后再试");
        }
    }

   // @Test
    public void testLock03(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String value = UUID.randomUUID().toString();
        Boolean  isLock = valueOperations.setIfAbsent("k1",value,5, TimeUnit.SECONDS);
        if(isLock){
            valueOperations.set("name","xxxx");
            String name = (String) valueOperations.get("name");
            System.out.println("name==="+name);
           Object result = redisTemplate.execute(script, Collections.singletonList("k1"),value);
            System.out.println("result==="+result);
            //redisTemplate.delete("k1");
        }else{
            System.out.println("线程被使用，请稍后再试");
        }
    }

    @Test
    public void testMuliThread(){
        ExecutorService executor = Executors.newFixedThreadPool(5);
       // executor.submit()
        for(int i=0 ;i<5;i++){
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread()+"===start");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    testLock03();
                    System.out.println(Thread.currentThread()+"===end");
                }
            });
        }
        executor.shutdown();
    }
}
