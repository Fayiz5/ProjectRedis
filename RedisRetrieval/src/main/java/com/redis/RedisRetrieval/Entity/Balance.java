package com.redis.RedisRetrieval.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Balance {

    @JsonProperty("bucketId")
    private int bucketId;

    @JsonProperty("counterRefId")
    private int counterRefId;

    @JsonProperty("counterValue")
    private int counterValue;

    @JsonProperty("requestType")
    private int requestType;

    @JsonProperty("transactionId")
    private int transactionId;

    @JsonProperty("uniqueTransactionId")
    private String uniqueTransactionId;

    @JsonProperty("serviceNumber")
    private String serviceNumber;

    @JsonProperty("serviceDetailId")
    private int serviceDetailId;

    @JsonProperty("packId")
    private int packId;

    @JsonProperty("smartTag")
    private String smartTag;
}
