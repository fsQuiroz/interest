package com.fsquiroz.aplazo.mapper;

import com.fsquiroz.aplazo.api.payment.PaymentDTO;
import com.fsquiroz.aplazo.persistence.entity.Payment;
import org.springframework.stereotype.Service;

@Service
public class PaymentMapper extends Mapper<Payment, PaymentDTO> {

    @Override
    public PaymentDTO map(Payment e) {
        return e == null ? null : PaymentDTO.builder()
                .id(e.getId())
                .created(e.getCreated())
                .updated(e.getUpdated())
                .deleted(e.getDeleted())
                .paymentNumber(e.getPaymentNumber())
                .paymentDate(e.getPaymentDate())
                .amount(e.getAmount())
                .build();
    }
}
