package com.maxima.library.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BorrowRecordDto {
    private Long userId;
    private Long bookId;
    private String checkOutDate;
    private String returnDate;
}