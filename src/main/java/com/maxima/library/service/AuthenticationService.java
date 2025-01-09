package com.maxima.library.service;

import com.maxima.library.dto.AccountDto;
import com.maxima.library.dto.JwtAuthenticationResponse;
import com.maxima.library.dto.SignInDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Сервис для регистрации и аутентификации пользователей
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AccountService accountService;
    private final JwtService jwtService;

    /**
     * Регистрация пользователя
     *
     * @param accountDto данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signUp(AccountDto accountDto) {
        return new JwtAuthenticationResponse(jwtService.generateToken(accountService.addAccount(accountDto)));
    }

    /**
     * Аутентификация пользователя
     *
     * @param signInDto данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signIn(SignInDto signInDto) {
        return new JwtAuthenticationResponse(jwtService.generateToken(accountService.getByUsername(signInDto.getUsername())));
    }
}