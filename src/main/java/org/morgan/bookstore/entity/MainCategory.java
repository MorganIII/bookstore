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
public class MainCategory {

    @Id
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    private Date creationDate;

    private Date updatedDate;

    @OneToMany(mappedBy = "mainCategory", cascade = {CascadeType.REMOVE})
    private Set<SubCategory> subCategories;


}
