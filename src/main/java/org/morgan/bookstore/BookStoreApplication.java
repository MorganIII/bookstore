package org.morgan.bookstore;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAsync
public class BookStoreApplication {

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;
    public static void main(String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);
    }

    @PostConstruct
    public void setup() {
        Stripe.apiKey = stripeSecretKey;
    }


}
