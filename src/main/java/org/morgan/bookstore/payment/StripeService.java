package org.morgan.bookstore.payment;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;
import org.springframework.stereotype.Service;

@Service
public class StripeService {


    public PaymentIntent createPaymentIntent(double amount) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long) (amount * 100))
                .setCurrency("egp")
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build()
                )
                .setCaptureMethod(PaymentIntentCreateParams.CaptureMethod.AUTOMATIC)
                .build();
        return PaymentIntent.create(params);
    }


    public void cancelPaymentIntent(String paymentIntentId)  {
        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            paymentIntent.cancel();
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    public void refund(String paymentIntentId)  {
        RefundCreateParams params =
                RefundCreateParams.builder().setPaymentIntent(paymentIntentId).build();
        try {
            Refund.create(params);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }
}
