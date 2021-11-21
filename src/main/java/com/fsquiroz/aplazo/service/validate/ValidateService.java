package com.fsquiroz.aplazo.service.validate;

import java.math.BigDecimal;

public interface ValidateService {

    void isValidId(Long id);

    void isNotNull(Object o, String name);

    void isWithinRangeExclusive(BigDecimal floor, BigDecimal ceiling, BigDecimal value, String name);

    void isWithinRangeInclusive(BigDecimal floor, BigDecimal ceiling, BigDecimal value, String name);

    void isWithinRangeInclusive(long floor, long ceiling, long value, String name);

    void hasNoDecimals(BigDecimal value, String name);
}
