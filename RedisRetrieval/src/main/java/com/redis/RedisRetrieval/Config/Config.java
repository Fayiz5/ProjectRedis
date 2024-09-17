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

    @Value("${server.url}")
    private String Redisurl;

    @Value("${server.port}")
    private String redisport;

    public String getRedisurl() {
        return Redisurl;
    }


    public String getRedisport() {
        return redisport;
    }

}
