package com.maxima.library.service;

import com.maxima.library.dto.AccountDto;
import com.maxima.library.dto.JwtAuthenticationResponse;
import com.maxima.library.dto.SignInDto;
import com.maxima.library.model.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Сервис для регистрации и аутентификации пользователей
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AccountService accountService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Регистрация пользователя
     *
     * @param accountDto данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signUp(AccountDto accountDto) {
        Account account;
        try {
            account = accountService.addAccount(accountDto);
        } catch (UsernameNotFoundException e) {
            return new JwtAuthenticationResponse(null);
        }
        return new JwtAuthenticationResponse(jwtService.generateToken(account));
    }

    /**
     * Аутентификация пользователя
     *
     * @param signInDto данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signIn(SignInDto signInDto) {
        Account account = accountService.getByUsername(signInDto.getUsername());
        if (passwordEncoder.matches(signInDto.getPassword(), account.getPassword())) {
            return new JwtAuthenticationResponse(jwtService.generateToken(account));
        } else {
            throw (new RuntimeException("Пароль не верный"));
        }
    }
}