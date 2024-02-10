package org.morgan.bookstore.service;

import lombok.RequiredArgsConstructor;
import org.morgan.bookstore.model.Principal;
import org.morgan.bookstore.model.User;
import org.morgan.bookstore.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
         User user = repository.findUserByEmail(email).
                orElseThrow(()-> new UsernameNotFoundException(String.format("user with email: %s not found",email)));
        return new Principal(user);
    }
}
