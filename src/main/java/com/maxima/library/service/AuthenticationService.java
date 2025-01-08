package com.maxima.library.service;

import com.maxima.library.dto.AccountDto;
import com.maxima.library.dto.JwtAuthenticationResponse;
import com.maxima.library.dto.SignInDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AccountService accountService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

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
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signInDto.getUsername(),
                signInDto.getPassword()
        ));

        return new JwtAuthenticationResponse(jwtService
                .generateToken(accountService
                .userDetailsService()
                .loadUserByUsername(signInDto.getUsername())));
    }
}