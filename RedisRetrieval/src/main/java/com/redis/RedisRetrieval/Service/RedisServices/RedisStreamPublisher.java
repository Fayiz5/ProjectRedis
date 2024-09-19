package com.redis.RedisRetrieval.Service.RedisServices;

import com.redis.RedisRetrieval.Entity.KeyOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class RedisStreamPublisher {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String STREAM_NAME = "key-operation-stream";
    private static final Logger logger = LoggerFactory.getLogger(RedisStreamPublisher.class);

    public void publishKeyOperation(KeyOperation keyOperation) {
        try {
            Map<String, Object> keyOperationData = new HashMap<>();
            keyOperation.setTimestamp(LocalDateTime.now().toString());  // Atomic timestamp assignment
            keyOperationData.put("key", keyOperation.getKey());
            keyOperationData.put("operation", keyOperation.getOperationType());
            keyOperationData.put("timestamp", keyOperation.getTimestamp());

            //IN REDIS CMD :XADD <STREAM_NAME> * DATA
            redisTemplate.opsForStream().add(STREAM_NAME, keyOperationData);

            logger.info("Published key operation to Redis stream: {}", keyOperation);
        } catch (Exception e) {
            logger.error("Failed to publish key operation to Redis stream", e);
        }
    }
}
