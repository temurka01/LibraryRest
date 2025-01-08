package com.maxima.library.config;

import com.maxima.library.config.security.filters.JwtAuthenticationFilter;
import com.maxima.library.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


@Configuration
@ComponentScan(basePackages = "com.maxima.library")
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class ApplicationConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AccountService accountService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowedOriginPatterns(List.of("*"));
                    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfiguration.setAllowedHeaders(List.of("*"));
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                }))
                // Настройка доступа к конечным точкам
                .authorizeHttpRequests(request -> request
                        // Можно указать конкретный путь, * - 1 уровень вложенности, ** - любое количество уровней вложенности
                        .requestMatchers("/add-book").hasAuthority("ADMIN")
                        .requestMatchers("/delete-book/**").hasAuthority("ADMIN")
                        .requestMatchers("/delete-account/**").hasAuthority("ADMIN")
                        .requestMatchers("/make-admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/add-account").permitAll()
                        .requestMatchers("/sign-in").permitAll()
                        .requestMatchers("/borrow/**").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers("/return/**").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers("/all-books").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers("/get-book").hasAnyAuthority("USER", "ADMIN")
                        .anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(accountService.userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
