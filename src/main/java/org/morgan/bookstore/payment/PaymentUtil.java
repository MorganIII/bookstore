package org.morgan.bookstore.payment;

import org.morgan.bookstore.enums.PaymentMethod;

public abstract class PaymentUtil {

    public static Pay createPayment(PaymentMethod paymentMethod) {
        return switch (paymentMethod) {
            case CARD -> new CardPayment(new StripeService());
            case COD -> new CashPayment();
        };
    }
}
