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
public class SubCategory {

    @Id
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    private String Description;

    private Date creationDate;

    private Date updatedDate;

    @ManyToOne
    @JoinColumn(name = "main_category", nullable = false)
    private MainCategory mainCategory;

    @OneToMany(mappedBy = "subCategory", cascade = {CascadeType.REMOVE})
    private Set<Book> books;
}
