package com.sewerynkamil.librarymanager.dto.nytimes;

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
public class NYTimesResultsDto {
    private String section;
    private String title;
    private String url;
    private String byline;
}