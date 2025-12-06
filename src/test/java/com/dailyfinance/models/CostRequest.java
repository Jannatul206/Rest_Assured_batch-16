package com.dailyfinance.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CostRequest {
    private String itemName;
    private Integer quantity;
    private String amount;
    private String purchaseDate;
    private String month;
    private String remarks;
}
