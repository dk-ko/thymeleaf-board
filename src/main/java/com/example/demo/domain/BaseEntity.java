package com.example.demo.domain;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@ToString
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long idx;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    protected LocalDateTime createdDate;

    @Column
    @LastModifiedDate
    protected LocalDateTime updatedDate;

//    @PrePersist
//    public void onPrePersist() {
//        this.createdDate = LocalDateTime.now(); // TODO ZonedDateTime으로 해야하는가 ?
//        this.updatedDate = this.createdDate;
//    }
//
//    @PreUpdate
//    public void onPreUpdate() {
//        this.updatedDate = LocalDateTime.now();
//    }
}