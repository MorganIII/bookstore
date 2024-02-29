package org.morgan.bookstore.service;

import lombok.RequiredArgsConstructor;
import org.morgan.bookstore.enums.Role;
import org.morgan.bookstore.model.Principal;
import org.morgan.bookstore.model.User;
import org.morgan.bookstore.repository.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public User getUserById(Integer id) {
        return repository.findUserById(id).
                orElseThrow(()-> new UsernameNotFoundException(String.format("user with email: %s not found",id)));
    }


    public Integer userId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Principal principal = (Principal) authentication.getPrincipal();
        return principal.getId();
    }

    public Integer getCurrent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Principal principal = (Principal) authentication.getPrincipal();
        boolean isAdmin = principal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_" +Role.ADMIN));
        if(isAdmin){
            return null;
        }
        return principal.getId();
    }

    public boolean isAnonymousUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication instanceof AnonymousAuthenticationToken;
    }
}
