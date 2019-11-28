package com.sewerynkamil.librarymanager.dto.wolnelektury;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Author Kamil Seweryn
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WolneLekturyAudiobookDto {
    private String author;
    private String title;
    private String genre;
    private String epoch;
    private String url;
}