package org.morgan.bookstore.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.Set;

@EqualsAndHashCode(callSuper = true, exclude = {"cart"})
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "display_name")
    private String displayName;

    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "passowrd", nullable = false)
    private String password;

    @Column(name = "contact_number")
    private String phoneNumber;

    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled;

    @Column(name = "token")
    private String token;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Address> addresses;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private Cart cart;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "USER_ROLE",
            joinColumns = {
                    @JoinColumn(name = "USER_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "ROLE_ID")
            }
    )
    private Set<Authorities> authorities;
}