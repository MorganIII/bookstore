package org.morgan.bookstore.order;

import lombok.Data;
import org.morgan.bookstore.model.Order;
import org.morgan.bookstore.request.OrderRequest;
import org.morgan.bookstore.response.OrderResponse;

@Data
public abstract class OrderHandler {

    protected OrderHandler next;
    public static OrderHandler createChain(OrderHandler head, OrderHandler... handlers) {
        OrderHandler current = head;
        for (OrderHandler handler : handlers) {
            current.setNext(handler);
            current = handler;
        }
        return head;
    }

    public abstract OrderResponse handleOrder(OrderRequest request, OrderResponse response, Order order);

    public OrderResponse process(OrderRequest request, OrderResponse response, Order order) {
        return next != null ? next.handleOrder(request, response, order) : response;
    }
}
