package com.redis.RedisRetrieval.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @JsonProperty("cid")
    private int cid;

    @JsonProperty("cType")
    private int cType;

    @JsonProperty("indvlId")
    private int indvlId;

    @JsonProperty("disability")
    private int disability;

    @JsonProperty("vip")
    private int vip;

    @JsonProperty("creationDt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date creationDt;

    @JsonProperty("registeredDt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date registeredDt;

    @JsonProperty("activationDt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date activationDt;

    @JsonProperty("deactivationDt")
    @JsonInclude(JsonInclude.Include.NON_NULL) // Skip null values during serialization
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date deactivationDt;

    @JsonProperty("status")
    private int status;

    @JsonProperty("extCustId")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String extCustId;
}
