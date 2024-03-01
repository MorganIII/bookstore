package org.morgan.bookstore.service;


import lombok.RequiredArgsConstructor;
import org.morgan.bookstore.enums.Role;
import org.morgan.bookstore.exception.UserException;
import org.morgan.bookstore.repository.AuthoritiesRepository;
import org.morgan.bookstore.repository.CartRepository;
import org.morgan.bookstore.request.LoginRequest;
import org.morgan.bookstore.response.LoginResponse;
import org.morgan.bookstore.request.RegisterRequest;
import org.morgan.bookstore.model.User;
import org.morgan.bookstore.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final AuthoritiesRepository authoritiesRepository;
    private final CartRepository cartRepository;
    public String register(RegisterRequest request) {
        if(isUserExist(request.getEmail())) {
            return "You are already registered";
        }
        String token = UUID.randomUUID().toString();
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .displayName(request.getFirstName() +" "+ request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isEnabled(true)
                .token(token)
                .authorities(Set.of(authoritiesRepository.findAuthoritiesByRole(Role.USER)))
                .build();
        emailService.sendEmail(request.getFirstName(), request.getEmail(),token);
        user = repository.save(user);
        cartRepository.save(CartService.createCart(null, user));
        return "registered successfully";
    }

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User regUser = repository.findUserByEmail(request.getEmail()).orElse(null);
        String jwtToken = jwtService.generateToken(regUser);
        return new LoginResponse(jwtToken);
    }

    public String verifyAccount(String token) {
        User user =  repository.findUserByToken(token).
                orElseThrow(()-> new UserException(String.format("token %s not valid", token)));
        if(Boolean.TRUE.equals(user.getIsEnabled())) {
            return "you are already verified your account";
        }
        user.setIsEnabled(true);
        repository.save(user);
        return "Account verified";
    }

    private boolean isUserExist(String email) {
        return repository.findUserByEmail(email).isPresent();
    }



}
