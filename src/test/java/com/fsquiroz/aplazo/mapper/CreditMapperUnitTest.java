package com.fsquiroz.aplazo.mapper;

import com.fsquiroz.aplazo.api.credit.CreditDTO;
import com.fsquiroz.aplazo.persistence.entity.Credit;
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
public class CreditMapperUnitTest {

    private CreditMapper mapper;

    @BeforeEach
    public void setup() {
        mapper = new CreditMapper();
    }

    @Test
    public void mapNull() {
        log.info("Test map null");

        Credit credit = null;

        CreditDTO response = mapper.map(credit);

        assertThat(response)
                .isNull();
    }

    @Test
    public void mapCredit() {
        log.info("Test map credit");

        Credit credit = new Credit();
        credit.setId(99L);
        credit.setCreated(Instant.now());
        credit.setAmount(new BigDecimal("1999.99"));
        credit.setRate(new BigDecimal("10.00"));
        credit.setTerms(4);

        CreditDTO response = mapper.map(credit);

        assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("updated", null)
                .hasFieldOrPropertyWithValue("deleted", null)
                .hasFieldOrPropertyWithValue("terms", 4);

        assertThat(response.getCreated())
                .isCloseTo(Instant.now(), new TemporalUnitWithinOffset(200, ChronoUnit.MILLIS));

        assertThat(response.getAmount())
                .isCloseTo(new BigDecimal("1999.99"), Offset.offset(BigDecimal.valueOf(0.005)));

        assertThat(response.getRate())
                .isCloseTo(new BigDecimal("10.00"), Offset.offset(BigDecimal.valueOf(0.005)));
    }
}
