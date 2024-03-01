package org.morgan.bookstore.payment;


import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.morgan.bookstore.enums.PaymentMethod;
import org.morgan.bookstore.enums.PaymentStatus;
import org.morgan.bookstore.model.Order;
import org.morgan.bookstore.model.Payment;

@Slf4j
@Getter
@AllArgsConstructor
public class CardPayment implements Pay {

    private StripeService stripeService;

    @Override
    public Payment pay(Order order) {
        double amount = order.getTotalPrice()+order.getShippingPrice()-order.getDiscount();
        try {
            PaymentIntent paymentIntent = stripeService.createPaymentIntent(amount);
            return Payment.builder()
                    .paymentStatus(PaymentStatus.PENDING)
                    .paymentIntentId(paymentIntent.getId())
                    .clientSecret(paymentIntent.getClientSecret())
                    .amount(amount)
                    .paymentMethod(PaymentMethod.CARD)
                    .build();
        } catch (StripeException e) {
            throw new RuntimeException("Error occurred while processing payment");
        }
    }


}