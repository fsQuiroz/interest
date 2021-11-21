package com.fsquiroz.aplazo.service.validate;

import com.fsquiroz.aplazo.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
public class ValidateServiceUnitTest {

    private ValidateServiceImpl service;

    @BeforeEach
    public void setup() {
        service = new ValidateServiceImpl();
    }

    @Test
    public void valueIsValidId() {
        log.info("Test value is valid id");

        Long id = 1L;

        Exception response = run(() -> service.isValidId(id));

        assertThat(response)
                .isNull();
    }

    @Test
    public void valueIsInvalidId() {
        log.info("Test value is invalid id");

        Long id = null;

        Exception response = run(() -> service.isValidId(id));

        assertThat(response)
                .isExactlyInstanceOf(BadRequestException.class)
                .hasMessage("Invalid id value");
    }

    @Test
    public void objectIsNotNull() {
        log.info("Test object is not null");

        Object o = "not empty";
        String field = "field";

        Exception response = run(() -> service.isNotNull(o, field));

        assertThat(response)
                .isNull();
    }

    @Test
    public void objectIsNull() {
        log.info("Test object is null");

        Object o = null;
        String field = "field";

        Exception response = run(() -> service.isNotNull(o, field));

        assertThat(response)
                .isExactlyInstanceOf(BadRequestException.class)
                .hasMessage("Missing element");
    }

    @Test
    public void valueIsWithinRangeExclusive() {
        log.info("Test value is within exclusive range");

        BigDecimal floor = new BigDecimal("1.00");
        BigDecimal ceiling = new BigDecimal("100.00");
        BigDecimal value = new BigDecimal("2.00");
        String field = "field";

        Exception response = run(() -> service.isWithinRangeExclusive(floor, ceiling, value, field));

        assertThat(response)
                .isNull();
    }

    @Test
    public void valueIsOutsideRangeExclusiveEqualsFloor() {
        log.info("Test value exclusive range is equals to floor");

        BigDecimal floor = new BigDecimal("1.00");
        BigDecimal ceiling = new BigDecimal("100.00");
        BigDecimal value = new BigDecimal("1.00");
        String field = "field";

        Exception response = run(() -> service.isWithinRangeExclusive(floor, ceiling, value, field));

        assertThat(response)
                .isExactlyInstanceOf(BadRequestException.class)
                .hasMessageStartingWith("Element is outside range");
    }

    @Test
    public void valueIsOutsideRangeExclusiveEqualsCeiling() {
        log.info("Test value exclusive range is equals to ceiling");

        BigDecimal floor = new BigDecimal("1.00");
        BigDecimal ceiling = new BigDecimal("100.00");
        BigDecimal value = new BigDecimal("100.00");
        String field = "field";

        Exception response = run(() -> service.isWithinRangeExclusive(floor, ceiling, value, field));

        assertThat(response)
                .isExactlyInstanceOf(BadRequestException.class)
                .hasMessageStartingWith("Element is outside range");
    }

    @Test
    public void valueIsOutsideRangeExclusiveAboveCeiling() {
        log.info("Test value exclusive range is above ceiling");

        BigDecimal floor = new BigDecimal("1.00");
        BigDecimal ceiling = new BigDecimal("100.00");
        BigDecimal value = new BigDecimal("1000.00");
        String field = "field";

        Exception response = run(() -> service.isWithinRangeExclusive(floor, ceiling, value, field));

        assertThat(response)
                .isExactlyInstanceOf(BadRequestException.class)
                .hasMessageStartingWith("Element is outside range");
    }

    @Test
    public void valueIsOutsideRangeExclusiveBelowFloor() {
        log.info("Test value exclusive range is below floor");

        BigDecimal floor = new BigDecimal("1.00");
        BigDecimal ceiling = new BigDecimal("100.00");
        BigDecimal value = new BigDecimal("0.50");
        String field = "field";

        Exception response = run(() -> service.isWithinRangeExclusive(floor, ceiling, value, field));

        assertThat(response)
                .isExactlyInstanceOf(BadRequestException.class)
                .hasMessageStartingWith("Element is outside range");
    }

    @Test
    public void wrapperValueIsWithinRangeInclusive() {
        log.info("Test wrapper value is within range inclusive");

        BigDecimal floor = new BigDecimal("1.00");
        BigDecimal ceiling = new BigDecimal("100.00");
        BigDecimal value = new BigDecimal("5.00");
        String field = "field";

        Exception response = run(() -> service.isWithinRangeInclusive(floor, ceiling, value, field));

        assertThat(response)
                .isNull();
    }

    @Test
    public void wrapperValueIsWithinRangeEqualsFloor() {
        log.info("Test wrapper value is within range equals floor");

        BigDecimal floor = new BigDecimal("1.00");
        BigDecimal ceiling = new BigDecimal("100.00");
        BigDecimal value = new BigDecimal("1.00");
        String field = "field";

        Exception response = run(() -> service.isWithinRangeInclusive(floor, ceiling, value, field));

        assertThat(response)
                .isNull();
    }

    @Test
    public void wrapperValueIsWithinRangeEqualsCeiling() {
        log.info("Test wrapper value is within range equals ceiling");

        BigDecimal floor = new BigDecimal("1.00");
        BigDecimal ceiling = new BigDecimal("100.00");
        BigDecimal value = new BigDecimal("100.00");
        String field = "field";

        Exception response = run(() -> service.isWithinRangeInclusive(floor, ceiling, value, field));

        assertThat(response)
                .isNull();
    }

    @Test
    public void wrapperValueIsOutsideRangeBelowFloor() {
        log.info("Test wrapper value is outside range below floor");

        BigDecimal floor = new BigDecimal("1.00");
        BigDecimal ceiling = new BigDecimal("100.00");
        BigDecimal value = new BigDecimal("0.50");
        String field = "field";

        Exception response = run(() -> service.isWithinRangeInclusive(floor, ceiling, value, field));

        assertThat(response)
                .isExactlyInstanceOf(BadRequestException.class)
                .hasMessageStartingWith("Element is outside range");
    }

    @Test
    public void wrapperValueIsOutsideRangeAboveCeiling() {
        log.info("Test wrapper value is outside range above ceiling");

        BigDecimal floor = new BigDecimal("1.00");
        BigDecimal ceiling = new BigDecimal("100.00");
        BigDecimal value = new BigDecimal("1000.00");
        String field = "field";

        Exception response = run(() -> service.isWithinRangeInclusive(floor, ceiling, value, field));

        assertThat(response)
                .isExactlyInstanceOf(BadRequestException.class)
                .hasMessageStartingWith("Element is outside range");
    }

    @Test
    public void primitiveValueIsWithinRangeInclusive() {
        log.info("Test primitive value is within range inclusive");

        long floor = 1L;
        long ceiling = 100L;
        long value = 5L;
        String field = "field";

        Exception response = run(() -> service.isWithinRangeInclusive(floor, ceiling, value, field));

        assertThat(response)
                .isNull();
    }

    @Test
    public void primitiveValueIsWithinRangeEqualsFloor() {
        log.info("Test primitive value is within range equals floor");

        long floor = 1L;
        long ceiling = 100L;
        long value = 1L;
        String field = "field";

        Exception response = run(() -> service.isWithinRangeInclusive(floor, ceiling, value, field));

        assertThat(response)
                .isNull();
    }

    @Test
    public void primitiveValueIsWithinRangeEqualsCeiling() {
        log.info("Test primitive value is within range equals ceiling");

        long floor = 1L;
        long ceiling = 100L;
        long value = 100L;
        String field = "field";

        Exception response = run(() -> service.isWithinRangeInclusive(floor, ceiling, value, field));

        assertThat(response)
                .isNull();
    }

    @Test
    public void primitiveValueIsOutsideRangeBelowFloor() {
        log.info("Test primitive value is outside range below floor");

        long floor = 1L;
        long ceiling = 100L;
        long value = 0L;
        String field = "field";

        Exception response = run(() -> service.isWithinRangeInclusive(floor, ceiling, value, field));

        assertThat(response)
                .isExactlyInstanceOf(BadRequestException.class)
                .hasMessageStartingWith("Element is outside range");
    }

    @Test
    public void primitiveValueIsOutsideRangeAboveCeiling() {
        log.info("Test primitive value is outside range above ceiling");

        long floor = 1L;
        long ceiling = 100L;
        long value = 1000L;
        String field = "field";

        Exception response = run(() -> service.isWithinRangeInclusive(floor, ceiling, value, field));

        assertThat(response)
                .isExactlyInstanceOf(BadRequestException.class)
                .hasMessageStartingWith("Element is outside range");
    }

    @Test
    public void valueHasNoDecimalWithScaleTwo() {
        log.info("Test value has no decimal with scale 2");

        BigDecimal value = new BigDecimal("100.00");
        String field = "field";

        Exception response = run(() -> service.hasNoDecimals(value, field));

        assertThat(response)
                .isNull();
    }

    @Test
    public void valueHasNoDecimalWithScaleZero() {
        log.info("Test value has no decimal with scale 0");

        BigDecimal value = new BigDecimal("100");
        String field = "field";

        Exception response = run(() -> service.hasNoDecimals(value, field));

        assertThat(response)
                .isNull();
    }

    @Test
    public void valueHasDecimal() {
        log.info("Test value has decimal");

        BigDecimal value = new BigDecimal("100.50");
        String field = "field";

        Exception response = run(() -> service.hasNoDecimals(value, field));

        assertThat(response)
                .isExactlyInstanceOf(BadRequestException.class)
                .hasMessageStartingWith("Value can not have decimals");
    }

    private Exception run(Runnable r) {
        try {
            r.run();
        } catch (Exception e) {
            return e;
        }
        return null;
    }
}
