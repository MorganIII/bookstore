package org.morgan.bookstore.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.morgan.bookstore.model.Order;
import org.morgan.bookstore.model.Payment;
import org.morgan.bookstore.order.OrderHandler;
import org.morgan.bookstore.payment.Pay;
import org.morgan.bookstore.payment.PaymentUtil;
import org.morgan.bookstore.repository.PaymentRepository;
import org.morgan.bookstore.request.OrderRequest;
import org.morgan.bookstore.response.OrderResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService extends OrderHandler {

    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public OrderResponse handleOrder(OrderRequest request, OrderResponse response, Order order) {
        Pay paymentMethod = PaymentUtil.createPayment(request.getPaymentMethod());
        Payment payment = paymentMethod.pay(order);

        response.setTotal(payment.getAmount());
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setPaymentStatus(payment.getPaymentStatus());
        response.setClientSecret(payment.getClientSecret());

        paymentRepository.save(payment);
        order.setPayment(payment);
        return process(request, response, order);
    }

    public Payment getPaymentByIntentId(String paymentIntentId) {
        return paymentRepository.findByPaymentIntentId(paymentIntentId).
                orElseThrow(() -> new EntityNotFoundException("Payment not found"));
    }

}
