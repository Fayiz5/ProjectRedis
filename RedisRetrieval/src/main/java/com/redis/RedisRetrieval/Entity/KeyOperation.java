package com.redis.RedisRetrieval.Entity;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class KeyOperation {

    private String key;
    private String operationType;
    private String timestamp;


    public KeyOperation(String key, String operationType, String timestamp) {
        this.key = key;
        this.operationType = operationType;
        this.timestamp = timestamp;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

