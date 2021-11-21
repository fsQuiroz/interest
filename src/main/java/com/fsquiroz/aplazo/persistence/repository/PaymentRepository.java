package com.fsquiroz.aplazo.persistence.repository;

import com.fsquiroz.aplazo.persistence.entity.Credit;
import com.fsquiroz.aplazo.persistence.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Page<Payment> findAllByCreditAndDeletedIsNull(Credit credit, Pageable pageRequest);
}
