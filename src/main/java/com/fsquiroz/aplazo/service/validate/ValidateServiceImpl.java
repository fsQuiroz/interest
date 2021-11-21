package com.fsquiroz.aplazo.service.validate;

import com.fsquiroz.aplazo.exception.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;

@Service
public class ValidateServiceImpl implements ValidateService {

    @Override
    public void isNotNull(Object o, String name) {
        Assert.hasText(name, "'name' must not be empty");
        if (o == null) {
            throw BadRequestException.byMissingElement(name);
        }
    }

    @Override
    public void isWithinRangeExclusive(BigDecimal floor, BigDecimal ceiling, BigDecimal value, String name) {
        Assert.hasText(name, "'name' must not be empty");
        Assert.notNull(value, "'value' must not be null");
        Assert.notNull(floor, "'floor' must not be null");
        Assert.notNull(ceiling, "'ceiling' must not be null");
        isNotNull(value, name);
        if (value.compareTo(floor) <= 0) {
            StringBuilder extraMessage = new StringBuilder("Value must be grater than ")
                    .append(floor.toPlainString());
            throw BadRequestException.byOutsideRange(name, extraMessage.toString(), value, floor, ceiling);
        }
        if (value.compareTo(ceiling) >= 0) {
            StringBuilder extraMessage = new StringBuilder("Value must be less than ")
                    .append(ceiling.toPlainString());
            throw BadRequestException.byOutsideRange(name, extraMessage.toString(), value, floor, ceiling);
        }
    }

    @Override
    public void isWithinRangeInclusive(BigDecimal floor, BigDecimal ceiling, BigDecimal value, String name) {
        Assert.hasText(name, "'name' must not be empty");
        Assert.notNull(value, "'value' must not be null");
        Assert.notNull(floor, "'floor' must not be null");
        Assert.notNull(ceiling, "'ceiling' must not be null");
        isNotNull(value, name);
        if (value.compareTo(floor) < 0) {
            StringBuilder extraMessage = new StringBuilder("Value must be equal or grater than ")
                    .append(floor.toPlainString());
            throw BadRequestException.byOutsideRange(name, extraMessage.toString(), value, floor, ceiling);
        }
        if (value.compareTo(ceiling) > 0) {
            StringBuilder extraMessage = new StringBuilder("Value must be equal or less than ")
                    .append(ceiling.toPlainString());
            throw BadRequestException.byOutsideRange(name, extraMessage.toString(), value, floor, ceiling);
        }
    }

    @Override
    public void isWithinRangeInclusive(long floor, long ceiling, long value, String name) {
        Assert.hasText(name, "'name' must not be empty");
        if (value < floor) {
            StringBuilder extraMessage = new StringBuilder("Value must be equal or grater than ")
                    .append(floor);
            throw BadRequestException.byOutsideRange(name, extraMessage.toString(), BigDecimal.valueOf(value), BigDecimal.valueOf(floor), BigDecimal.valueOf(ceiling));
        }
        if (value > ceiling) {
            StringBuilder extraMessage = new StringBuilder("Value must be equal or less than ")
                    .append(ceiling);
            throw BadRequestException.byOutsideRange(name, extraMessage.toString(), BigDecimal.valueOf(value), BigDecimal.valueOf(floor), BigDecimal.valueOf(ceiling));
        }
    }

    @Override
    public void hasNoDecimals(BigDecimal value, String name) {
        Assert.hasText(name, "'name' must not be empty");
        Assert.notNull(value, "'value' must not be null");
        if (value.stripTrailingZeros().scale() > 0) {
            throw BadRequestException.byDecimalValue(name, value);
        }
    }
}
