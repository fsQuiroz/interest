package com.fsquiroz.aplazo.api.credit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Credit")
public class CreditDTO {

    @ApiModelProperty(example = "1")
    private Long id;

    @ApiModelProperty(hidden = true)
    private Instant created;

    @ApiModelProperty(hidden = true)
    private Instant updated;

    @ApiModelProperty(hidden = true)
    private Instant deleted;

    @ApiModelProperty(example = "1999.99")
    private BigDecimal amount;

    @ApiModelProperty(example = "4")
    private int terms;

    @ApiModelProperty(example = "15.00")
    private BigDecimal rate;
}
