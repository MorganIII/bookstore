package org.morgan.bookstore.repository;

import org.morgan.bookstore.model.Coupon;
import org.morgan.bookstore.response.CouponResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer> {


    Optional<Coupon> findCouponByCode(String code);

    @Query(value = """
            select new org.morgan.bookstore.response.CouponResponse(c.id, c.code, c.type, c.discount, c.expiryDate)
            from Coupon c
            where (:active is null or c.active=:active)
            """)
    List<CouponResponse> findAllCoupons(Boolean active);
}
