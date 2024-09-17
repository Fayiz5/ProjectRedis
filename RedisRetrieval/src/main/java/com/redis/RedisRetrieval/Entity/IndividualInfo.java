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
public class IndividualInfo {

    @JsonProperty("indvlId")
    private int indvlId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("fName")
    private String fName;

    @JsonProperty("mName")
    private String mName;

    @JsonProperty("lName")
    private String lName;

    @JsonProperty("business")
    private String business;

    @JsonProperty("dob")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date dob;

    @JsonProperty("nationality")
    private int nationality;

    @JsonProperty("occupation")
    private String occupation;

    @JsonProperty("addrId")
    private int addrId;

    @JsonProperty("gender")
    private int gender;

    @JsonProperty("contact")
    private String contact;

    @JsonProperty("altContact")
    private String altContact;

    @JsonProperty("email")
    private String email;

    @JsonProperty("altEmail")
    private String altEmail;

    @JsonProperty("docId")
    private int docId;

    @JsonProperty("docNum")
    private String docNum;

    @JsonProperty("city")
    private String city;

    @JsonProperty("state")
    private String state;

    @JsonProperty("countryId")
    private int countryId;

    @JsonProperty("country")
    private String country;

    @JsonProperty("zipcode")
    private String zipcode;

    @JsonProperty("addrType")
    private int addrType;
}
