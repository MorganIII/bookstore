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
@Table(name = "sections")
public class Section extends BaseEntity {
    @Id
    @Column(name = "section_name")
    private String name;

    @Column(name = "section_description", length = 500)
    private String description;

    @OneToMany(mappedBy = "section", cascade = {CascadeType.REMOVE})
    private Set<Category> categories;
}
