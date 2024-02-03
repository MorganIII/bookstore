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
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Category {

    @Id
    @Column(name = "category_name")
    private String name;

    @Column(name = "category_description", length = 500)
    private String description;

    @CreatedDate
    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @LastModifiedDate
    @Column(name = "update_date", insertable = false)
    private LocalDateTime updatedDate;

    @ManyToOne
    @JoinColumn(name = "section", nullable = false)
    private Section section;

    @OneToMany(mappedBy = "category", cascade = {CascadeType.REMOVE})
    private Set<Book> books;
}
