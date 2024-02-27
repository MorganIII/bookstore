package org.morgan.bookstore.controller;


import lombok.RequiredArgsConstructor;
import org.morgan.bookstore.enums.OrderStatus;
import org.morgan.bookstore.model.Order;
import org.morgan.bookstore.model.User;
import org.morgan.bookstore.order.OrderHandler;
import org.morgan.bookstore.request.OrderRequest;
import org.morgan.bookstore.response.OrderListing;
import org.morgan.bookstore.response.OrderResponse;
import org.morgan.bookstore.service.OrderService;
import org.morgan.bookstore.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderHandler orderHandler;

    private final UserService userService;

    private final OrderService orderService;

    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @PostMapping
    public OrderResponse createOrder(@RequestBody OrderRequest request) {
        User user = userService.getUserById(userService.userId());
        Order order = new Order();
        order.setUser(user);
        return orderHandler.handleOrder(request, new OrderResponse(),order);
    }

    @PutMapping("/cancel/{id}")
    public void cancelOrder(@PathVariable(name = "id") Integer orderId) {
        orderService.cancelOrder(orderId);
    }

    @PutMapping("status/{id}")
    public void updateOrderStatus(@PathVariable(name = "id") Integer orderId, @RequestParam(name = "status") String status) {
        orderService.updateOrderStatus(orderId, OrderStatus.fromValue(status));
    }


    @GetMapping("/{id}")
    public OrderResponse getOrder(@PathVariable(name = "id") String orderTrackingNumber) {
        Integer userId = userService.getCurrent();
        return orderService.getOrderDetails(orderTrackingNumber, userId);
    }

    @GetMapping
    public List<OrderListing> getOrders(@RequestParam(name = "status", required = false) String status) {
        return orderService.getAllUserOrders(OrderStatus.fromValue(status));
    }

}
