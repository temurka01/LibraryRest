package com.maxima.library.controller;

import com.maxima.library.dto.JwtAuthenticationResponse;
import com.maxima.library.dto.AccountDto;
import com.maxima.library.dto.SignInDto;
import com.maxima.library.service.AccountService;
import com.maxima.library.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final AuthenticationService authenticationService;

    @PostMapping("/add-account")
    public ResponseEntity<JwtAuthenticationResponse> createAccount(@Valid @RequestBody AccountDto accountDto) {
        return ResponseEntity.ok(authenticationService.signUp(accountDto));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@Valid @RequestBody SignInDto signInDto) {
        return ResponseEntity.ok(authenticationService.signIn(signInDto));
    }

    @DeleteMapping("/delete-account/{id}")
    public void deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
    }

    @GetMapping("/make-admin/{id}")
    public void makeAdmin(@PathVariable Long id) {
        accountService.makeAdmin(id);
    }
}
