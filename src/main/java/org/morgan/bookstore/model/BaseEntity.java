package org.morgan.bookstore.model;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @Column(name = "creation_time",nullable = false,updatable = false)
    @CreationTimestamp
    private LocalDateTime creationTime;

    @Column(name = "last_updated_time", insertable = false)
    @UpdateTimestamp
    private LocalDateTime lastUpdatedTime;

    @Column(name ="created_by")
    @CreatedBy
    private String createdBy;

    @Column(name ="updated_by")
    private  String updatedBy;
}
