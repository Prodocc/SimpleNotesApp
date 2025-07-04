package com.notes.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Setter
@Getter
@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
public class BaseEntity {

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
