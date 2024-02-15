package org.morgan.bookstore.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.morgan.bookstore.exception.CouponException;

import java.util.List;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CouponType {

    FREE_SHIPPING("Free Shipping"),
    FIXED_AMOUNT("Fixed Amount"),
    PERCENTAGE("Percentage");

    private final String name;

    CouponType(String name) {
        this.name = name;
    }

    @JsonCreator
    public static CouponType fromValue(String value) {
        for (CouponType type : CouponType.values()) {
            if (type.name.equals(value)) {
                return type;
            }
        }
        throw new CouponException(String.format("Invalid coupon type value: %s", value));
    }

    public static List<String> getCouponTypes() {
        return List.of(FREE_SHIPPING.name, FIXED_AMOUNT.name, PERCENTAGE.name);
    }
}
