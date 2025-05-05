package dev.saul.descarbonizacion_transporte.global.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.Data;

@Data
public abstract class GenericEntity {
    @Id
    protected String id;
    protected boolean isActive = true;

    @CreatedDate
    protected LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    protected LocalDateTime updatedAt = LocalDateTime.now();

}
