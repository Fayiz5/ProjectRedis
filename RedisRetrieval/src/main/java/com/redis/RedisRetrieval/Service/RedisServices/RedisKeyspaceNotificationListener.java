package com.redis.RedisRetrieval.Service.RedisServices;

import com.redis.RedisRetrieval.Entity.KeyOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RedisKeyspaceNotificationListener implements MessageListener {

    @Autowired
    private RedisStreamPublisher redisStreamPublisher;

    private static final Logger logger = LoggerFactory.getLogger(RedisKeyspaceNotificationListener.class);

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            KeyOperation keyOperation = new KeyOperation();
            String event = new String(message.getBody());
            keyOperation.setOperationType(extractOperationTypeFromEvent(event));
            keyOperation.setKey(extractKeyFromEvent(event));
            redisStreamPublisher.publishKeyOperation(keyOperation);

            logger.info("Published Key Operation to stream: {}", keyOperation);
        } catch (Exception e) {
            logger.error("Error processing Redis keyspace notification", e);
        }
    }

    private String extractOperationTypeFromEvent(String event) {
        if (event.startsWith("set"))
            return "SET";
        else if (event.startsWith("del"))
            return "DEL";
        else if (event.startsWith("json.set"))
            return "JSON.SET";
        else if (event.startsWith("json.get"))
            return "JSON.GET";
        else if (event.startsWith("get"))
            return "GET";
        else if (event.startsWith("json.del"))
            return "JSON.DEL";
        return "UNKNOWN";
    }

    private String extractKeyFromEvent(String event) {
        try {

            String[] parts = event.split(":");
            if (parts.length >= 2) {
                return parts[1];
            } else {
                logger.warn("Unexpected event format: {}", event);
                return "UNKNOWN_KEY";
            }
        } catch (Exception e) {
            logger.error("Error extracting key from event: {}", event, e);
            return "UNKNOWN_KEY";
        }
    }
}
