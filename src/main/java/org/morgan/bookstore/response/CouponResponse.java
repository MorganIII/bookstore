package org.morgan.bookstore.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.morgan.bookstore.enums.CouponType;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponResponse {

    private Integer id;

    private String code;

    private CouponType type;

    private Double discount;

    private LocalDateTime expiryDate;
}
