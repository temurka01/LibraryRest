package com.maxima.library.mapper;

import com.maxima.library.dto.*;
import com.maxima.library.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.SimpleDateFormat;
import java.util.Date;

@Mapper
public interface MyMapper {
    @Mapping(target = "registrationDate", expression = "java(todayToString())")
    @Mapping(target = "password", qualifiedByName = "encodePassword", source = "password")
    @Mapping(target = "username", source = "accountDto.username")
    @Mapping(target = "email", source = "accountDto.email")
    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "state", constant = "CONFIRMED")
    Account toAccount(AccountDto accountDto);

    @Mapping(target = "name", source = "bookDto.name")
    @Mapping(target = "country", source = "bookDto.country")
    Author toAuthor(BookDto bookDto);

    @Mapping(target = "amount", ignore = true)
    @Mapping(target = "author", source = "author")
    @Mapping(target = "id", ignore = true)
    Book toBookNoAmount(BookDto bookDto, Author author);

    @Mapping(target = "author.name", source = "author.name")
    @Mapping(target = "author.country", source = "author.country")
    @Mapping(target = "author.id", source = "author.id")
    @Mapping(target = "id", ignore = true)
    Book toBook(BookDto bookDto, Author author);

    BorrowRecordDto toBorrowRecordDto(BorrowRecord borrowRecord);

    default String todayToString() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    @Named("encodePassword")
    default String encodePassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }
}