package com.fsquiroz.aplazo.mapper;

import com.fsquiroz.aplazo.api.credit.CreditDTO;
import com.fsquiroz.aplazo.api.payment.PaymentDTO;
import com.fsquiroz.aplazo.persistence.entity.Credit;
import com.fsquiroz.aplazo.persistence.entity.Payment;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.data.Offset;
import org.assertj.core.data.TemporalUnitWithinOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
public class PaymentMapperUnitTest {

    @MockBean
    private CreditMapper creditMapper;

    private PaymentMapper mapper;

    @BeforeEach
    public void setup() {
        mapper = new PaymentMapper(creditMapper);
    }

    @Test
    public void mapNull() {
        log.info("Test map null");

        Payment payment = null;

        PaymentDTO response = mapper.map(payment);

        assertThat(response)
                .isNull();
    }

    @Test
    public void mapPayment() {
        log.info("Test map payment");

        Credit credit = new Credit();
        credit.setId(101L);

        Payment payment = new Payment();
        payment.setId(99L);
        payment.setCreated(Instant.now());
        payment.setCredit(credit);
        payment.setPaymentNumber(3);
        payment.setPaymentDate(Instant.now());
        payment.setAmount(new BigDecimal("549.99"));

        Mockito.doReturn(CreditDTO.builder().id(101L).build()).when(creditMapper).map(any(Credit.class));

        PaymentDTO response = mapper.map(payment);

        assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("updated", null)
                .hasFieldOrPropertyWithValue("deleted", null)
                .hasFieldOrPropertyWithValue("credit.id", 101L)
                .hasFieldOrPropertyWithValue("paymentNumber", 3);

        assertThat(response.getCreated())
                .isCloseTo(Instant.now(), new TemporalUnitWithinOffset(200, ChronoUnit.MILLIS));

        assertThat(response.getPaymentDate())
                .isCloseTo(Instant.now(), new TemporalUnitWithinOffset(200, ChronoUnit.MILLIS));

        assertThat(response.getAmount())
                .isCloseTo(new BigDecimal("549.99"), Offset.offset(BigDecimal.valueOf(0.005)));
    }
}
