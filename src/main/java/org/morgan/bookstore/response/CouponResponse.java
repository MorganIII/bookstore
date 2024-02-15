package org.morgan.bookstore.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.morgan.bookstore.enums.CouponType;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponResponse {

    private Integer id;

    private String code;

    private CouponType type;

    private Integer discount;

    private LocalDateTime expiryDate;
}
