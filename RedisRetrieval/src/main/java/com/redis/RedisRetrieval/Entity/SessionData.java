package com.redis.RedisRetrieval.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionData {
    private List<SessionMast> sessionMast;

    // Getters and Setters
}

