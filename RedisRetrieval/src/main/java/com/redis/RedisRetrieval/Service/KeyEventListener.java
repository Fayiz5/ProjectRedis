package com.redis.RedisRetrieval.Service;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class KeyEventListener implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String event = new String(message.getChannel());
        String key = new String(message.getBody());
        String eventType = getEventTypeFromChannel(new String(message.getChannel()));

        // Log the event with timestamp
        System.out.println("Event: " + eventType + ", Key: " + key + " at " + LocalDateTime.now());
    }

    private String getEventTypeFromChannel(String channel) {
        switch (channel) {
            case "__keyevent@0__:set":
                return "SET";
            case "__keyevent@0__:del":
                return "DEL";
            case "__keyevent@0__:expired":
                return "EXPIRED";
            default:
                return "UNKNOWN";
        }
    }
}

