package com.fsquiroz.aplazo.service.payment;

import com.fsquiroz.aplazo.persistence.entity.Credit;
import com.fsquiroz.aplazo.persistence.entity.Payment;
import com.fsquiroz.aplazo.persistence.repository.PaymentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    private PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Page<Payment> list(Credit credit, Pageable pageRequest) {
        return paymentRepository.findAllByCreditAndDeletedIsNull(credit, pageRequest);
    }
}
