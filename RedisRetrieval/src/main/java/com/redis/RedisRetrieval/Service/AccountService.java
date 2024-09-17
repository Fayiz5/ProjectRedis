package com.redis.RedisRetrieval.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redis.RedisRetrieval.Entity.Account;
import com.redis.RedisRetrieval.Entity.IndividualInfo;
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

public class AccountService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private ObjectMapper objectMapper;
    private static final Logger logger = LogManager.getLogger(AccountService.class);

    public Account getAccountData(String key) {
        String jsonString = redisService.getKey(key);

        if (jsonString == null) {
            throw new DataNotFoundException("Data not found for key: " + key);
        }

        try {
            return objectMapper.readValue(jsonString, Account.class);
        } catch (JsonProcessingException e) {
            throw new DataProcessingException("Failed to process JSON for key: " + key, e);
        }
    }

    public boolean storeAccountInfo(String key, Account accountInfo) {
        try {
            if (redisService.getKey(key) == null || accountInfo ==null) {
                logger.error("No data found for key: " + key);
                return false;
            }
            String jsonValue = objectMapper.writeValueAsString(accountInfo);
            return redisService.setKey(key, jsonValue);
        } catch (JsonProcessingException e) {
            //logger.error("Exception occurred while storing individual info: " + e.toString());
            e.printStackTrace();
        }
        return false;
    }
    public boolean updateAccountInfo(String key, Account updatedInfo) {
        if (redisService.getKey(key) == null || updatedInfo ==null) {
            logger.error("No data found for key: " + key);
            return false;
        }
        return storeAccountInfo(key, updatedInfo);
    }

    public boolean deleteAccountInfo(String key) {
        if (redisService.getKey(key) == null) {
            logger.error("No data found for key: " + key);
            return false;
        }
        return redisService.deleteKey(key);
    }

    public List<Account> getAllAccountsByPattern() {

        String pattern = "*ACT*";
        List<Account> profiles = new ArrayList<>();
        try {
            Set<String> keys= redisService.RetriveKeys(pattern);
            for (String key : keys) {
                String jsonString = redisService.getKey(key);
                if (jsonString != null) {
                    try {
                        Account account = objectMapper.readValue(jsonString, Account.class);
                        profiles.add(account);
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

    public List<Account> sortProfiles(String sortBy) {
        List<Account> accounts = getAllAccountsByPattern(); // Fetch all profiles

        return accounts.stream()
                .sorted(getComparator(sortBy))
                .collect(Collectors.toList());
    }
    private Comparator<Account> getComparator(String sortBy) {
        switch (sortBy.toLowerCase()) {

            case "id":
                return Comparator.comparing(Account::getActId);
            case "number":
                return Comparator.comparing(Account::getActNum);
            case "name":
                return Comparator.comparing(Account::getActName);

            default:
                throw new IllegalArgumentException("Invalid field for sorting: " + sortBy);
        }
    }

}
