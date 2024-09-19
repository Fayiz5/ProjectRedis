package com.redis.RedisRetrieval.Service.RedisServices;

import com.redis.RedisRetrieval.Entity.KeyOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RedisStreamService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private static final String STREAM_NAME = "key-operation-stream";
    private static final String CONSUMER_GROUP = "key-operations-group";
    private static final String CONSUMER_NAME = "consumer-1";

    public List<KeyOperation> getEvents(LocalDate startDate, LocalDate endDate) {
        StreamOperations<String, Object, Object> streamOps = redisTemplate.opsForStream();

        // Convert dates to message IDs or timestamps
        String startId = (startDate != null) ? getMessageIdFromDate(startDate) : "0";
        String endId = (endDate != null) ? getMessageIdFromDate(endDate.plusDays(1)) : "+";

        List<KeyOperation> keyOperations = new ArrayList<>();
        boolean moreMessages = true;

        while (moreMessages) {
            List<MapRecord<String, Object, Object>> messages = streamOps.read(Consumer.from(CONSUMER_GROUP,CONSUMER_NAME),
                    StreamReadOptions.empty().count(1000),
                    StreamOffset.create(STREAM_NAME, ReadOffset.from(startId))

            );

            // If no more messages are returned, break the loop
            if (messages == null || messages.isEmpty()) {
                moreMessages = false;
            } else {
                // Add the messages to keyOperations list
                keyOperations.addAll(messages.stream()
                        .map(this::mapToKeyOperation)
                        .collect(Collectors.toList()));

                // Get the ID of the last message retrieved and set it as the new startId
                startId = messages.get(messages.size() - 1).getId().getValue();

                // Check if the last message ID is greater than or equal to endId, then stop
                if (messages.get(messages.size() - 1).getId().getValue().compareTo(endId) >= 0) {
                    moreMessages = false;
                }
            }
        }

        return keyOperations;
    }

    private String getMessageIdFromDate(LocalDate date) {
        // Convert LocalDate to a Redis message ID format (example: 1693526400000-0)
        long timestamp = date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000;
        return timestamp + "-0";  // Simplified, assuming no sequence number (part after dash)
    }

    private KeyOperation mapToKeyOperation(MapRecord<String, Object, Object> record) {
        return new KeyOperation(
                (String) record.getValue().get("key"),
                (String) record.getValue().get("operation"),
                (String) record.getValue().get("timestamp")
        );
    }
}
