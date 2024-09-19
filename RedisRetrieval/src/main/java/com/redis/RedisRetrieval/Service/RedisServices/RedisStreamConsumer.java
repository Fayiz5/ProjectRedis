package com.redis.RedisRetrieval.Service.RedisServices;

import com.redis.RedisRetrieval.Entity.KeyOperation;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class RedisStreamConsumer {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String STREAM_NAME = "key-operation-stream";
    private static final String CONSUMER_GROUP = "key-operations-group";
    private static final String CONSUMER_NAME = "consumer-1";
    private static final Logger logger = LoggerFactory.getLogger(RedisStreamConsumer.class);

    private List<KeyOperation> keyOperations = new ArrayList<>();
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();


    @PostConstruct
    public void startListeningToStream() {
        try {
            // Check if the consumer group exists
            boolean groupExists = redisTemplate.opsForStream().groups(STREAM_NAME)
                    .stream()
                    .anyMatch(group -> group.groupName().equals(CONSUMER_GROUP));

            if (!groupExists) {
                // Create the Consumer Group in Redis
                redisTemplate.opsForStream().createGroup(STREAM_NAME, CONSUMER_GROUP);
                logger.info("fayiz Created Redis stream consumer group: {}", CONSUMER_GROUP);
            } else {
                logger.info("fayiz Consumer group {} already exists.", CONSUMER_GROUP);
            }
        } catch (Exception e) {
            logger.error("fayiz Failed to create or verify consumer group {}: {}", CONSUMER_GROUP, e.getMessage());
        }

        // Start a scheduled task to consume messages from the stream
        executorService.scheduleAtFixedRate(this::consumeMessages, 0, 1, TimeUnit.SECONDS);
    }


    private void consumeMessages() {
        try {
            List<MapRecord<String, Object, Object>> messages = redisTemplate.opsForStream()
                    .read(Consumer.from(CONSUMER_GROUP, CONSUMER_NAME),
                            StreamReadOptions.empty().block(Duration.ofMinutes(1)),
                            StreamOffset.fromStart(STREAM_NAME));

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
        } catch (Exception e) {
            logger.error("Error processing stream messages", e);
        }
    }

    public List<KeyOperation> getAllKeyOperations() {
        return new ArrayList<>(keyOperations);
    }

    private void processKeyOperation(String key, String operationType, String timestamp) {
        logger.info("Processing Key: {}, Operation: {}, Timestamp: {}", key, operationType, timestamp);
    }

    // Add shutdown hook to clean up resources
    @PreDestroy
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
