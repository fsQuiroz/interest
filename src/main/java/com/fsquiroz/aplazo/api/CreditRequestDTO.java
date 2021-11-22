package com.fsquiroz.aplazo.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(example = "1999.99")
    private BigDecimal amount;

    @ApiModelProperty(example = "15.00")
    private BigDecimal rate;

    @ApiModelProperty(example = "4")
    private Integer terms;
}
