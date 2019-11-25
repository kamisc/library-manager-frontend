package com.sewerynkamil.librarymanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Author Kamil Seweryn
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RentDto {
    private Long rentId;
    private Long specimenId;
    private String bookTitle;
    private String userEmail;
    private LocalDate rentDate;
    private LocalDate returnDate;
}
