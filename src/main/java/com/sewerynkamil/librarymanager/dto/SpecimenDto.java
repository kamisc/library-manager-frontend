package com.sewerynkamil.librarymanager.dto;

import com.sewerynkamil.librarymanager.dto.enumerated.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Author Kamil Seweryn
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SpecimenDto {
    private Long id;
    private Status status;
    private String publisher;
    private Integer yearOfPublication;
    private String bookTitle;
}