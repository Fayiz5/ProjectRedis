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
public class Profiles {

    @JsonProperty("sid")
    private int sid;

    @JsonProperty("serviceNumber")
    private String serviceNumber;

    @JsonProperty("accountId")
    private int accountId;

    @JsonProperty("langId")
    private int langId;

    @JsonProperty("validityDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date validityDate;

    @JsonProperty("expiryDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date expiryDate;

    @JsonProperty("subsDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date subsDate;

    @JsonProperty("status")
    private int status;

    @JsonProperty("prevState")
    private int prevState;

    @JsonProperty("stateChgDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date stateChgDate;

    @JsonProperty("supervsnDate")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date supervsnDate;

    @JsonProperty("tempBlocked")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer tempBlocked;

    @JsonProperty("state")
    private int state;

    @JsonProperty("packId")
    private int packId;

    @JsonProperty("cos")
    private int cos;

    @JsonProperty("srvId")
    private int srvId;

    @JsonProperty("provServices")
    private String provServices;

    @JsonProperty("nodeId")
    private int nodeId;

    @JsonProperty("notiId")
    private int notiId;

    @JsonProperty("notiCnt")
    private int notiCnt;

    @JsonProperty("notiSegment")
    private int notiSegment;

    @JsonProperty("indvlId")
    private int indvlId;

    @JsonProperty("indvlFlag")
    private int indvlFlag;

    @JsonProperty("firstRecharge")
    private int firstRecharge;

    @JsonProperty("firstCall")
    private int firstCall;

    @JsonProperty("testFlag")
    private int testFlag;

    @JsonProperty("subsType")
    private int subsType;

    @JsonProperty("profileType")
    private int profileType;

    @JsonProperty("activeProfile")
    private int activeProfile;

    @JsonProperty("RoamingFlag")
    private int roamingFlag;

    @JsonProperty("BillShockFlag")
    private int billShockFlag;
}

