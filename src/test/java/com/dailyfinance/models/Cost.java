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
public class Cost {
    @JsonProperty("_id")
    private String id;
    
    private String itemName;
    private Integer quantity;
    private String amount;
    private String purchaseDate;
    private String month;
    private String remarks;
    private String userId;
    private String createdAt;
    private String updatedAt;
}
