package com.fsquiroz.aplazo.mapper;

import com.fsquiroz.aplazo.api.payment.PaymentDTO;
import com.fsquiroz.aplazo.persistence.entity.Payment;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.data.Offset;
import org.assertj.core.data.TemporalUnitWithinOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
public class PaymentMapperUnitTest {

    private PaymentMapper mapper;

    @BeforeEach
    public void setup() {
        mapper = new PaymentMapper();
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

        Payment payment = new Payment();
        payment.setId(99L);
        payment.setCreated(Instant.now());
        payment.setPaymentNumber(3);
        payment.setPaymentDate(Instant.now());
        payment.setAmount(new BigDecimal("549.99"));

        PaymentDTO response = mapper.map(payment);

        assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("updated", null)
                .hasFieldOrPropertyWithValue("deleted", null)
                .hasFieldOrPropertyWithValue("paymentNumber", 3);

        assertThat(response.getCreated())
                .isCloseTo(Instant.now(), new TemporalUnitWithinOffset(200, ChronoUnit.MILLIS));

        assertThat(response.getPaymentDate())
                .isCloseTo(Instant.now(), new TemporalUnitWithinOffset(200, ChronoUnit.MILLIS));

        assertThat(response.getAmount())
                .isCloseTo(new BigDecimal("549.99"), Offset.offset(BigDecimal.valueOf(0.005)));
    }
}
