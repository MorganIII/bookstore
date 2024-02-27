package org.morgan.bookstore.repository;

import org.morgan.bookstore.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Integer> {


    Optional<Payment> findByPaymentIntentId(String paymentIntentId);
}
