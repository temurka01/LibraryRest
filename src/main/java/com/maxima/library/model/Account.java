package com.maxima.library.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    @Column(name = "registration_date")
    private String registrationDate;
    @Enumerated(value = EnumType.STRING)
    private Role role;
    @Enumerated(value = EnumType.STRING)
    private State state;
    private String password;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(this.role.name()));
    }

    /**
     * Меняет роль на ADMIN
     *
     * @return сущность измененного аккаунта
     */
    public Account toAdmin() {
        this.role = Role.ADMIN;
        return this;
    }

    public enum Role {
        ADMIN, USER
    }

    public enum State {
        CONFIRMED, NOT_CONFIRMED, BANNED, DELETED
    }
}