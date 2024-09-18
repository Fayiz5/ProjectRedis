package com.redis.RedisRetrieval.Service.RedisServices;

import com.redis.RedisRetrieval.Entity.KeyOperation;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


    @Service
    public class RedisStreamConsumer {

        @Autowired
        private RedisTemplate<String, Object> redisTemplate;

        private static final String STREAM_NAME = "key-operation-stream";
        private static final String CONSUMER_GROUP = "key-operations-group";
        private static final String CONSUMER_NAME = "consumer-1";

        private List<KeyOperation> keyOperations = new ArrayList<>();

        @PostConstruct
        public void startListeningToStream() {
            try {
                redisTemplate.opsForStream().createGroup(STREAM_NAME, CONSUMER_GROUP);
            } catch (Exception e) {

            }

            while (true) {
                List<MapRecord<String, Object, Object>> messages = redisTemplate.opsForStream()
                        .read(Consumer.from(CONSUMER_GROUP, CONSUMER_NAME), StreamReadOptions.empty().block(Duration.ofMinutes(1)), StreamOffset.fromStart(STREAM_NAME));

                if (messages != null && !messages.isEmpty()) {
                    for (MapRecord<String, Object, Object> message : messages) {
                        String key = (String) message.getValue().get("key");
                        String operationType = (String) message.getValue().get("operation");
                        String timestamp = (String) message.getValue().get("timestamp");

                        KeyOperation keyOperation = new KeyOperation(key, operationType, timestamp);
                        keyOperations.add(keyOperation);

                        processKeyOperation(key, operationType, timestamp);

                        redisTemplate.opsForStream().acknowledge(STREAM_NAME, CONSUMER_GROUP, message.getId());
                    }
                }
            }
        }

        public List<KeyOperation> getAllKeyOperations() {
            return new ArrayList<>(keyOperations);
        }

        private void processKeyOperation(String key, String operationType, String timestamp) {
            System.out.println("Key: " + key + ", Operation: " + operationType + ", Timestamp: " + timestamp);
        }
    }

