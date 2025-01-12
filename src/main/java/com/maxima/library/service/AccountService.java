package com.maxima.library.service;

import com.maxima.library.dto.AccountDto;
import com.maxima.library.mapper.MyMapper;
import com.maxima.library.model.Account;
import com.maxima.library.repository.AccountRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Сервис для работы с аккаунтами
 */
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final MyMapper mapper = Mappers.getMapper(MyMapper.class);

    /**
     * Добавляет новый аккаунт
     *
     * @param accountDto данные об аккаунте
     */
    public Account addAccount(AccountDto accountDto) {
        //ищет совпадения по email и username
        if (!(accountRepository.existsByEmail(accountDto.getEmail()) && !accountRepository.existsByUsername(accountDto.getUsername()))) {
            //если совпадений нет, сохраняет
            return accountRepository.save(mapper.toAccount(accountDto));
        } else {
            throw (new UsernameNotFoundException("Данные уже используются"));
        }
    }

    /**
     * Удаляет аккаунт
     *
     * @param id id аккаунта
     */
    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }

    /**
     * Находит аккаунт по логину
     *
     * @param username логин пользователя
     * @return сущность аккаунта
     */
    public Account getByUsername(String username) {
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    /**
     * Делает пользователя под заданным id админом
     *
     * @param id id пользователя
     */
    public void makeAdmin(Long id) {
        accountRepository.findById(id).ifPresent(value -> accountRepository.save(value.toAdmin()));
    }

    /**
     * Создает начального пользователя с именем, паролем и ролью admin
     */
    @PostConstruct
    private void addAdmin() {
        makeAdmin(addAccount(AccountDto.builder()
                .email("admin@mail.ru")
                .username("admin")
                .password("admin")
                .build()).getId());
    }
}