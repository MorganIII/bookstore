package org.morgan.bookstore.service;


import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.morgan.bookstore.enums.*;
import org.morgan.bookstore.model.Order;
import org.morgan.bookstore.model.Payment;
import org.morgan.bookstore.order.OrderHandler;
import org.morgan.bookstore.payment.StripeService;
import org.morgan.bookstore.repository.OrderItemRepository;
import org.morgan.bookstore.repository.OrderRepository;
import org.morgan.bookstore.request.AddressRequest;
import org.morgan.bookstore.request.OrderRequest;
import org.morgan.bookstore.response.ItemDTO;
import org.morgan.bookstore.response.OrderListing;
import org.morgan.bookstore.response.OrderResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService extends OrderHandler {

    private final OrderRepository orderRepository;
    private final BookService bookService;
    private final PaymentService paymentService;
    private final OrderItemRepository orderItemRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final StripeService stripeService;
    @Transactional
    public void paymentSucceeded(String PaymentIntentId) {
        Payment payment = paymentService.getPaymentByIntentId(PaymentIntentId);
        Order order = getOrderByPayment(payment);
        order.setOrderStatus(OrderStatus.PURCHASED);
        payment.setPaymentStatus(PaymentStatus.SUCCESSFUL);
        orderRepository.save(order);
    }

    @Transactional
    public void paymentFailed(String PaymentIntentId) {
        Payment payment = paymentService.getPaymentByIntentId(PaymentIntentId);
        Order order = getOrderByPayment(payment);
        order.setOrderStatus(OrderStatus.FAILED);
        payment.setPaymentStatus(PaymentStatus.FAILED);
        List<ItemDTO> items = orderItemRepository.getOrderItems(order);
        bookService.updateBookCopiesInStock(items, ChangeType.INCREASE);
        bookService.updateSoldCopies(items, ChangeType.DECREASE);
        orderRepository.save(order);
    }

    @Transactional
    public void cancelOrder(Integer orderId) {
        Integer userId = userService.getCurrent();
        Order order = orderRepository.
                findOrderByIdAndUser(orderId, userId).
                orElseThrow(()-> new EntityNotFoundException("Order not found"));
        if(order.getOrderStatus() != OrderStatus.PURCHASED && order.getOrderStatus() != OrderStatus.PENDING){
            throw new IllegalStateException(
                    String.format("You can't cancel the order after it has been %s", order.getOrderStatus())
            );
        }
        Payment payment =  order.getPayment();
        if(payment.getPaymentMethod() == PaymentMethod.CARD) {
            switch (order.getOrderStatus()) {
                case PURCHASED:
                    stripeService.refund(payment.getPaymentIntentId());
                    break;
                case PENDING:
                    stripeService.cancelPaymentIntent(payment.getPaymentIntentId());
                    break;
            }
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        // sending an email to user to notify him that the payment has been cancelled
        orderRepository.save(order);
    }

    @Transactional
    public void updateOrderStatus(Integer orderId, OrderStatus orderStatus) {
        Order order = getOrderById(orderId);
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public OrderResponse handleOrder(OrderRequest request, OrderResponse response, Order order) {
        if(request.getPaymentMethod() == PaymentMethod.COD) {
            order.setOrderStatus(OrderStatus.PURCHASED);
        } else {
            order.setOrderStatus(OrderStatus.PENDING);
        }
        order.setPlacedAt(LocalDateTime.now());
        response.setOrderStatus(order.getOrderStatus());
        response.setPlacedAt(order.getPlacedAt());
        order = orderRepository.save(order);
        int orderNumber = order.getId()+1000;
        String orderTrackingNumber = "ORD-"+orderNumber;
        response.setOrderTrackingNumber(orderTrackingNumber);
        order.setOrderTrackingNumber(orderTrackingNumber);
        orderRepository.save(order);
        return process(request, response, order);
    }


    public Order getOrderByPayment(Payment payment) {
        return orderRepository.findOrderByPayment(payment).orElseThrow(() -> new EntityNotFoundException("Order not found"));
    }



    public List<OrderListing> getAllUserOrders(OrderStatus orderStatus) {
        Integer userId = userService.getCurrent();
        return orderRepository.findAllUserOrders(userId, orderStatus);
    }

    public OrderResponse getOrderDetails(String orderTrackingNumber, Integer userId) {
        Order order = orderRepository.findOrderByOrderTrackingNumber(orderTrackingNumber, userId).orElseThrow(() -> new EntityNotFoundException("Order not found"));
        return OrderResponse.builder()
                .orderStatus(order.getOrderStatus())
                .placedAt(order.getPlacedAt())
                .orderTrackingNumber(order.getOrderTrackingNumber())
                .totalItems(order.getTotalItems())
                .subTotal(order.getPayment().getAmount())
                .paymentMethod(order.getPayment().getPaymentMethod())
                .paymentStatus(order.getPayment().getPaymentStatus())
                .items(orderItemRepository.getOrderItems(order))
                .shippingAddress(modelMapper.map(order.getShippingAddress(), AddressRequest.class))
                .total(order.getPayment().getAmount())
                .build();
    }

    public Order getOrderById(Integer orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Order not found"));
    }
}
