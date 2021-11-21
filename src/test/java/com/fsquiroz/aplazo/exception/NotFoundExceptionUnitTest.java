package com.fsquiroz.aplazo.exception;

import com.fsquiroz.aplazo.persistence.entity.Credit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
public class NotFoundExceptionUnitTest {

    @Test
    public void buildById() {
        log.info("Test build NotFoundException by id");

        Class<Credit> clazz = Credit.class;
        Long id = 11L;

        NotFoundException response = NotFoundException.byId(clazz, id);

        assertThat(response)
                .isNotNull()
                .hasMessage("Entity not found")
                .extracting("meta.entity", "meta.id")
                .containsExactly(clazz.getSimpleName(), id);
    }
}
