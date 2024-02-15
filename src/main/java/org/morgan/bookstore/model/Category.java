package org.morgan.bookstore.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category extends BaseEntity{

    @Id
    @Column(name = "category_name")
    private String name;

    @Column(name = "category_description", length = 500)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_section", nullable = false)
    private Section section;

    @OneToMany(mappedBy = "category", cascade = {CascadeType.REMOVE},fetch = FetchType.LAZY)
    private Set<Book> books;
}
