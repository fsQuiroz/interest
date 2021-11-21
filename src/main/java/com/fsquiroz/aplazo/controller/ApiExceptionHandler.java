package com.fsquiroz.aplazo.controller;

import com.fsquiroz.aplazo.api.ExceptionDTO;
import com.fsquiroz.aplazo.exception.AppException;
import com.fsquiroz.aplazo.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice(annotations = RestController.class)
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<Object> appException(HttpServletRequest req, AppException ae) {
        logUrl(req);
        log.debug(ae.getMessage(), ae);
        return parseException(req, ae);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<Object> propertyReferenceException(HttpServletRequest req, PropertyReferenceException e) {
        logUrl(req);
        log.debug(e.getMessage(), e);
        return parseException(req, BadRequestException.byInvalidSortParam(e.getType().getType(), e.getPropertyName()));
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.debug(e.getMessage(), e);
        return parseException(null, BadRequestException.byMissingElement(e.getParameterName()));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.debug(e.getMessage(), e);
        return parseException(null, BadRequestException.byMalformedBody());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> typeMismatchException(HttpServletRequest req, MethodArgumentTypeMismatchException e) {
        logUrl(req);
        log.debug(e.getMessage(), e);
        return parseException(req, BadRequestException.byMalformedParam(e));
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<Object> dataAccessApiUsageException(HttpServletRequest req, InvalidDataAccessApiUsageException e) {
        logUrl(req);
        log.debug(e.getMessage(), e);
        return parseException(req, BadRequestException.byMalformedSortingParam(e.getMessage()));
    }

    @ExceptionHandler(ClientAbortException.class)
    public void clientAbortException(HttpServletRequest req, ClientAbortException e) {
        logUrl(req);
        log.debug(e.getMessage(), e);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> genericException(HttpServletRequest req, Exception e) {
        if (req.getQueryString() != null && !req.getQueryString().isEmpty()) {
            log.error("[{}] {}?{}", req.getMethod(), req.getRequestURL(), req.getQueryString());
        } else {
            log.error("[{}] {}", req.getMethod(), req.getRequestURL());
        }
        log.error(e.getMessage(), e);
        return parseException(req, e);
    }

    private void logUrl(HttpServletRequest req) {
        if (req.getQueryString() != null && !req.getQueryString().isEmpty()) {
            log.debug("[{}] {}?{}", req.getMethod(), req.getRequestURL(), req.getQueryString());
        } else {
            log.debug("[{}] {}", req.getMethod(), req.getRequestURL());
        }
    }

    private ResponseEntity<Object> parseException(HttpServletRequest req, Exception e) {
        ResponseStatus rs = AnnotatedElementUtils.findMergedAnnotation(e.getClass(), ResponseStatus.class);
        HttpStatus status;
        Map<String, Object> meta = null;
        if (rs != null) {
            status = rs.value();
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        if (e instanceof AppException) {
            AppException ae = (AppException) e;
            meta = ae.getMeta();
        }
        return build(req, status, meta, status == HttpStatus.INTERNAL_SERVER_ERROR ? "There has been an unexpected error" : e.getMessage());
    }

    private ResponseEntity<Object> build(HttpServletRequest req, HttpStatus status, Map<String, Object> meta, String message) {
        ExceptionDTO me = new ExceptionDTO(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                req != null ? req.getRequestURI() : null,
                meta
        );
        return new ResponseEntity<>(
                me,
                status
        );
    }

}
