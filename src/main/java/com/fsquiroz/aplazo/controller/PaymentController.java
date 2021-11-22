package com.fsquiroz.aplazo.controller;

import com.fsquiroz.aplazo.api.CreditRequestDTO;
import com.fsquiroz.aplazo.api.ExceptionDTO;
import com.fsquiroz.aplazo.api.credit.CreditDTO;
import com.fsquiroz.aplazo.api.payment.PaymentDTO;
import com.fsquiroz.aplazo.mapper.CreditMapper;
import com.fsquiroz.aplazo.mapper.PaymentMapper;
import com.fsquiroz.aplazo.persistence.entity.Credit;
import com.fsquiroz.aplazo.persistence.entity.Payment;
import com.fsquiroz.aplazo.service.credit.CreditService;
import com.fsquiroz.aplazo.service.payment.PaymentService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("/credits")
public class PaymentController {

    private CreditService creditService;

    private PaymentService paymentService;

    private CreditMapper creditMapper;

    private PaymentMapper paymentMapper;

    public PaymentController(CreditService creditService, PaymentService paymentService, CreditMapper creditMapper, PaymentMapper paymentMapper) {
        this.creditService = creditService;
        this.paymentService = paymentService;
        this.creditMapper = creditMapper;
        this.paymentMapper = paymentMapper;
    }

    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataTypeClass = Integer.class, paramType = "query",
                    value = "Page number. Range from 0 to N", example = "0")
            ,
            @ApiImplicitParam(name = "size", dataTypeClass = Integer.class, paramType = "query",
                    value = "Page size. Number of elements to be retrieved. Rage from 1 to N", example = "20")
            ,
            @ApiImplicitParam(name = "sort", dataTypeClass = String.class, paramType = "query",
                    value = "Sort result. Order by property. Applicable to some first level properties. Ex: sort=name,desc")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok", response = Page.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ExceptionDTO.class)
    })
    public ResponseEntity<Page<CreditDTO>> listCredits(@ApiIgnore Pageable pageRequest) {
        Page<Credit> credits = creditService.list(pageRequest);
        Page<CreditDTO> mapped = creditMapper.map(credits);
        return ResponseEntity.ok(mapped);
    }

    @PostMapping
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created", response = List.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ExceptionDTO.class)
    })
    public ResponseEntity<List<PaymentDTO>> calculate(CreditRequestDTO request) {
        List<Payment> payments = creditService.calculate(request);
        List<PaymentDTO> mapped = paymentMapper.map(payments);
        return new ResponseEntity<>(mapped, HttpStatus.CREATED);
    }

    @GetMapping("/{creditId}/payments")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataTypeClass = Integer.class, paramType = "query",
                    value = "Page number. Range from 0 to N", example = "0")
            ,
            @ApiImplicitParam(name = "size", dataTypeClass = Integer.class, paramType = "query",
                    value = "Page size. Number of elements to be retrieved. Rage from 1 to N", example = "20")
            ,
            @ApiImplicitParam(name = "sort", dataTypeClass = String.class, paramType = "query",
                    value = "Sort result. Order by property. Applicable to some first level properties. Ex: sort=name,desc")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok", response = Page.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ExceptionDTO.class),
            @ApiResponse(code = 414, message = "Not Found", response = ExceptionDTO.class)
    })
    public ResponseEntity<Page<PaymentDTO>> paymentsByCredit(
            @PathVariable Long creditId,
            @ApiIgnore Pageable pageRequest
    ) {
        Credit credit = creditService.get(creditId);
        Page<Payment> payments = paymentService.list(credit, pageRequest);
        Page<PaymentDTO> mapped = paymentMapper.map(payments);
        return ResponseEntity.ok(mapped);
    }
}
