package com.maxima.library.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class BorrowRecordDto {
    private Long userId;
    private Long bookId;
    private String checkOutDate;
    private String returnDate;
}