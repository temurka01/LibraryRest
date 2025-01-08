package com.maxima.library.repository;

import com.maxima.library.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String name);
}
