package com.maxima.library.service;

import com.maxima.library.dto.BookDto;
import com.maxima.library.mapper.MyMapper;
import com.maxima.library.model.Book;
import com.maxima.library.repository.AuthorRepository;
import com.maxima.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для добавления и удаления книг
 */
@Service
@RequiredArgsConstructor
public class LibrarianService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final MyMapper mapper = Mappers.getMapper(MyMapper.class);


    /**
     * Добавляет книгу и автора
     *
     * @param bookDto данные о книге и ее авторе
     */
    public void addBook(BookDto bookDto) {
        //ищет автора в бд
        authorRepository.findOne(Example.of(mapper.toAuthor(bookDto)))
                .map(author1 -> bookRepository//если нашел
                        .findOne(Example.of(mapper.toBookNoAmount(bookDto, author1)))//ищет книгу в бд//
                        .map(value -> //если нашел//
                                bookRepository////увеличивает количество этой книги
                                        .save(value.changeAmount(bookDto.getAmount())))
                        .orElseGet(() -> //если не нашел//
                                bookRepository////сохраняет новую книгу с ссылкой на найденного автора
                                        .save(mapper.toBook(bookDto, author1))))
                .orElseGet(() -> //если не нашел
                        bookRepository////сохраняет нового автора и новую книгу с ссылкой на него
                                .save(mapper.toBook(bookDto, authorRepository
                                        .save(mapper.toAuthor(bookDto)))));
    }

    /**
     * Удаляет книгу по id
     *
     * @param id id книги
     */
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    /**
     * @return список всех книг
     */
    public List<Book> allBooks() {
        return bookRepository.findAll();
    }

    /**
     * Находит книгу по id
     *
     * @param id id книги
     * @return сущность книги
     */
    public Book getBook(Long id) {
        return bookRepository.findById(id).orElse(null);
    }
}