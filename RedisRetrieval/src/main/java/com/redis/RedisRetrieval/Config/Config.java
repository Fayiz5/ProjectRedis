package com.redis.RedisRetrieval.Config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Configuration
public class Config {

    @Value("${spring.redis.host}")
    private String Redisurl;

    @Value("${spring.redis.port}")
    private int redisport;

    public String getRedisurl() {
        return Redisurl;
    }


    public int getRedisport() {
        return redisport;
    }

}
