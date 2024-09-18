package com.redis.RedisRetrieval.Service.RedisServices;

import com.redis.RedisRetrieval.Entity.KeyOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class RedisKeyspaceNotificationListener implements MessageListener {

    @Autowired
    private RedisStreamPublisher redisStreamPublisher;



    @Override
    public void onMessage(Message message, byte[] pattern) {
        KeyOperation keyOperation=new KeyOperation();
        String event = new String(message.getBody());
        keyOperation.setOperationType( extractOperationTypeFromEvent(event));
        keyOperation.setKey( extractKeyFromEvent(event));
        redisStreamPublisher.publishKeyOperation(keyOperation);
    }

    private String extractOperationTypeFromEvent(String event) {
        if (event.startsWith("set"))
            return "SET";
         else if (event.startsWith("del"))
            return "DEL";
        else if(event.startsWith("json.set"))
            return "JSON.SET";
        else if(event.startsWith("json.get"))
            return "JSON.GET";
        return "UNKNOWN";
    }

    private String extractKeyFromEvent(String event) {
        return event.split(":")[1];  // Example parsing logic
    }
}
