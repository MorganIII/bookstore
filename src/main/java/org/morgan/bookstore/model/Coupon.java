package org.morgan.bookstore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.morgan.bookstore.enums.CouponType;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Coupon{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String code;

    @Enumerated(EnumType.STRING)
    private CouponType type;

    private Integer discount;

    private Integer minimumAmount;

    private Boolean active;

    private String description;

    private LocalDateTime startDate ;

    private LocalDateTime expiryDate;


}
