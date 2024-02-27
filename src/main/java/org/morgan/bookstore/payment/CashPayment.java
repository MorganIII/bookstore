package org.morgan.bookstore.payment;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.morgan.bookstore.enums.PaymentMethod;
import org.morgan.bookstore.enums.PaymentStatus;
import org.morgan.bookstore.model.Order;
import org.morgan.bookstore.model.Payment;
import org.morgan.bookstore.response.ItemDTO;

import java.util.List;

@Slf4j
public class CashPayment implements Pay{



    @Override
    public Payment pay(Order order) {
        Double paymentAmount = order.getTotalPrice() + order.getShippingPrice() - order.getDiscount();
        log.info("Processing cash payment");
        Payment payment = Payment.builder()
                .paymentMethod(PaymentMethod.COD)
                .paymentStatus(PaymentStatus.SUCCESSFUL)
                .amount(paymentAmount)
                .build();
        log.info("Cash payment processed successfully");
        return payment;
    }
}
