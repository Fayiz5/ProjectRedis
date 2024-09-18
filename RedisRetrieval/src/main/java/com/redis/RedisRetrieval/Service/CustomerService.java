package com.redis.RedisRetrieval.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redis.RedisRetrieval.Entity.Customer;
import com.redis.RedisRetrieval.Entity.IndividualInfo;
import com.redis.RedisRetrieval.Exceptions.DataNotFoundException;
import com.redis.RedisRetrieval.Exceptions.DataProcessingException;
import com.redis.RedisRetrieval.Service.RedisServices.RedisService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomerService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private ObjectMapper objectMapper;
    private static final Logger logger = LogManager.getLogger(CustomerService.class);

    public Customer getCustomerData(String key) {
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

    public boolean storeCustomerInfo(String key, Customer customer) {
        try {
            if (redisService.getKey(key) == null || customer ==null) {
                logger.error("No data found for key: " + key);
                return false;
            }
            String jsonValue = objectMapper.writeValueAsString(customer);
            return redisService.setKey(key, jsonValue);
        } catch (JsonProcessingException e) {
            //logger.error("Exception occurred while storing individual info: " + e.toString());
            e.printStackTrace();
        }
        return false;
    }
    public boolean updatecustomerInfo(String key, Customer updatedInfo) {
        if (redisService.getKey(key) == null || updatedInfo ==null) {
            logger.error("No data found for key: " + key);
            return false;
        }
        return storeCustomerInfo(key, updatedInfo);
    }

    public boolean deleteIndividualInfo(String key) {
        if (redisService.getKey(key) == null) {
            logger.error("No data found for key: " + key);
            return false;
        }
        return redisService.deleteKey(key);
    }

    public List<Customer> getAllProfilesByPattern() {

        String pattern = "*CUST*";
        List<Customer> profiles = new ArrayList<>();
        try {
            Set<String> keys= redisService.RetriveKeys(pattern);
            for (String key : keys) {
                String jsonString = redisService.getKey(key);
                if (jsonString != null) {
                    try {
                        Customer customer = objectMapper.readValue(jsonString, Customer.class);
                        profiles.add(customer);
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

    public List<Customer> sortProfiles(String sortBy) {
        List<Customer> profiles = getAllProfilesByPattern(); // Fetch all profiles

        return profiles.stream()
                .sorted(getComparator(sortBy))
                .collect(Collectors.toList());
    }
    private Comparator<Customer> getComparator(String sortBy) {
        switch (sortBy.toLowerCase()) {

            case "id":
                return Comparator.comparing(Customer::getCid);

            case "extid":
                return  Comparator.comparing(Customer::getExtCustId);

            default:
                throw new IllegalArgumentException("Invalid field for sorting: " + sortBy);
        }
    }

}
