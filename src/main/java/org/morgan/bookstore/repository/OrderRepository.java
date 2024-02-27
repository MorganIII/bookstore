package org.morgan.bookstore.repository;

import org.morgan.bookstore.enums.OrderStatus;
import org.morgan.bookstore.model.Order;
import org.morgan.bookstore.model.Payment;
import org.morgan.bookstore.response.OrderListing;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{


    @EntityGraph(attributePaths = {"shippingAddress", "payment", "coupon"})
    @Query("select o from Order o where (o.user.id=:userId or :userId is null)  and o.orderTrackingNumber=:orderTrackingNumber")
    Optional<Order> findOrderByOrderTrackingNumber(@Param("orderTrackingNumber") String orderTrackingNumber, @Param("userId") Integer userId );

    Optional<Order> findOrderByPayment(Payment payment);


    @Query(value = """
            select new org.morgan.bookstore.response.OrderListing
            (o.id, o.totalItems, p.amount, o.orderTrackingNumber, o.placedAt, o.orderStatus)
            from Order o inner join o.payment p
            where (o.user.id=:userId or :userId is null) and (o.orderStatus=:orderStatus or :orderStatus is null)
        """)
    List<OrderListing> findAllUserOrders(@Param("userId") Integer userId, OrderStatus orderStatus);

    @Query(value = """
           select o from Order o where o.id=:id and o.user.id=:userId
           """)
    Optional<Order> findOrderByIdAndUser(Integer id, Integer userId);


}
