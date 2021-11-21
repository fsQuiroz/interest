package com.fsquiroz.aplazo.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@javax.persistence.Entity
@Getter
@Setter
@ToString(callSuper = true)
public class Credit extends Entity {

    private BigDecimal amount;

    private int terms;

    private BigDecimal rate;
}
