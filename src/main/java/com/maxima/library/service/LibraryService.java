package com.maxima.library.service;

import com.maxima.library.dto.BorrowRecordDto;
import com.maxima.library.mapper.MyMapper;
import com.maxima.library.model.Book;
import com.maxima.library.model.BorrowRecord;
import com.maxima.library.repository.BookRepository;
import com.maxima.library.repository.BorrowRecordRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Сервис для выдачи и возврата книг
 */
@Service
@RequiredArgsConstructor
public class LibraryService {
    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final JwtService jwtService;
    private final MyMapper mapper = Mappers.getMapper(MyMapper.class);
    public static final String BEARER_PREFIX = "Bearer ";

    /**
     * Добавляет запись о взятой книге и уменьшает количество этой книги на 1
     *
     * @param id    id книги
     * @param token jwt токен
     * @return данные о сохраненной записи
     */

    public BorrowRecordDto borrowBook(Long id, String token) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            if (book.get().getAmount() > 0) {
                changeAmount(book.get(), -1);
                return mapper.toBorrowRecordDto(borrowRecordRepository.save(new BorrowRecord(
                        null,
                        jwtService.extractId(token.substring(BEARER_PREFIX.length())),
                        id,
                        todayToString(),
                        null)));
            }
        }
        return null;
    }

    /**
     * Добавляет к записи о взятой книге дату возврата и увеличивает количество этой книги на 1
     *
     * @param id    id книги
     * @param token jwt токен
     * @return данные о сохраненной записи
     */
    public BorrowRecordDto returnBook(Long id, String token) {
        List<BorrowRecord> list = borrowRecordRepository
                .findAll(Example.of(new BorrowRecord(null, jwtService.extractId(token.substring(BEARER_PREFIX.length())), id, null, null)))
                .stream()
                .filter(value -> value.getReturnDate() == null)
                .sorted(Comparator.comparing(BorrowRecord::getCheckOutDate))
                .limit(1)
                .toList();
        if (!list.isEmpty()) {
            BorrowRecord borrowRecord = list.get(0);
            borrowRecord.setReturnDate(todayToString());
            bookRepository.findById(id).ifPresent(value -> changeAmount(value, 1));
            return mapper.toBorrowRecordDto(borrowRecordRepository.save(borrowRecord));
        } else {
            throw (new NoSuchElementException("Запись не найдена"));
        }
    }

    /**
     * Изменяет количество книги на указанное значение
     *
     * @param book   обьект книги
     * @param amount значение на которое нужно изменить
     */
    private void changeAmount(Book book, int amount) {
        bookRepository.save(book.changeAmount(amount));
    }

    /**
     * @return текущую дату в строку
     */
    private String todayToString() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }
}