package com.maxima.library.service;

import com.maxima.library.dto.AccountDto;
import com.maxima.library.model.Account;
import com.maxima.library.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService target;

    private AccountDto accountDto;
    private Account account;

    @BeforeEach
    void setUp() {
        accountDto = AccountDto.builder()
                .email("test@example.com")
                .username("testuser")
                .password("password")
                .build();

        account = Account.builder()
                .id(1L)
                .email("test@example.com")
                .username("testuser")
                .state(Account.State.CONFIRMED)
                .role(Account.Role.USER)
                .registrationDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))
                .password("password")
                .build();
    }


    @Test
    void addAccount_Success() {
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account result = target.addAccount(accountDto);

        assertNotNull(result);
        assertEquals(account, result);
    }

    @Test
    void addAccount_EmailExists_ReturnNull() {
        when(accountRepository.existsByEmail(accountDto.getEmail())).thenReturn(true);
        when(accountRepository.existsByUsername(accountDto.getUsername())).thenReturn(false);

        assertThrows(UsernameNotFoundException.class, () -> target.addAccount(accountDto));
    }

    @Test
    void addAccount_UsernameExists_ReturnNull() {
        when(accountRepository.existsByEmail(accountDto.getEmail())).thenReturn(false);

        Account result = target.addAccount(accountDto);

        assertNull(result);
    }

    @Test
    void deleteAccount() {
        target.deleteAccount(1L);
        verify(accountRepository, times(1)).deleteById(1L);
    }

    @Test
    void getByUsername_Success() {
        String username = "testuser";
        when(accountRepository.findByUsername(username)).thenReturn(Optional.of(account));

        Account result = target.getByUsername(username);

        assertNotNull(result);
        assertEquals(account, result);
    }

    @Test
    void getByUsername_NotFound() {
        String username = "nonexistentuser";
        when(accountRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> target.getByUsername(username));
    }

    @Test
    void makeAdmin_Success() {
        Long id = 1L;
        when(accountRepository.findById(id)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        target.makeAdmin(id);

        verify(accountRepository, times(1)).save(argThat(updatedAccount -> updatedAccount.getRole().equals(Account.Role.ADMIN)));
    }

    @Test
    void makeAdmin_NotFound() {
        Long id = 1L;
        when(accountRepository.findById(id)).thenReturn(Optional.empty());
        target.makeAdmin(id);
        verify(accountRepository, never()).save(any());
    }
}
