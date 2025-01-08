package com.maxima.library.controller;

import com.maxima.library.dto.BorrowRecordDto;
import com.maxima.library.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LibraryController {
    private final LibraryService libraryService;

    @GetMapping("/borrow/{bookId}")
    public ResponseEntity<BorrowRecordDto> borrowBook(@PathVariable Long bookId, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(libraryService.borrowBook(bookId, token));
    }

    @GetMapping("/return/{bookId}")
    public ResponseEntity<BorrowRecordDto> returnBook(@PathVariable Long bookId, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(libraryService.returnBook(bookId, token));
    }
}
