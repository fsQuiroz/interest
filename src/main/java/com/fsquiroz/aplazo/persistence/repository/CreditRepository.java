package com.fsquiroz.aplazo.persistence.repository;

import com.fsquiroz.aplazo.persistence.entity.Credit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditRepository extends JpaRepository<Credit, Long> {

    Page<Credit> findAllByDeletedIsNull(Pageable pageRequest);
}
