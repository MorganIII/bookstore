package org.morgan.bookstore.configuration;


import lombok.RequiredArgsConstructor;
import org.morgan.bookstore.enums.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtFilter;
    private final AuthenticationProvider authenticationProvider;
    private static final String[] WHITE_LIST = {
            "/api/auth/**", "/api/books/image", "/api/books/title","/api/books/category","/api/books/details/{id}",
            "/api/sections/all", "/api/categories/all","/api/categories/section", "/v2/api-docs", "/v3/api-docs",
            "v3/api-docs/**", "/swagger-resources", "/swagger-resources/**", "/configuration/ui", "/configuration/security",
            "/swagger-ui/**", "/webjars/**", "/swagger-ui.html"
    };

    private static final String[] ADMIN_LIST = {
            "/api/coupons/**", "/api/sections/{name}", "/api/categories/{name}", "/api/books/{id}", "/api/orders/status/{id}"
    };


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize-> authorize
                        .requestMatchers(WHITE_LIST).permitAll()
                        .requestMatchers(ADMIN_LIST).hasRole(Role.ADMIN.name())
                        .requestMatchers(POST,"/api/sections").hasRole(Role.ADMIN.name())
                        .requestMatchers(POST,"/api/categories").hasRole(Role.ADMIN.name())
                        .requestMatchers(POST,"/api/books").hasRole(Role.ADMIN.name())
                        .requestMatchers(GET,"/api/wishlists//{wishlist-id}").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore( jwtFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
}
