package com.maxima.library.controller;

import com.maxima.library.dto.BookDto;
import com.maxima.library.model.Book;
import com.maxima.library.service.LibrarianService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {
    private final LibrarianService librarianService;

    @PostMapping("/add-book")
    public void addBook(@Valid @RequestBody BookDto bookDto) {
        librarianService.addBook(bookDto);
    }

    @DeleteMapping("/delete-book/{bookId}")
    public void deleteBook(@PathVariable Long bookId) {
        librarianService.deleteBook(bookId);
    }

    @GetMapping("/all-books")
    public ResponseEntity<List<Book>> allBooks() {
        return ResponseEntity.ok(librarianService.allBooks());
    }

    @GetMapping("/get-book/{bookId}")
    public ResponseEntity<Book> getBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(librarianService.getBook(bookId));
    }
}