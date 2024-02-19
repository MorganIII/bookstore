package org.morgan.bookstore.repository;

import org.morgan.bookstore.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Integer> {


    @Query(value = """
        select c from Cart c
        where c.user.id=:userId
        """)
    Optional<Cart> getCartByUser(Integer userId);
}
