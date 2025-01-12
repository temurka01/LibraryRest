package com.maxima.library.mapper;

import com.maxima.library.dto.AccountDto;
import com.maxima.library.dto.BookDto;
import com.maxima.library.dto.BorrowRecordDto;
import com.maxima.library.model.Account;
import com.maxima.library.model.Author;
import com.maxima.library.model.Book;
import com.maxima.library.model.BorrowRecord;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoSettings;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@MockitoSettings
public class MyMapperTest {

    @InjectMocks
    private MyMapper myMapper = Mappers.getMapper(MyMapper.class);

    public MyMapperTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testToAccount() {
        AccountDto dto = new AccountDto("user", "user@example.com", "password123");
        Account account = myMapper.toAccount(dto);

        assertEquals("user", account.getUsername());
        assertEquals("user@example.com", account.getEmail());
        assertNotNull(account.getRegistrationDate());
        assertEquals("USER", account.getRole().name());
        assertEquals("CONFIRMED", account.getState().name());
        assertNotNull(account.getPassword());
    }

    @Test
    public void testToAuthor() {
        BookDto bookDto = new BookDto("title", "authorName", "authorCountry", 2021, 10);
        Author author = myMapper.toAuthor(bookDto);

        assertEquals("authorName", author.getName());
        assertEquals("authorCountry", author.getCountry());
    }

    @Test
    public void testToBookNoAmount() {
        BookDto bookDto = new BookDto("title", "authorName", "authorCountry", 2021, 10);
        Author author = Author.builder()
                .name("authorName")
                .country("authorCountry")
                .build();
        Book book = myMapper.toBookNoAmount(bookDto, author);

        assertEquals("title", book.getTitle());
        assertEquals(author, book.getAuthor());
        assertNull(book.getAmount());
    }

    @Test
    public void testToBook() {
        BookDto bookDto = new BookDto("title", "authorName", "authorCountry", 2021, 10);
        Author author = new Author(1L, "authorName", "authorCountry");
        Book book = myMapper.toBook(bookDto, author);

        assertEquals("authorName", book.getAuthor().getName());
        assertEquals("authorCountry", book.getAuthor().getCountry());
        assertEquals(1L, book.getAuthor().getId());
        assertEquals("title", book.getTitle());
        assertEquals(2021, book.getReleaseYear());
        assertEquals(10, book.getAmount());
        assertNull(book.getId());
    }

    @Test
    public void testToBorrowRecordDto() {
        BorrowRecord record = BorrowRecord.builder()
                .userId(1L)
                .bookId(1L)
                .build();
        BorrowRecordDto recordDto = myMapper.toBorrowRecordDto(record);

        assertEquals(1L, recordDto.getUserId());
        assertEquals(1L, recordDto.getBookId());
    }

    @Test
    public void testEncodePassword() {
        assertNotEquals("password123", myMapper.encodePassword("password123"));
    }

    @Test
    public void testTodayToString() {
        assertEquals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), myMapper.todayToString());
    }
}