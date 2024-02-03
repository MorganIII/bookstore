package org.morgan.bookstore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "sections")
@EntityListeners(AuditingEntityListener.class)
public class Section {


    @Id
    @Column(name = "section_name")
    private String name;

    @Column(name = "section_description", length = 500)
    private String description;

    @CreatedDate
    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @LastModifiedDate
    @Column(name = "update_date", insertable = false)
    private LocalDateTime updatedDate;

    @OneToMany(mappedBy = "section", cascade = {CascadeType.REMOVE})
    private Set<Category> categories;


}
