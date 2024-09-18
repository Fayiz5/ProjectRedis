package com.redis.RedisRetrieval.Service;

import com.redis.RedisRetrieval.Entity.IndividualInfo;
import com.redis.RedisRetrieval.Exceptions.DataNotFoundException;
import com.redis.RedisRetrieval.Exceptions.DataProcessingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.redis.RedisRetrieval.Service.RedisServices.RedisService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class IndividualInfoService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private ObjectMapper objectMapper;
    private static final Logger logger = LogManager.getLogger(IndividualInfoService.class);

    public IndividualInfo getIndividualData(String key) {
        String jsonString = redisService.getKey(key);

        if (jsonString == null) {
            throw new DataNotFoundException("Data not found for key: " + key);
        }

        try {
            return objectMapper.readValue(jsonString, IndividualInfo.class);
        } catch (JsonProcessingException e) {
            throw new DataProcessingException("Failed to process JSON for key: " + key, e);
        }
    }

    public boolean storeIndividualInfo(String key, IndividualInfo individualInfo) {
        try {
            if (redisService.getKey(key) == null || individualInfo ==null) {
                logger.error("No data found for key: " + key);
                return false;
            }
            String jsonValue = objectMapper.writeValueAsString(individualInfo);
            return redisService.setKey(key, jsonValue);
        } catch (JsonProcessingException e) {
            //logger.error("Exception occurred while storing individual info: " + e.toString());
            e.printStackTrace();
        }
        return false;
    }
    public boolean updateIndividualInfo(String key, IndividualInfo updatedInfo) {
        if (redisService.getKey(key) == null || updatedInfo ==null) {
            logger.error("No data found for key: " + key);
            return false;
        }
        return storeIndividualInfo(key, updatedInfo);
    }

    public boolean deleteIndividualInfo(String key) {
        if (redisService.getKey(key) == null) {
            logger.error("No data found for key: " + key);
            return false;
        }
        return redisService.deleteKey(key);
    }

    public List<IndividualInfo> getAllProfilesByPattern() {

        String pattern = "*PI*";
        List<IndividualInfo> profiles = new ArrayList<>();
        try {
        Set<String> keys= redisService.RetriveKeys(pattern);
            for (String key : keys) {
                String jsonString = redisService.getKey(key);
                if (jsonString != null) {
                    try {
                        IndividualInfo individualInfo = objectMapper.readValue(jsonString, IndividualInfo.class);
                        profiles.add(individualInfo);
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

    public List<IndividualInfo> sortProfiles(String sortBy) {
        List<IndividualInfo> profiles = getAllProfilesByPattern(); // Fetch all profiles

        return profiles.stream()
                .sorted(getComparator(sortBy))
                .collect(Collectors.toList());
    }
    private Comparator<IndividualInfo> getComparator(String sortBy) {
        switch (sortBy.toLowerCase()) {

            case "Id":
                return Comparator.comparing(IndividualInfo::getIndvlId);
            case "dob":
                return Comparator.comparing(IndividualInfo::getDob);
            case "name":
                return Comparator.comparing(IndividualInfo::getFName).thenComparing(IndividualInfo::getLName);
            case "zipcode":
                return Comparator.comparing(IndividualInfo::getZipcode);
            default:
                throw new IllegalArgumentException("Invalid field for sorting: " + sortBy);
        }
    }

}
