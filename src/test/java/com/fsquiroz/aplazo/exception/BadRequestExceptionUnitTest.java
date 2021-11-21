package com.fsquiroz.aplazo.exception;

import com.fsquiroz.aplazo.persistence.entity.Credit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
public class BadRequestExceptionUnitTest {

    @Test
    public void buildByMissingElement() {
        log.info("Test build BadRequestException by missing element");

        String element = "field";

        BadRequestException response = BadRequestException.byMissingElement(element);

        assertThat(response)
                .isNotNull()
                .hasMessage("Missing element")
                .extracting("meta.element")
                .isNotNull()
                .isEqualTo(element);
    }

    @Test
    public void buildByOutsideRangeWithoutExtraMessage() {
        log.info("Test build BadRequestException by outside range without extra message");

        String element = "rate";
        String extraMessage = null;
        BigDecimal value = new BigDecimal("0.99");
        BigDecimal floor = new BigDecimal("1.00");
        BigDecimal ceiling = new BigDecimal("100.0");

        BadRequestException response = BadRequestException.byOutsideRange(element, extraMessage, value, floor, ceiling);

        assertThat(response)
                .isNotNull()
                .hasMessage("Element is outside range")
                .extracting("meta.element", "meta.value", "meta.floor", "meta.ceiling")
                .containsExactly(element, value, floor, ceiling);
    }

    @Test
    public void buildByOutsideRangeWithExtraMessage() {
        log.info("Test build BadRequestException by outside range with extra message");

        String element = "rate";
        String extraMessage = "Value can not be equal or less than 1.00";
        BigDecimal value = new BigDecimal("0.99");
        BigDecimal floor = new BigDecimal("1.00");
        BigDecimal ceiling = new BigDecimal("100.0");

        BadRequestException response = BadRequestException.byOutsideRange(element, extraMessage, value, floor, ceiling);

        assertThat(response)
                .isNotNull()
                .hasMessage("Element is outside range" + ". " + extraMessage)
                .extracting("meta.element", "meta.value", "meta.floor", "meta.ceiling")
                .containsExactly(element, value, floor, ceiling);
    }

    @Test
    public void buildByDecimalValue() {
        log.info("Test build by decimal value");

        String element = "rate";
        BigDecimal value = new BigDecimal("0.99");

        BadRequestException response = BadRequestException.byDecimalValue(element, value);

        assertThat(response)
                .isNotNull()
                .hasMessage("Value can not have decimals")
                .extracting("meta.element", "meta.value")
                .containsExactly(element, value);
    }

    @Test
    public void buildByInvalidSortParam() {
        log.info("Test build by invalid sort param");

        Class<Credit> clazz = Credit.class;
        String param = "term";

        BadRequestException response = BadRequestException.byInvalidSortParam(clazz, param);

        assertThat(response)
                .isNotNull()
                .hasMessage("Invalid sort param")
                .extracting("meta.entity", "meta.param")
                .containsExactly(clazz.getSimpleName(), param);
    }

    @Test
    public void buildByMalformedBody() {
        log.info("Test build by malformed body");

        BadRequestException response = BadRequestException.byMalformedBody();

        assertThat(response)
                .isNotNull()
                .hasMessage("Unable to parse body content")
                .hasFieldOrPropertyWithValue("meta", null);
    }

    @Test
    public void buildByMalformedParamWithoutException() {
        log.info("Test build by malformed body without exception");

        MethodArgumentTypeMismatchException exception = null;

        BadRequestException response = BadRequestException.byMalformedParam(exception);

        assertThat(response)
                .isNotNull()
                .hasMessage("Unable to parse param")
                .hasFieldOrPropertyWithValue("meta", null);
    }

    @Test
    public void buildByMalformedParamWithExceptionName() {
        log.info("Test build by malformed body with exception and name");

        String param = "param";
        String value = "tue";
        Class<?> requiredType = Boolean.class;
        String message = "Value 'tue' can not be cast to Boolean";

        MethodArgumentTypeMismatchException exception = Mockito.mock(MethodArgumentTypeMismatchException.class);
        Mockito.doReturn(param).when(exception).getName();
        Mockito.doReturn(value).when(exception).getValue();
        Mockito.doReturn(requiredType).when(exception).getRequiredType();
        Mockito.doReturn(message).when(exception).getMessage();

        BadRequestException response = BadRequestException.byMalformedParam(exception);

        assertThat(response)
                .isNotNull()
                .hasMessage("Unable to parse param")
                .extracting("meta.param", "meta.value", "meta.requiredType", "meta.message")
                .containsExactly(param, value, requiredType.getSimpleName(), message);
    }

    @Test
    public void buildByMalformedParamWithExceptionPropertyName() {
        log.info("Test build by malformed body with exception and property name");

        String propertyName = "param";
        String value = "tue";
        Class<?> requiredType = Boolean.class;
        String message = "Value 'tue' can not be cast to Boolean";

        MethodArgumentTypeMismatchException exception = Mockito.mock(MethodArgumentTypeMismatchException.class);
        Mockito.doReturn(null).when(exception).getName();
        Mockito.doReturn(propertyName).when(exception).getPropertyName();
        Mockito.doReturn(value).when(exception).getValue();
        Mockito.doReturn(requiredType).when(exception).getRequiredType();
        Mockito.doReturn(message).when(exception).getMessage();

        BadRequestException response = BadRequestException.byMalformedParam(exception);

        assertThat(response)
                .isNotNull()
                .hasMessage("Unable to parse param")
                .extracting("meta.param", "meta.value", "meta.requiredType", "meta.message")
                .containsExactly(propertyName, value, requiredType.getSimpleName(), message);
    }

    @Test
    public void buildByMalformedParamWithExceptionUnknownType() {
        log.info("Test build by malformed body with exception and unknown type");

        String propertyName = "param";
        String value = "tue";
        String message = "Value 'tue' can not be cast to Boolean";

        MethodArgumentTypeMismatchException exception = Mockito.mock(MethodArgumentTypeMismatchException.class);
        Mockito.doReturn(null).when(exception).getName();
        Mockito.doReturn(propertyName).when(exception).getPropertyName();
        Mockito.doReturn(value).when(exception).getValue();
        Mockito.doReturn(null).when(exception).getRequiredType();
        Mockito.doReturn(message).when(exception).getMessage();

        BadRequestException response = BadRequestException.byMalformedParam(exception);

        assertThat(response)
                .isNotNull()
                .hasMessage("Unable to parse param")
                .extracting("meta.param", "meta.value", "meta.requiredType", "meta.message")
                .containsExactly(propertyName, value, "UNKNOWN", message);
    }

    @Test
    public void buildByMalformedSortingParamWithoutMessage() {
        log.info("Test build by malformed sorting param without message");

        String message = null;

        BadRequestException response = BadRequestException.byMalformedSortingParam(message);

        assertThat(response)
                .isNotNull()
                .hasMessage("Unable to parse sorting param")
                .hasFieldOrPropertyWithValue("meta", null);
    }

    @Test
    public void buildByMalformedSortingParamWithMessage() {
        log.info("Test build by malformed sorting param with message");

        String message = "Unrecognizable direction 'acs'";

        BadRequestException response = BadRequestException.byMalformedSortingParam(message);

        assertThat(response)
                .isNotNull()
                .hasMessage("Unable to parse sorting param")
                .extracting("meta.param", "meta.message")
                .containsExactly("sort", message);
    }
}
