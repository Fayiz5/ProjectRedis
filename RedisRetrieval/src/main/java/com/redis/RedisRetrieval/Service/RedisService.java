package com.redis.RedisRetrieval.Service;

import com.redis.RedisRetrieval.RedisUtil.RedisPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RedisService {

    private static final Logger logger = LogManager.getLogger(RedisService.class);

    @Autowired
    private RedisPool redisPool;

    public boolean setKey(String key, String value) {
        try (Jedis jedis = redisPool.getResource()) {
            return jedis.set(key, value) != null;
        } catch (JedisException e) {
            logger.error("Exception occurred while setting key: " + e.toString());
        }
        return false;
    }

    public String getKey(String key) {
        try (Jedis jedis = redisPool.getResource()) {
            return jedis.get(key);
        } catch (JedisException e) {
            logger.error("Exception occurred while getting key: " + e.toString());
        }
        return null;
    }

    public boolean deleteKey(String key) {
        try (Jedis jedis = redisPool.getResource()) {
            return jedis.del(key) > 0;
        } catch (JedisException e) {
            logger.error("Exception occurred while deleting key: " + e.toString());
        }
        return false;
    }

    public Set<String> RetriveKeys(String key)
    {
        Set<String> keys=new HashSet<>();
        try(Jedis jedis=redisPool.getResource()) {
            keys = jedis.keys(key);
        }catch (JedisException e){
            logger.error("Exception while fetching All Matching keys");
        }
        return keys;
    }


}
