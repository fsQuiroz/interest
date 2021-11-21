package com.fsquiroz.aplazo.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public abstract class AppException extends RuntimeException {

    private Map<String, Object> meta;

    protected AppException(Map<String, Object> meta, String message) {
        super(message);
        this.meta = meta;
    }

    protected AppException(Map<String, Object> meta, String message, Throwable cause) {
        super(message, cause);
        this.meta = meta;
    }

}
