package org.morgan.bookstore.model;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@AllArgsConstructor
public class Principal implements UserDetails {

    private User user;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        user.getRole().forEach((role)-> {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_"+role.getRole());
            authorities.add(grantedAuthority);
        });
        return authorities;
    }

    public Integer getId() {
        return user.getId();
    }
    @Override
    public String getPassword() {
        return user.getPassword();
    }
    @Override
    public String getUsername() {
        return user.getEmail();
    }
    @Override
    public boolean isEnabled() {
        return user.getIsEnabled();
    }
}
