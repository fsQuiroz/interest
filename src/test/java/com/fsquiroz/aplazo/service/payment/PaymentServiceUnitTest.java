package com.fsquiroz.aplazo.service.payment;

import com.fsquiroz.aplazo.persistence.entity.Credit;
import com.fsquiroz.aplazo.persistence.entity.Payment;
import com.fsquiroz.aplazo.persistence.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
public class PaymentServiceUnitTest {

    @MockBean
    private PaymentRepository paymentRepository;

    @Captor
    private ArgumentCaptor<Credit> creditCaptor;

    @Captor
    private ArgumentCaptor<Pageable> pageRequestCaptor;

    private PaymentServiceImpl service;

    @BeforeEach
    public void setup() {
        service = new PaymentServiceImpl(paymentRepository);
    }

    @Test
    public void listPayments() {
        log.info("Test list payments");

        Credit credit = new Credit();
        credit.setId(1L);

        PageRequest pageRequest = PageRequest.of(1, 20);
        Payment p1 = new Payment();
        p1.setId(1L);
        Payment p2 = new Payment();
        p2.setId(2L);

        List<Payment> payments = Arrays.asList(p1, p2);
        Page<Payment> page = new PageImpl<>(payments, pageRequest, 2L);

        Mockito.doReturn(page).when(paymentRepository).findAllByCreditAndDeletedIsNull(any(), any());

        Page<Payment> response = service.list(credit, pageRequest);

        Mockito.verify(paymentRepository, Mockito.times(1))
                .findAllByCreditAndDeletedIsNull(creditCaptor.capture(), pageRequestCaptor.capture());

        assertThat(response)
                .isNotNull()
                .hasSize(2)
                .containsExactly(p1, p2);

        Credit capturedCredit = creditCaptor.getValue();

        assertThat(capturedCredit)
                .isEqualTo(credit);

        Pageable capturedPageRequest = pageRequestCaptor.getValue();

        assertThat(capturedPageRequest)
                .isNotNull()
                .hasFieldOrPropertyWithValue("page", 1)
                .hasFieldOrPropertyWithValue("size", 20);
    }
}
