package com.redis.RedisRetrieval.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redis.RedisRetrieval.Entity.Balance;
import com.redis.RedisRetrieval.Entity.SessionData;
import com.redis.RedisRetrieval.Entity.SessionDetails;
import com.redis.RedisRetrieval.Entity.SessionMast;
import com.redis.RedisRetrieval.Service.RedisServices.RedisService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private ObjectMapper objectMapper;
    private static final Logger logger = LogManager.getLogger(SessionService.class);


    public SessionData getSessionDataAsObject(String key) {
        try {
            // Fetch the JSON data from Redis using the key
            String jsonString = redisService.getKey(key);

            // Check if data exists for the provided key
            if (jsonString == null) {
                logger.error("No data found for key: " + key);
                return null;
            }



            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            SessionData sessionData = objectMapper.readValue(jsonString, SessionData.class);

            for (SessionMast sessionMaster : sessionData.getSessionMast()) {
                for (SessionDetails sessionDetail : sessionMaster.getSessionDetail()) {
                    String jsonDetails = sessionDetail.getJsonDetails();
                    if (jsonDetails != null && !jsonDetails.isEmpty()) {

                        JsonNode jsonNode = objectMapper.readTree(jsonDetails);

                        if (jsonNode.has("balance")) {
                            JsonNode balanceNode = jsonNode.get("balance");
                            List<Balance> balances = objectMapper.readValue(
                                    balanceNode.toString(),
                                    new TypeReference<List<Balance>>() {}
                            );
                            sessionDetail.setBalances(balances);
                        }
                    }
                }
            }

            return sessionData;
        } catch (JsonProcessingException e) {
            logger.error("Error processing JSON data for key: " + key, e);
            return null;
        }
    }


}
