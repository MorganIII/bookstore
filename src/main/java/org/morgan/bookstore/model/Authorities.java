package org.morgan.bookstore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.morgan.bookstore.enums.Role;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table
public class Authorities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Enumerated(EnumType.STRING)
    @Column(name = "_role")
    private Role role;


}
