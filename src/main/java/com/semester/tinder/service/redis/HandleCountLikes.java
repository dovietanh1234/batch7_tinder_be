package com.semester.tinder.service.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;


import java.util.Set;

@Service
@EnableScheduling
public class HandleCountLikes {


    private final RedisTemplate<String, Integer> redisTemplate;

    public HandleCountLikes( RedisTemplate<String, Integer> redisTemplate ){
            this.redisTemplate = redisTemplate;
    }


    @Bean
    public ThreadPoolTaskExecutor taskExecutor(){

        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

        // dat so luong luong core la 5
        taskExecutor.setCorePoolSize(5);
        // dat so luong max threads la 10
        taskExecutor.setMaxPoolSize(10);
        // dat size of queue la 100.
        taskExecutor.setQueueCapacity(100);
        taskExecutor.initialize();

        return taskExecutor;
    }

    public int like(String userId){

        ValueOperations<String, Integer> ops = redisTemplate.opsForValue();

        // check is user exist?
        Boolean hasUser = redisTemplate.hasKey(userId);
        if( hasUser != null && hasUser ){

            // lay qua key la userId
            Integer likes = ops.get( userId );

            if( likes != null && likes > 0 ){
                    ops.set( userId, likes - 1 );
                    return 200;
            } else {
                // thong bao den nguoi dung ban da het like bang cach su dung notification
                return 400;
            }

        }else {
// 50 like nhung dat 5 de test...
            ops.set(userId, 4); // 47
            return 200;
        }
    }

    // chay vao 0h dem hang ngay!
    @Scheduled(cron = "0 0 0 * * ?")
    public void resetLikes(){
        Set<String> keys = redisTemplate.keys("*");
        if( keys != null ){
            for ( String key : keys ){
                redisTemplate.opsForValue().set(key, 5); // 50 de 5 de test
            }
        }

    }


    public String testOnDemo(){

        try {
            Set<String> keys = redisTemplate.keys("*");
            if( keys != null ){
                for ( String key : keys ){
                    redisTemplate.opsForValue().set(key, 5); // 50 de 5 de test
                }

            }
            return "update number like for all users in redis success";
        }catch (Exception e){
            return e.getMessage();
        }



    }


}
