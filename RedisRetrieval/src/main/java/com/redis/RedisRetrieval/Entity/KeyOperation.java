package com.redis.RedisRetrieval.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class KeyOperation {

    private String key;
    private String operationType;
    private String timestamp;


}