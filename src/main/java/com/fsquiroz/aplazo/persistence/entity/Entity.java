package com.fsquiroz.aplazo.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Data
@EqualsAndHashCode(of = "id")
@MappedSuperclass
public class Entity implements Comparable<Entity>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(columnDefinition = "TIMESTAMP")
    protected Instant created;

    @Column(columnDefinition = "TIMESTAMP")
    protected Instant updated;

    @Column(columnDefinition = "TIMESTAMP")
    protected Instant deleted;

    @Override
    public int compareTo(Entity o) {
        Long id = getId();
        if (o == null || o.getId() == null) {
            return Integer.MAX_VALUE;
        } else if (id == null) {
            return Integer.MIN_VALUE;
        } else {
            return id.compareTo(o.getId());
        }
    }
}
