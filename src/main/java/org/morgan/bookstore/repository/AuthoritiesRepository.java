package org.morgan.bookstore.repository;

import org.morgan.bookstore.enums.Role;
import org.morgan.bookstore.model.Authorities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthoritiesRepository extends JpaRepository<Authorities,Integer> {

    Authorities findAuthoritiesByRole(Role role);
}
