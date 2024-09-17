package com.redis.RedisRetrieval.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redis.RedisRetrieval.Entity.Profiles;
import com.redis.RedisRetrieval.Exceptions.DataNotFoundException;
import com.redis.RedisRetrieval.Exceptions.DataProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ProfilesService {
    @Autowired
    private RedisService redisService;

    @Autowired
    private ObjectMapper objectMapper;
    private static final Logger logger = LogManager.getLogger(ProfilesService.class);

    public Profiles getProfilesData(String key) {
        String jsonString = redisService.getKey(key);

        if (jsonString == null) {
            throw new DataNotFoundException("Data not found for key: " + key);
        }

        try {
            return objectMapper.readValue(jsonString, Profiles.class);
        } catch (JsonProcessingException e) {
            throw new DataProcessingException("Failed to process JSON for key: " + key, e);
        }
    }

    public boolean storeProfilesInfo(String key, List<Profiles> ProfilesInfo) {
        if (key == null || ProfilesInfo == null) {
            logger.error("Key or ProfilesInfo is null.");
            return false;
        }

        try {

            String jsonValue = objectMapper.writeValueAsString(ProfilesInfo);
            return redisService.setKey(key, jsonValue);
        } catch (JsonProcessingException e) {
            logger.error("Exception occurred while processing JSON for storing Profiles info: " + e.toString());
        } catch (Exception e) {
            logger.error("Exception occurred while storing Profiles info: " + e.toString());
        }
        return false;
    }

    public boolean updateProfiles(String key, List<Profiles> updatedInfo) {
        if (redisService.getKey(key) == null || updatedInfo ==null) {
            logger.error("No data found for key: " + key);
            return false;
        }
        return storeProfilesInfo(key, updatedInfo);
    }

    public boolean deletestoreProfilesInfo(String key) {
        if (redisService.getKey(key) == null) {
            logger.error("No data found for key: " + key);
            return false;
        }
        return redisService.deleteKey(key);
    }

    public List<Profiles> getAllSubssByPattern() {

        String pattern = "*SUBS*";
        List<Profiles> profiles = new ArrayList<>();
        try {
            Set<String> keys= redisService.RetriveKeys(pattern);
            for (String key : keys) {
                String jsonString = redisService.getKey(key);
                if (jsonString != null) {
                    try {
                        Profiles ProfilesInfo = objectMapper.readValue(jsonString, Profiles.class);
                        profiles.add(ProfilesInfo);
                    } catch (JsonProcessingException e) {
                        logger.error("Failed to process JSON for key: " + key, e);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception occurred while retrieving profiles by pattern: " + e.toString());
        }
        return profiles;
    }

    public List<Profiles> sortProfiles(String sortBy) {
        List<Profiles> profiles = getAllSubssByPattern(); // Fetch all profiles

        return profiles.stream()
                .sorted(getComparator(sortBy))
                .collect(Collectors.toList());
    }
    private Comparator<Profiles> getComparator(String sortBy) {
        switch (sortBy.toLowerCase()) {

            case "id":
                return Comparator.comparing(Profiles::getSid);
            case "servicenumber":
                return Comparator.comparing(Profiles::getServiceNumber);


            default:
                throw new IllegalArgumentException("Invalid field for sorting: " + sortBy);
        }
    }

}
