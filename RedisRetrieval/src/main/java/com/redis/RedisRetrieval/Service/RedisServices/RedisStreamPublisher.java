package com.redis.RedisRetrieval.Service.RedisServices;

import com.redis.RedisRetrieval.Entity.KeyOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class RedisStreamPublisher {

    @Autowired
    private RedisTemplate<String, KeyOperation> redisTemplate;

    private static final String STREAM_NAME = "key-operation-stream";

    public void publishKeyOperation(KeyOperation keyOperation) {
        Map<String, Object> keyOperationData = new HashMap<>();
        keyOperationData.put("key", keyOperation.getKey());
        keyOperationData.put("operation", keyOperation.getOperationType());
        keyOperation.setTimestamp(LocalDateTime.now().toString());
        keyOperationData.put("timestamp", keyOperation.getTimestamp());

        redisTemplate.opsForStream().add(STREAM_NAME, keyOperationData);
    }
}
