package com.maxima.library.service;

import com.maxima.library.dto.BorrowRecordDto;
import com.maxima.library.mapper.MyMapper;
import com.maxima.library.model.Author;
import com.maxima.library.model.Book;
import com.maxima.library.model.BorrowRecord;
import com.maxima.library.repository.BookRepository;
import com.maxima.library.repository.BorrowRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibraryServiceTest {

    @Mock
    private BorrowRecordRepository borrowRecordRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private MyMapper mapper;

    @InjectMocks
    private LibraryService libraryService;

    private Book book;
    private BorrowRecord borrowRecord;
    private BorrowRecordDto borrowRecordDto;
    private String token;
    private Long userId;
    private Long bookId;
    private String today;


    @BeforeEach
    void setUp() {
        bookId = 1L;
        userId = 10L;
        today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        token = "Bearer test_jwt_token";
        book = Book.builder()
                .id(bookId)
                .title("Test Book")
                .author(Author.builder()
                        .id(1L)
                        .name("Test Author")
                        .country("Test country")
                        .build())
                .releaseYear(2024)
                .amount(5)
                .build();
        borrowRecord = new BorrowRecord(
                1L,
                userId,
                bookId,
                today,
                null);
        borrowRecordDto = BorrowRecordDto.builder()
                .checkOutDate(today)
                .bookId(bookId)
                .userId(userId)
                .build();
    }

    @Test
    void borrowBook_Success() {
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(jwtService.extractId("test_jwt_token")).thenReturn(userId);
        when(borrowRecordRepository.save(any(BorrowRecord.class))).thenReturn(borrowRecord);
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BorrowRecordDto result = libraryService.borrowBook(bookId, token);

        assertNotNull(result);
        assertEquals(borrowRecordDto, result);
        verify(bookRepository, times(1)).save(argThat(updatedBook -> updatedBook.getAmount() == 4));
        verify(borrowRecordRepository, times(1)).save(any(BorrowRecord.class));
    }

    @Test
    void borrowBook_BookNotFound() {
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        BorrowRecordDto result = libraryService.borrowBook(bookId, token);

        assertNull(result);
        verify(borrowRecordRepository, never()).save(any(BorrowRecord.class));
        verify(bookRepository, never()).save(any(Book.class));
        verify(mapper, never()).toBorrowRecordDto(any(BorrowRecord.class));

    }

    @Test
    void borrowBook_NoBooksAvailable() {
        book.setAmount(0);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        BorrowRecordDto result = libraryService.borrowBook(bookId, token);

        assertNull(result);
        verify(borrowRecordRepository, never()).save(any(BorrowRecord.class));
        verify(bookRepository, never()).save(any(Book.class));
        verify(mapper, never()).toBorrowRecordDto(any(BorrowRecord.class));
    }

    @Test
    void returnBook_Success() {
        when(jwtService.extractId("test_jwt_token")).thenReturn(userId);
        when(borrowRecordRepository.findAll(any(Example.class))).thenReturn(List.of(borrowRecord));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(borrowRecordRepository.save(any(BorrowRecord.class))).thenReturn(borrowRecord);
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));
        borrowRecordDto.setReturnDate(today);

        BorrowRecordDto result = libraryService.returnBook(bookId, token);

        assertNotNull(result);
        assertEquals(borrowRecordDto, result);
        verify(borrowRecordRepository, times(1)).save(argThat(record -> record.getReturnDate() != null));
        verify(bookRepository, times(1)).save(argThat(updatedBook -> updatedBook.getAmount() == 6));
    }

    @Test
    void returnBook_RecordNotFound() {
        when(jwtService.extractId("test_jwt_token")).thenReturn(userId);
        when(borrowRecordRepository.findAll(any(Example.class))).thenReturn(List.of());

        assertThrows(NoSuchElementException.class, () -> libraryService.returnBook(bookId, token));
        verify(borrowRecordRepository, never()).save(any());
        verify(bookRepository, never()).save(any());
        verify(mapper, never()).toBorrowRecordDto(any());
    }
}