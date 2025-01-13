package com.maxima.library.service;

import com.maxima.library.dto.AccountDto;
import com.maxima.library.dto.JwtAuthenticationResponse;
import com.maxima.library.dto.SignInDto;
import com.maxima.library.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private AccountService accountService;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationService authenticationService;

    private AccountDto accountDto;
    private SignInDto signInDto;
    private Account account;
    private String token;

    @BeforeEach
    void setUp() {
        accountDto = AccountDto.builder()
                .email("test@example.com")
                .username("testUser")
                .password("password")
                .build();

        signInDto = SignInDto.builder()
                .username("testUser")
                .password("password")
                .build();

        account = Account.builder()
                .id(1L)
                .email("test@example.com")
                .username("testUser")
                .password("password")
                .build();

        token = "test_jwt_token";
    }

    @Test
    void signUp_Success() {
        when(accountService.addAccount(accountDto)).thenReturn(account);
        when(jwtService.generateToken(account)).thenReturn(token);

        JwtAuthenticationResponse response = authenticationService.signUp(accountDto);

        assertNotNull(response);
        assertEquals(token, response.getToken());
        verify(accountService, times(1)).addAccount(accountDto);
        verify(jwtService, times(1)).generateToken(account);
    }

    @Test
    void signUp_AccountCreationFails() {
        when(accountService.addAccount(accountDto)).thenThrow(new UsernameNotFoundException("Данные уже используются"));

        assertNull(authenticationService.signUp(accountDto).getToken());
        verify(accountService, times(1)).addAccount(accountDto);
    }

    @Test
    void signIn_Success() {
        when(accountService.getByUsername(signInDto.getUsername())).thenReturn(account);
        when(jwtService.generateToken(account)).thenReturn(token);
        when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(true);

        JwtAuthenticationResponse response = authenticationService.signIn(signInDto);

        assertNotNull(response);
        assertEquals(token, response.getToken());
        verify(accountService, times(1)).getByUsername(signInDto.getUsername());
        verify(jwtService, times(1)).generateToken(account);
    }

    @Test
    void signIn_IncorrectPassword() {
        when(accountService.getByUsername(signInDto.getUsername())).thenReturn(account);
        when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(false);

        assertThrows(RuntimeException.class, () -> authenticationService.signIn(signInDto));
    }

    @Test
    void signIn_UserNotFound() {
        when(accountService.getByUsername(signInDto.getUsername())).thenThrow(new UsernameNotFoundException("Пользователь не найден"));

        assertThrows(UsernameNotFoundException.class, () -> authenticationService.signIn(signInDto));
        verify(accountService, times(1)).getByUsername(signInDto.getUsername());
        verify(jwtService, never()).generateToken(any());
    }
}