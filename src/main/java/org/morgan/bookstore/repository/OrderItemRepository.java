package org.morgan.bookstore.repository;

import org.morgan.bookstore.model.Order;
import org.morgan.bookstore.model.OrderItem;
import org.morgan.bookstore.response.ItemDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {


    @Query(value = """
        select new org.morgan.bookstore.response.ItemDTO
        (oi.id, b.id, b.title, b.actualPrice, oi.quantity, b.actualPrice ,b.bookThumbnail)
        from OrderItem oi inner join oi.book b
        where oi.order=:order
        """)
    List<ItemDTO> getOrderItems(@Param("order") Order order);
}
