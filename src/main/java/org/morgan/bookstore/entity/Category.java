package org.morgan.bookstore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {

    @Id
    private String name;

    private String Description;

    private Date creationDate;

    private Date updatedDate;

    @ManyToOne
    @JoinColumn(name = "section", nullable = false)
    private Section section;

    @OneToMany(mappedBy = "category", cascade = {CascadeType.REMOVE})
    private Set<Book> books;
}
