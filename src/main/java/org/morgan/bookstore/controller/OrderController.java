package org.morgan.bookstore.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
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

@Tag(name = "Order", description = "The order API")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderHandler orderHandler;

    private final UserService userService;

    private final OrderService orderService;

    @Operation(summary = "Create order", description = "Create order")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Order created"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            }
    )
    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @PostMapping
    public OrderResponse createOrder(@RequestBody OrderRequest request) {
        User user = userService.getUserById(userService.userId());
        Order order = new Order();
        order.setUser(user);
        return orderHandler.handleOrder(request, new OrderResponse(),order);
    }

    @Operation(summary = "Cancel order", description = "Cancel order")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Order cancelled"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Order not found")
            }
    )
    @PutMapping("/cancel/{id}")
    public void cancelOrder(@PathVariable(name = "id") Integer orderId) {
        orderService.cancelOrder(orderId);
    }

    @Operation(summary = "Update order status", description = "Update order status")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Order status updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Order not found")
            }
    )
    @PutMapping("status/{id}")
    public void updateOrderStatus(@PathVariable(name = "id") @Positive Integer orderId, @RequestParam(name = "status") @NotBlank String status) {
        orderService.updateOrderStatus(orderId, OrderStatus.fromValue(status));
    }


    @Operation(summary = "Get order", description = "Get order by order tracking number")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Order found"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Order not found")
            }
    )
    @GetMapping("/{id}")
    public OrderResponse getOrder(@PathVariable(name = "id") @NotBlank String orderTrackingNumber) {
        Integer userId = userService.getCurrent();
        return orderService.getOrderDetails(orderTrackingNumber, userId);
    }

    @Operation(summary = "Get orders", description = "Get all orders")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Orders found"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            }
    )
    @GetMapping
    public List<OrderListing> getOrders(@RequestParam(name = "status", required = false) String status) {
        return orderService.getAllUserOrders(OrderStatus.fromValue(status));
    }

}
