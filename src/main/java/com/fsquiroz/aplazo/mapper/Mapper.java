package com.fsquiroz.aplazo.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Mapper<ENTITY, DTO> {

    public abstract DTO map(ENTITY e);

    public List<DTO> map(Collection<ENTITY> entities) {
        return entities == null ? null : entities.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    public Page<DTO> map(Page<ENTITY> entities) {
        return new PageImpl<>(map(entities.toList()), entities.getPageable(), entities.getTotalElements());
    }
}
