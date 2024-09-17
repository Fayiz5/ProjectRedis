package com.redis.RedisRetrieval.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionMast {

    @JsonProperty("sessionId")
    private String sessionId;

    @JsonProperty("serviceNumber")
    private String serviceNumber;

    @JsonProperty("requestType")
    private int requestType;

    @JsonProperty("operation")
    private int operation;

    @JsonProperty("serviceId")
    private int serviceId;

    @JsonProperty("transactionId")
    private int transactionId;

    @JsonProperty("status")
    private int status;

    @JsonProperty("requestTime")
    private String requestTime;

    @JsonProperty("previousTxId")
    private int previousTxId;

    @JsonProperty("previousOperation")
    private int previousOperation;

    @JsonProperty("subsId")
    private int subsId;

    @JsonProperty("sharedSuffix")
    private String sharedSuffix;

    @JsonProperty("nodeId")
    private int nodeId;

    @JsonProperty("uniqueTxnId")
    private String uniqueTxnId;

    @JsonProperty("revertTxnId")
    private String revertTxnId;

    @JsonProperty("instanceId")
    private int instanceId;

    @JsonProperty("configuredNodeId")
    private int configuredNodeId;

    @JsonProperty("workingNodeId")
    private int workingNodeId;

    @JsonProperty("haStatus")
    private int haStatus;

    @JsonProperty("subscriptionType")
    private int subscriptionType;

    @JsonProperty("sessionDetail")
    private List<SessionDetail> sessionDetail;

}
