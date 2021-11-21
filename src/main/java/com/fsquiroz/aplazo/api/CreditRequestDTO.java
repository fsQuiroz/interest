package com.fsquiroz.aplazo.api;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "CreditRequest")
public class CreditRequestDTO {

    private BigDecimal amount;

    private BigDecimal rate;

    private Integer terms;
}
