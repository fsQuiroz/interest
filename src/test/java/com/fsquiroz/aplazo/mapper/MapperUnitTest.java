package com.fsquiroz.aplazo.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
public class MapperUnitTest {

    private Mapper<BigInteger, String> mapper;

    @BeforeEach
    public void setup() {
        mapper = new NumberMapper();
    }

    @Test
    public void testMapNullList() {
        log.info("Test map null list");

        List<BigInteger> values = null;

        List<String> response = mapper.map(values);

        assertThat(response)
                .isNull();
    }

    @Test
    public void testMapEmptyList() {
        log.info("Test map empty list");

        List<BigInteger> values = Collections.emptyList();

        List<String> response = mapper.map(values);

        assertThat(response)
                .hasSize(0);
    }

    @Test
    public void testMapList() {
        log.info("Test map list");

        List<BigInteger> values = Arrays.asList(
                new BigInteger("1"),
                new BigInteger("10"),
                new BigInteger("50")
        );

        List<String> response = mapper.map(values);

        assertThat(response)
                .hasSize(3)
                .containsExactly("1", "10", "50");
    }

    @Test
    public void testMapPage() {
        log.info("Test map page");

        List<BigInteger> values = Arrays.asList(
                new BigInteger("1"),
                new BigInteger("10"),
                new BigInteger("50")
        );
        Pageable pageRequest = PageRequest.of(1, 20);
        Page<BigInteger> page = new PageImpl<>(values, pageRequest, 3L);

        Page<String> response = mapper.map(page);

        assertThat(response)
                .hasSize(3)
                .containsExactly("1", "10", "50");

        assertThat(response.getPageable())
                .isNotNull()
                .hasFieldOrPropertyWithValue("page", 1)
                .hasFieldOrPropertyWithValue("size", 20);
    }

    private static class NumberMapper extends Mapper<BigInteger, String> {

        @Override
        public String map(BigInteger e) {
            return e.toString();
        }
    }
}
