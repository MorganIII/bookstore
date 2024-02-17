package org.morgan.bookstore;

import lombok.RequiredArgsConstructor;
import org.morgan.bookstore.model.Role;
import org.morgan.bookstore.model.User;
import org.morgan.bookstore.repository.UserRepository;
import org.morgan.bookstore.request.LoginRequest;
import org.morgan.bookstore.service.AuthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAsync
@RequiredArgsConstructor
public class BookStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);
    }


    @Bean
    CommandLineRunner commandLineRunner(
            AuthService authService,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {

        return args -> {
            User user = User.builder()
                    .email("morgan@gmail.com")
                    .firstName("mosaab")
                    .lastName("morgan")
                    .isEnabled(true)
                    .password(passwordEncoder.encode("123"))
                    .role(Set.of(new Role("USER","")))
                    .build();
            userRepository.save(user);
            System.out.println("user token: "+
                    authService.login(new LoginRequest("morgan@gmail.com","123")));
        };
    }


}
