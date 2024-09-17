package com.redis.RedisRetrieval.Exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;

public class DataProcessingException extends Throwable {
    public DataProcessingException(String s, JsonProcessingException e) {
        super(s);
    }
}
