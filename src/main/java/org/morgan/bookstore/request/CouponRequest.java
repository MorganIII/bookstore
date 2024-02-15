package org.morgan.bookstore.request;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.morgan.bookstore.enums.CouponType;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponRequest {


    @NotBlank(message = "Code is required")
    private String code;

    @NotNull(message = "Coupon type is required")
    private CouponType type;

    @PositiveOrZero(message = "Discount should be positive value or zero")
    private Integer discount;


    @PositiveOrZero(message = "Minimum amount should be positive value or zero")
    private Integer minimumAmount;


    @Size(max = 100, message = "Description should be less than 100 characters")
    private String description;

    @NotNull(message = "provide the start date of the coupon")
    @Future(message = "start date should be in the future")
    private LocalDateTime startDate;

    @NotNull(message = "provide the expiry date of the coupon")
    @Future(message = "expiry date should be in the future")
    private LocalDateTime expiryDate;
}
