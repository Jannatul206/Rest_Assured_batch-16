package com.dailyfinance.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    @JsonProperty("_id")
    private String id;
    
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private String address;
    private String gender;
    private Boolean termsAccepted;
    private String role;
    private String profileImage;
    private String resetPasswordToken;
    private String resetPasswordExpire;
    private String createdAt;
    private String updatedAt;
}
