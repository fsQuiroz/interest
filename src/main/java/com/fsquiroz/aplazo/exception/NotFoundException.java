package com.fsquiroz.aplazo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.LinkedHashMap;
import java.util.Map;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends AppException {

    private NotFoundException(Map<String, Object> meta, String message) {
        super(meta, message);
    }

    public static NotFoundException byId(Class<?> clazz, Long id) {
        Map<String, Object> meta = ofClass(clazz);
        meta.put("id", id);
        return new NotFoundException(meta, "Entity not found");
    }

    private static Map<String, Object> ofClass(Class<?> clazz) {
        Assert.notNull(clazz, "'clazz' can not be null");
        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put("entity", clazz.getSimpleName());
        return meta;
    }
}
