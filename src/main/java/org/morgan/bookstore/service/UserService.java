package org.morgan.bookstore.service;

import lombok.RequiredArgsConstructor;
import org.morgan.bookstore.dto.Principal;
import org.morgan.bookstore.entity.User;
import org.morgan.bookstore.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = repository.findByUserName(username);
        if(user.isPresent()) {
            return new Principal(user.get());
        }
        throw new UsernameNotFoundException("user with username: "+ username+ "not found" );
    }
}
