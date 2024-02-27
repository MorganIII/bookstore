package org.morgan.bookstore.payment;

import org.morgan.bookstore.model.Order;
import org.morgan.bookstore.model.Payment;

public interface Pay {

    Payment pay(Order order);

}
