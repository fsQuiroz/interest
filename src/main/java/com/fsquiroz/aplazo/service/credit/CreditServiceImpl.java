package com.fsquiroz.aplazo.service.credit;

import com.fsquiroz.aplazo.api.CreditRequestDTO;
import com.fsquiroz.aplazo.exception.NotFoundException;
import com.fsquiroz.aplazo.persistence.entity.Credit;
import com.fsquiroz.aplazo.persistence.entity.Payment;
import com.fsquiroz.aplazo.persistence.repository.CreditRepository;
import com.fsquiroz.aplazo.persistence.repository.PaymentRepository;
import com.fsquiroz.aplazo.service.validate.ValidateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class CreditServiceImpl implements CreditService {

    private int termDurationInDays;

    private int minTerms;

    private int maxTerms;

    private BigDecimal minRate;

    private BigDecimal maxRate;

    private BigDecimal minAmount;

    private BigDecimal maxAmount;

    private ValidateService validate;

    private CreditRepository creditRepository;

    private PaymentRepository paymentRepository;

    public CreditServiceImpl(
            @Value("${credit.termDurationInDays:7}") int termDurationInDays,
            @Value("${credit.minTerm:4}") int minTerms,
            @Value("${credit.maxTerm:52}") int maxTerms,
            @Value("${credit.minRate:1}") BigDecimal minRate,
            @Value("${credit.maxRate:100}") BigDecimal maxRate,
            @Value("${credit.minAmount:1.00}") BigDecimal minAmount,
            @Value("${credit.minAmount:999999.00}") BigDecimal maxAmount,
            ValidateService validate,
            CreditRepository creditRepository,
            PaymentRepository paymentRepository
    ) {
        this.termDurationInDays = termDurationInDays;
        this.minTerms = minTerms;
        this.maxTerms = maxTerms;
        this.minRate = minRate;
        this.maxRate = maxRate;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.validate = validate;
        this.creditRepository = creditRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Page<Credit> list(Pageable pageRequest) {
        return creditRepository.findAllByDeletedIsNull(pageRequest);
    }

    @Override
    public List<Payment> calculate(CreditRequestDTO request) {
        validate.isNotNull(request, "body");
        validate.isNotNull(request.getAmount(), "amount");
        validate.isNotNull(request.getRate(), "rate");
        validate.isNotNull(request.getTerms(), "terms");
        validate.hasNoDecimals(request.getRate(), "rate");
        validate.isWithinRangeInclusive(minAmount, maxAmount, request.getAmount(), "amount");
        validate.isWithinRangeExclusive(minRate, maxRate, request.getRate(), "rate");
        validate.isWithinRangeInclusive(minTerms, maxTerms, request.getTerms(), "terms");

        Instant now = Instant.now();
        Instant nowTruncated = now.truncatedTo(ChronoUnit.DAYS);

        Credit credit = new Credit();
        credit.setCreated(now);
        credit.setAmount(request.getAmount());
        credit.setRate(request.getRate());
        credit.setTerms(request.getTerms());
        credit = creditRepository.save(credit);

        List<Payment> payments = new ArrayList<>(request.getTerms());
        BigDecimal paymentAmount = request.getAmount()
                .multiply(BigDecimal.ONE.add(request.getRate().divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP)))
                .divide(BigDecimal.valueOf(request.getTerms()), 2, RoundingMode.HALF_UP);

        for (int i = 0; i < request.getTerms(); i++) {
            Instant paymentDate = nowTruncated.plus((i + 1) * termDurationInDays, ChronoUnit.DAYS);
            Payment payment = new Payment();
            payment.setCreated(now);
            payment.setCredit(credit);
            payment.setPaymentDate(paymentDate);
            payment.setPaymentNumber(i + 1);
            payment.setAmount(paymentAmount);
            payments.add(payment);
        }
        paymentRepository.saveAll(payments);
        return payments;
    }

    @Override
    public Credit get(Long id) {
        validate.isValidId(id);
        return creditRepository.findById(id)
                .orElseThrow(() -> NotFoundException.byId(Credit.class, id));
    }
}
