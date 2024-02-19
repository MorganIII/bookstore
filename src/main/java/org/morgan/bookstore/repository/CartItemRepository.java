package org.morgan.bookstore.repository;

import jakarta.transaction.Transactional;
import org.morgan.bookstore.model.Book;
import org.morgan.bookstore.model.Cart;
import org.morgan.bookstore.model.CartItem;
import org.morgan.bookstore.response.ItemDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Integer> {

    boolean existsByCartAndBook(Cart cart, Book book);

    boolean existsByCartAndId(Cart cart, Integer itemId);

    @Query(value = """
        select new org.morgan.bookstore.response.ItemDTO
        (ci.id, b.id, b.title, b.actualPrice, ci.quantity, b.actualPrice ,b.bookThumbnail)
        from CartItem ci inner join ci.book b
        where ci.cart=:cart
        """)
    List<ItemDTO> getCartItems(@Param("cart")Cart cart);


    @Modifying
    @Query(value = """
        update CartItem c
        set c.quantity=:quantity
        where c.cart=:cart and c.book=:book
        """)
    @Transactional
    void updateCartItemQuantity(@Param("cart")Cart cart,
                                @Param("book")Book book,
                                @Param("quantity")Integer quantity);

    @Query(value = """
        select count(c) from CartItem c
        where c.cart=:cart""")
    Integer countCartItems(@Param("cart")Cart cart);

    @Transactional
    @Modifying
    @Query(value = """
        delete from CartItem c
        where c.cart=:cart
        """)
    void clearCart(@Param("cart")Cart cart);



}
