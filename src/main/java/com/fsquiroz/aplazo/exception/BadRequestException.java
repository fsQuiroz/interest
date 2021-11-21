package com.fsquiroz.aplazo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends AppException {

    private BadRequestException(Map<String, Object> meta, String message) {
        super(meta, message);
    }

    public static BadRequestException byInvalidId() {
        return new BadRequestException(null, "Invalid id value");
    }

    public static BadRequestException byMissingElement(String element) {
        Assert.hasText(element, "'element' must not be empty");
        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put("element", element);
        return new BadRequestException(meta, "Missing element");
    }

    public static BadRequestException byOutsideRange(String element, String extraMessage, BigDecimal value, BigDecimal floor, BigDecimal ceiling) {
        Assert.hasText(element, "'element' must not be empty");
        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put("element", element);
        meta.put("value", value);
        meta.put("floor", floor);
        meta.put("ceiling", ceiling);
        StringBuilder message = new StringBuilder("Element is outside range");
        if (StringUtils.hasText(extraMessage)) {
            message.append(". ")
                    .append(extraMessage);
        }
        return new BadRequestException(meta, message.toString());
    }

    public static BadRequestException byDecimalValue(String element, BigDecimal value) {
        Assert.hasText(element, "'element' must not be empty");
        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put("element", element);
        meta.put("value", value);
        return new BadRequestException(meta, "Value can not have decimals");
    }

    public static BadRequestException byInvalidSortParam(Class<?> clazz, String param) {
        Assert.notNull(clazz, "'clazz' can not be null");
        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put("entity", clazz.getSimpleName());
        meta.put("param", param);
        return new BadRequestException(meta, "Invalid sort param");
    }

    public static BadRequestException byMalformedBody() {
        return new BadRequestException(null, "Unable to parse body content");
    }

    public static BadRequestException byMalformedParam(MethodArgumentTypeMismatchException e) {
        Map<String, Object> meta = null;
        if (e != null) {
            meta = new LinkedHashMap<>();
            meta.put("param", e.getName() != null ? e.getName() : e.getPropertyName());
            meta.put("value", e.getValue());
            meta.put("requiredType", e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "UNKNOWN");
            meta.put("message", e.getMessage());
        }
        return new BadRequestException(meta, "Unable to parse param");
    }

    public static BadRequestException byMalformedSortingParam(String message) {
        Map<String, Object> meta = null;
        if (message != null) {
            meta = new LinkedHashMap<>();
            meta.put("param", "sort");
            meta.put("message", message);
        }
        return new BadRequestException(meta, "Unable to parse sorting param");
    }
}
