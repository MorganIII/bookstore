package org.morgan.bookstore.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum OrderStatus {
    PENDING,
    PURCHASED,
    FAILED,
    CANCELLED,
    FULFILLED;


    @JsonCreator
    public static OrderStatus fromValue(String value) {
        for(OrderStatus orderStatus : values()) {
            if(orderStatus.name().equalsIgnoreCase(value)) {
                return orderStatus;
            }
        }
        return null;
    }
}
