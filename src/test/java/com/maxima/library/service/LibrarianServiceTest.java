package com.maxima.library.service;

import com.maxima.library.dto.BookDto;
import com.maxima.library.mapper.MyMapper;
import com.maxima.library.model.Author;
import com.maxima.library.model.Book;
import com.maxima.library.repository.AuthorRepository;
import com.maxima.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibrarianServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private MyMapper mapper;

    @InjectMocks
    private LibrarianService librarianService;

    private BookDto bookDto;
    private Author author;
    private Book book;

    @BeforeEach
    void setUp() {
        bookDto = BookDto.builder()
                .name("Test Author")
                .title("Test Book")
                .country("Test country")
                .releaseYear(2024)
                .amount(5)
                .build();

        author = Author.builder()
                .id(1L)
                .name("Test Author")
                .country("Test country")
                .build();

        book = Book.builder()
                .id(1L)
                .title("Test Book")
                .author(author)
                .releaseYear(2024)
                .amount(5)
                .build();
    }

    @Test
    void addBook_AuthorAndBookExists_IncreaseAmount() {
        when(authorRepository.findOne(any(Example.class))).thenReturn(Optional.of(author));
        when(bookRepository.findOne(any(Example.class))).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        librarianService.addBook(bookDto);

        verify(bookRepository, times(1)).save(argThat(updatedBook -> updatedBook.getAmount() == 10));
    }

    @Test
    void addBook_AuthorExistsBookNotExists_AddNewBook() {
        when(authorRepository.findOne(any(Example.class))).thenReturn(Optional.of(author));
        when(bookRepository.findOne(any(Example.class))).thenReturn(Optional.empty());
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        librarianService.addBook(bookDto);

        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void addBook_AuthorNotExists_AddNewAuthorAndBook() {
        when(authorRepository.findOne(any(Example.class))).thenReturn(Optional.empty());
        when(authorRepository.save(any(Author.class))).thenReturn(author);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        librarianService.addBook(bookDto);

        verify(authorRepository, times(1)).save(any(Author.class));
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void deleteBook() {
        Long id = 1L;
        librarianService.deleteBook(id);
        verify(bookRepository, times(1)).deleteById(id);
    }

    @Test
    void allBooks() {
        List<Book> bookList = List.of(book);
        when(bookRepository.findAll()).thenReturn(bookList);
        List<Book> result = librarianService.allBooks();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(bookList, result);
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void getBook_Success() {
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        Book result = librarianService.getBook(id);
        assertNotNull(result);
        assertEquals(book, result);
    }

    @Test
    void getBook_NotFound() {
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        Book result = librarianService.getBook(id);
        assertNull(result);
    }
}