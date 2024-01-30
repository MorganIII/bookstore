package org.morgan.bookstore.service;


import lombok.RequiredArgsConstructor;
import org.morgan.bookstore.dto.LoginRequest;
import org.morgan.bookstore.dto.LoginResponse;
import org.morgan.bookstore.dto.RegisterRequest;
import org.morgan.bookstore.entity.Role;
import org.morgan.bookstore.entity.User;
import org.morgan.bookstore.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    public String register(RegisterRequest request) {
        Optional<User> regUser = repository.findByUserName(request.email());
        if(regUser.isPresent()) {
            return "You are already registered";
        }
        var user = User.builder()
                .userFirstName(request.firstName())
                .userLastName(request.lastName())
                .userName(request.email())
                .userPassword(passwordEncoder.encode(request.password()))
                .role(Set.of(new Role("USER","")))
                .build();
        repository.save(user);
        return "registered successfully";
    }

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        Optional<User> regUser = repository.findByUserName(request.email());
        String jwtToken = jwtService.generateToken(regUser.get());
        return new LoginResponse(jwtToken);
    }

}
