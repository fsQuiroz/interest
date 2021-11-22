package com.fsquiroz.aplazo.mapper;

import com.fsquiroz.aplazo.api.credit.CreditDTO;
import com.fsquiroz.aplazo.persistence.entity.Credit;
import org.springframework.stereotype.Service;

@Service
public class CreditMapper extends Mapper<Credit, CreditDTO> {

    @Override
    public CreditDTO map(Credit e) {
        return e == null ? null : CreditDTO.builder()
                .id(e.getId())
                .created(e.getCreated())
                .updated(e.getUpdated())
                .deleted(e.getDeleted())
                .amount(e.getAmount())
                .terms(e.getTerms())
                .rate(e.getRate())
                .build();
    }
}
