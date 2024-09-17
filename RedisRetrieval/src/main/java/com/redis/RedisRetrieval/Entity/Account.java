package com.redis.RedisRetrieval.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @JsonProperty("actId")
    private int actId;

    @JsonProperty("custId")
    private int custId;

    @JsonProperty("actType")
    private int actType;

    @JsonProperty("status")
    private int status;

    @JsonProperty("contact")
    private String contact;

    @JsonProperty("priorityCust")
    private int priorityCust;

    @JsonProperty("multiService")
    private int multiService;

    @JsonProperty("creditLimit")
    private long creditLimit;

    @JsonProperty("createDt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date createDt;

    @JsonProperty("actNum")
    private String actNum;

    @JsonProperty("parentActId")
    private int parentActId;

    @JsonProperty("extActId")
    private String extActId;

    @JsonProperty("actName")
    private String actName;

    @JsonProperty("costCenter")
    private String costCenter;

    @JsonProperty("parentStatus")
    private int parentStatus;
}
