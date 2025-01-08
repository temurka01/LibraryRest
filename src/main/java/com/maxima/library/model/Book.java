package com.maxima.library.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @ManyToOne
    private Author author;
    @Column(name = "release_year")
    private Integer releaseYear;
    private Integer amount;

    /**
     * Изменяет количество книги на указанное значение
     *
     * @param amount значение на которое нужно изменить
     * @return обьект измененной книги
     */
    public Book changeAmount(Integer amount) {
        this.amount = (this.amount + amount);
        return this;
    }
}
