package com.example.Artisan.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.example.Artisan.entities.Role.ARTIST;
import static com.example.Artisan.entities.Role.USER;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**"
    };
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers(WHITE_LIST_URL).permitAll()
                            .requestMatchers("/api/v1/artist/all").hasAnyAuthority(USER.name())
                            .requestMatchers("/api/v1/artist/profile").hasAnyAuthority(ARTIST.name())
                            .requestMatchers("/api/v1/artist/profile/details/{artistId}").hasAnyAuthority(USER.name())
                            .requestMatchers("/api/v1/artist/**").hasAnyAuthority(ARTIST.name())
                            .requestMatchers("/api/v1/order/history").hasAnyAuthority(USER.name())
                            .requestMatchers("/api/v1/user/**").hasAuthority(USER.name())
                            .anyRequest().authenticated()
                    )
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                    .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
            return http.build();
    }
}
