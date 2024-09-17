package com.redis.RedisRetrieval.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionDetails {

    @JsonProperty("sessionId")
    private String sessionId;

    @JsonProperty("trasactionId")
    private int transactionId;

    @JsonProperty("status")
    private int status;

    @JsonProperty("master")
    private int master;

    @JsonProperty("recordId")
    private int recordId;

    @JsonProperty("requestTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH-mm-ss")
    private String requestTime;

    @JsonProperty("operation")
    private int operation;

    @JsonProperty("requestType")
    private int requestType;

    @JsonProperty("subsId")
    private int subsId;

    @JsonProperty("counterRefId")
    private String counterRefId;

    @JsonProperty("totalDebitCounter")
    private String totalDebitCounter;

    @JsonProperty("totalReserveCounter")
    private String totalReserveCounter;

    @JsonProperty("workingNodeId")
    private int workingNodeId;

    @JsonProperty("encodedBalances")
    private String encodedBalances;

    @JsonProperty("encodedCounters")
    private String encodedCounters;

    @JsonProperty("jsonDetails")
    private JsonDetails jsonDetails;
}

