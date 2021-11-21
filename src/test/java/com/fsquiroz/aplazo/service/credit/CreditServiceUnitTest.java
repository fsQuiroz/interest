package com.fsquiroz.aplazo.service.credit;

import com.fsquiroz.aplazo.api.CreditRequestDTO;
import com.fsquiroz.aplazo.exception.NotFoundException;
import com.fsquiroz.aplazo.persistence.entity.Credit;
import com.fsquiroz.aplazo.persistence.entity.Payment;
import com.fsquiroz.aplazo.persistence.repository.CreditRepository;
import com.fsquiroz.aplazo.persistence.repository.PaymentRepository;
import com.fsquiroz.aplazo.service.validate.ValidateServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.data.Offset;
import org.assertj.core.data.TemporalUnitWithinOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.ReturnsArgumentAt;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
public class CreditServiceUnitTest {

    private int termDurationInDays = 7;

    private int minTerms = 4;

    private int maxTerms = 52;

    private BigDecimal minRate = new BigDecimal("1");

    private BigDecimal maxRate = new BigDecimal("100");

    private BigDecimal minAmount = new BigDecimal("1.00");

    private BigDecimal maxAmount = new BigDecimal("999999.00");

    @MockBean
    private ValidateServiceImpl validate;

    @MockBean
    private CreditRepository creditRepository;

    @MockBean
    private PaymentRepository paymentRepository;

    @Captor
    private ArgumentCaptor<Pageable> pageRequestCaptor;

    @Captor
    private ArgumentCaptor<Credit> creditCaptor;

    private CreditServiceImpl service;

    @BeforeEach
    public void setup() {
        service = new CreditServiceImpl(termDurationInDays, minTerms, maxTerms, minRate, maxRate, minAmount, maxAmount, validate, creditRepository, paymentRepository);
    }

    @Test
    public void listCredits() {
        log.info("Test list credits");

        PageRequest pageRequest = PageRequest.of(1, 20);
        Credit c1 = new Credit();
        c1.setId(1L);
        Credit c2 = new Credit();
        c2.setId(2L);

        List<Credit> credits = Arrays.asList(c1, c2);
        Page<Credit> page = new PageImpl<>(credits, pageRequest, 2L);

        Mockito.doReturn(page).when(creditRepository).findAllByDeletedIsNull(any());

        Page<Credit> response = service.list(pageRequest);

        Mockito.verify(creditRepository, Mockito.times(1))
                .findAllByDeletedIsNull(pageRequestCaptor.capture());

        assertThat(response)
                .isNotNull()
                .hasSize(2)
                .containsExactly(c1, c2);

        Pageable capturedPageRequest = pageRequestCaptor.getValue();

        assertThat(capturedPageRequest)
                .isNotNull()
                .hasFieldOrPropertyWithValue("page", 1)
                .hasFieldOrPropertyWithValue("size", 20);
    }

    @Test
    public void getCreditById() {
        log.info("Test get credit by id");

        Long id = 1L;

        Credit c = new Credit();
        c.setId(1L);

        Mockito.doReturn(Optional.of(c)).when(creditRepository).findById(any());

        Credit response = service.get(id);

        assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    public void getCreditByMissingId() {
        log.info("Test get credit by missing id");

        Long id = 1L;

        Mockito.doReturn(Optional.empty()).when(creditRepository).findById(any());

        Exception response = null;

        try {
            service.get(id);
        } catch (Exception e) {
            response = e;
        }

        assertThat(response)
                .isExactlyInstanceOf(NotFoundException.class)
                .hasMessage("Entity not found")
                .hasFieldOrPropertyWithValue("meta.id", 1L);
    }

    @Test
    public void calculateCreditMinPayments() {
        log.info("Test calculate credit with min payments");

        CreditRequestDTO request = CreditRequestDTO.builder()
                .amount(new BigDecimal("1000.00"))
                .rate(new BigDecimal("10.00"))
                .terms(4)
                .build();

        Mockito.when(creditRepository.save(any())).then(inv -> {
            Credit c = inv.getArgument(0);
            c.setId(1L);
            return c;
        });

        List<Payment> response = service.calculate(request);

        Mockito.verify(creditRepository, Mockito.times(1)).save(creditCaptor.capture());

        Credit capturedCredit = creditCaptor.getValue();

        assertThat(capturedCredit)
                .isNotNull();
        assertThat(capturedCredit.getCreated())
                .isCloseTo(Instant.now(), new TemporalUnitWithinOffset(200, ChronoUnit.MILLIS));
        assertThat(capturedCredit.getAmount())
                .isCloseTo(new BigDecimal("1000.00"), Offset.offset(BigDecimal.valueOf(0.005)));
        assertThat(capturedCredit.getRate())
                .isCloseTo(new BigDecimal("10.00"), Offset.offset(BigDecimal.valueOf(0.005)));
        assertThat(capturedCredit.getTerms())
                .isEqualTo(4);

        assertThat(response)
                .isNotNull()
                .hasSize(4);

        assertThat(response.get(0))
                .hasFieldOrProperty("created")
                .hasFieldOrPropertyWithValue("credit", capturedCredit)
                .hasFieldOrPropertyWithValue("paymentNumber", 1);
        assertThat(response.get(0).getPaymentDate())
                .isCloseTo(Instant.now().truncatedTo(ChronoUnit.DAYS).plus(7, ChronoUnit.DAYS), new TemporalUnitWithinOffset(200, ChronoUnit.MILLIS));
        assertThat(response.get(0).getAmount())
                .isCloseTo(new BigDecimal("275.00"), Offset.offset(BigDecimal.valueOf(0.005)));

        assertThat(response.get(1))
                .hasFieldOrProperty("created")
                .hasFieldOrPropertyWithValue("credit", capturedCredit)
                .hasFieldOrPropertyWithValue("paymentNumber", 2);
        assertThat(response.get(1).getPaymentDate())
                .isCloseTo(Instant.now().truncatedTo(ChronoUnit.DAYS).plus(14, ChronoUnit.DAYS), new TemporalUnitWithinOffset(200, ChronoUnit.MILLIS));
        assertThat(response.get(1).getAmount())
                .isCloseTo(new BigDecimal("275.00"), Offset.offset(BigDecimal.valueOf(0.005)));

        assertThat(response.get(2))
                .hasFieldOrProperty("created")
                .hasFieldOrPropertyWithValue("credit", capturedCredit)
                .hasFieldOrPropertyWithValue("paymentNumber", 3);
        assertThat(response.get(2).getPaymentDate())
                .isCloseTo(Instant.now().truncatedTo(ChronoUnit.DAYS).plus(21, ChronoUnit.DAYS), new TemporalUnitWithinOffset(200, ChronoUnit.MILLIS));
        assertThat(response.get(2).getAmount())
                .isCloseTo(new BigDecimal("275.00"), Offset.offset(BigDecimal.valueOf(0.005)));

        assertThat(response.get(3))
                .hasFieldOrProperty("created")
                .hasFieldOrPropertyWithValue("credit", capturedCredit)
                .hasFieldOrPropertyWithValue("paymentNumber", 4);
        assertThat(response.get(3).getPaymentDate())
                .isCloseTo(Instant.now().truncatedTo(ChronoUnit.DAYS).plus(28, ChronoUnit.DAYS), new TemporalUnitWithinOffset(200, ChronoUnit.MILLIS));
        assertThat(response.get(3).getAmount())
                .isCloseTo(new BigDecimal("275.00"), Offset.offset(BigDecimal.valueOf(0.005)));
    }

    @Test
    public void calculateCreditUntidyAmount() {
        log.info("Test calculate credit with untidy amount");

        CreditRequestDTO request = CreditRequestDTO.builder()
                .amount(new BigDecimal("777.00"))
                .rate(new BigDecimal("21.00"))
                .terms(5)
                .build();

        Mockito.when(creditRepository.save(any())).then(inv -> {
            Credit c = inv.getArgument(0);
            c.setId(1L);
            return c;
        });

        List<Payment> response = service.calculate(request);

        Mockito.verify(creditRepository, Mockito.times(1)).save(creditCaptor.capture());

        Credit capturedCredit = creditCaptor.getValue();

        assertThat(capturedCredit)
                .isNotNull();
        assertThat(capturedCredit.getCreated())
                .isCloseTo(Instant.now(), new TemporalUnitWithinOffset(200, ChronoUnit.MILLIS));
        assertThat(capturedCredit.getAmount())
                .isCloseTo(new BigDecimal("777.00"), Offset.offset(BigDecimal.valueOf(0.005)));
        assertThat(capturedCredit.getRate())
                .isCloseTo(new BigDecimal("21.00"), Offset.offset(BigDecimal.valueOf(0.005)));
        assertThat(capturedCredit.getTerms())
                .isEqualTo(5);

        assertThat(response)
                .isNotNull()
                .hasSize(5);

        assertThat(response.get(0))
                .hasFieldOrProperty("created")
                .hasFieldOrPropertyWithValue("credit", capturedCredit)
                .hasFieldOrPropertyWithValue("paymentNumber", 1);
        assertThat(response.get(0).getPaymentDate())
                .isCloseTo(Instant.now().truncatedTo(ChronoUnit.DAYS).plus(7, ChronoUnit.DAYS), new TemporalUnitWithinOffset(200, ChronoUnit.MILLIS));
        assertThat(response.get(0).getAmount())
                .isCloseTo(new BigDecimal("188.03"), Offset.offset(BigDecimal.valueOf(0.005)));

        assertThat(response.get(1))
                .hasFieldOrProperty("created")
                .hasFieldOrPropertyWithValue("credit", capturedCredit)
                .hasFieldOrPropertyWithValue("paymentNumber", 2);
        assertThat(response.get(1).getPaymentDate())
                .isCloseTo(Instant.now().truncatedTo(ChronoUnit.DAYS).plus(14, ChronoUnit.DAYS), new TemporalUnitWithinOffset(200, ChronoUnit.MILLIS));
        assertThat(response.get(1).getAmount())
                .isCloseTo(new BigDecimal("188.03"), Offset.offset(BigDecimal.valueOf(0.005)));

        assertThat(response.get(2))
                .hasFieldOrProperty("created")
                .hasFieldOrPropertyWithValue("credit", capturedCredit)
                .hasFieldOrPropertyWithValue("paymentNumber", 3);
        assertThat(response.get(2).getPaymentDate())
                .isCloseTo(Instant.now().truncatedTo(ChronoUnit.DAYS).plus(21, ChronoUnit.DAYS), new TemporalUnitWithinOffset(200, ChronoUnit.MILLIS));
        assertThat(response.get(2).getAmount())
                .isCloseTo(new BigDecimal("188.03"), Offset.offset(BigDecimal.valueOf(0.005)));

        assertThat(response.get(3))
                .hasFieldOrProperty("created")
                .hasFieldOrPropertyWithValue("credit", capturedCredit)
                .hasFieldOrPropertyWithValue("paymentNumber", 4);
        assertThat(response.get(3).getPaymentDate())
                .isCloseTo(Instant.now().truncatedTo(ChronoUnit.DAYS).plus(28, ChronoUnit.DAYS), new TemporalUnitWithinOffset(200, ChronoUnit.MILLIS));
        assertThat(response.get(3).getAmount())
                .isCloseTo(new BigDecimal("188.03"), Offset.offset(BigDecimal.valueOf(0.005)));

        assertThat(response.get(4))
                .hasFieldOrProperty("created")
                .hasFieldOrPropertyWithValue("credit", capturedCredit)
                .hasFieldOrPropertyWithValue("paymentNumber", 5);
        assertThat(response.get(4).getPaymentDate())
                .isCloseTo(Instant.now().truncatedTo(ChronoUnit.DAYS).plus(35, ChronoUnit.DAYS), new TemporalUnitWithinOffset(200, ChronoUnit.MILLIS));
        assertThat(response.get(4).getAmount())
                .isCloseTo(new BigDecimal("188.03"), Offset.offset(BigDecimal.valueOf(0.005)));
    }
}
