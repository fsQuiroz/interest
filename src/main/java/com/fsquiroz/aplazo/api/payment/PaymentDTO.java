package com.fsquiroz.aplazo.api.payment;

import com.fsquiroz.aplazo.api.credit.CreditDTO;
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
@ApiModel(value = "Payment")
public class PaymentDTO {

    @ApiModelProperty(example = "1")
    private Long id;

    @ApiModelProperty(hidden = true)
    private Instant created;

    @ApiModelProperty(hidden = true)
    private Instant updated;

    @ApiModelProperty(hidden = true)
    private Instant deleted;

    private CreditDTO credit;

    @ApiModelProperty(example = "1")
    private int paymentNumber;

    @ApiModelProperty(example = "2021-12-01T00:00:00.000")
    private Instant paymentDate;

    @ApiModelProperty(example = "549.99")
    private BigDecimal amount;
}
