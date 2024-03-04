package org.morgan.bookstore.controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.morgan.bookstore.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Payment", description = "Payment API")
@Slf4j
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {


    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    private final OrderService orderService;

    @Operation(summary = "Handle Stripe Webhook")
    @PostMapping(value = "/webhook")
    public ResponseEntity<String> webhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            log.error("Failed signature verification");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        PaymentIntent intent;

        switch(event.getType()) {
            case "payment_intent.succeeded":
                intent = (PaymentIntent) event
                        .getDataObjectDeserializer()
                        .getObject()
                        .get();
                orderService.paymentSucceeded(intent.getId());
                log.info("Payment succeeded{}", intent.getId());
                break;

            case "payment_intent.payment_failed":
                intent = (PaymentIntent) event
                        .getDataObjectDeserializer()
                        .getObject()
                        .get();
                orderService.paymentFailed(intent.getId());
                log.error("Payment failed{}", intent.getId());
                break;

            default:
                log.info("Unhandled event type: {}", event.getType());
                break;
        }

        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
