package org.morgan.bookstore.model;

import jakarta.persistence.*;
import lombok.*;
import org.morgan.bookstore.enums.PaymentMethod;
import org.morgan.bookstore.enums.PaymentStatus;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "payments")
public class Payment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String paymentIntentId;

    private String clientSecret;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private Double amount;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "payment")
    private Order order;

}
