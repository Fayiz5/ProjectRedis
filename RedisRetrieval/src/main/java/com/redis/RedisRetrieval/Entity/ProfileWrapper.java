package com.redis.RedisRetrieval.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileWrapper {

    @JsonProperty("profiles")
    private List<Profile> profiles;
}
