package com.fsquiroz.aplazo.service.credit;

import com.fsquiroz.aplazo.api.CreditRequestDTO;
import com.fsquiroz.aplazo.persistence.entity.Credit;
import com.fsquiroz.aplazo.persistence.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CreditService {

    Page<Credit> list(Pageable pageRequest);

    List<Payment> calculate(CreditRequestDTO request);

    Credit get(Long id);
}
