package com.fsquiroz.aplazo.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import java.math.BigDecimal;

@javax.persistence.Entity
@Getter
@Setter
@ToString(callSuper = true)
public class Credit extends Entity {

    @Column(scale = 2)
    private BigDecimal amount;

    private int terms;

    @Column(scale = 2)
    private BigDecimal rate;
}
