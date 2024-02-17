package org.morgan.bookstore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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


    @NotBlank(message = "Code is required")
    private String code;

    @NotNull(message = "Coupon type is required")
    @Enumerated(EnumType.STRING)
    private CouponType type;

    @PositiveOrZero(message = "Discount should be positive value or zero")
    private Integer discount;

    @PositiveOrZero(message = "Minimum amount should be positive value or zero")
    private Integer minimumAmount;

    @NotNull(message = "Active status is required")
    private Boolean active;

    @Size(max = 100, message = "Description should be less than 100 characters")
    private String description;

    @NotNull(message = "provide the start date of the coupon")
    @Future(message = "start date should be in the future")
    private LocalDateTime startDate;

    @NotNull(message = "provide the expiry date of the coupon")
    @Future(message = "expiry date should be in the future")
    private LocalDateTime expiryDate;

    public boolean isExpired(){
        return LocalDateTime.now().isAfter(expiryDate);
    }


}
