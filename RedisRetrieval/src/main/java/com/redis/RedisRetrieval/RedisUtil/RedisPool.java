package com.redis.RedisRetrieval.RedisUtil;

import com.redis.RedisRetrieval.Config.Config;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;

@Component
public class RedisPool {

    private static final Logger logger = LogManager.getLogger(RedisPool.class);
    private JedisPool pool;

    @Autowired
    private Config config;

    @PostConstruct
    public void init() {
        try {
            JedisPoolConfig poolConfig = buildPoolConfig();
            String redisUri = "redis://" + config.getRedisurl() + ":" + config.getRedisport();
            pool = new JedisPool(poolConfig, new URI(redisUri));
            logger.info("RedisPool initialized with URI: " + redisUri);
        } catch (URISyntaxException e) {
            logger.error("Invalid Redis URI syntax: " + e.toString());
        } catch (Exception e) {
            logger.error("Exception occurred while initializing RedisPool: " + e.toString());
        }
    }

    private JedisPoolConfig buildPoolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(128);
        poolConfig.setMinIdle(16);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
        poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(true);
        return poolConfig;
    }

    public Jedis getResource() {
        return pool.getResource();
    }

    @PreDestroy
    public void close() {
        if (pool != null) {
            pool.close();
        }
    }
}
