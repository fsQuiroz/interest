package com.fsquiroz.aplazo.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.Instant;

@javax.persistence.Entity
@Getter
@Setter
@ToString(callSuper = true)
public class Payment extends Entity {

    @ManyToOne
    @JoinColumn
    private Credit credit;

    private int paymentNumber;

    private Instant paymentDate;

    @Column(scale = 3)
    private BigDecimal amount;
}
