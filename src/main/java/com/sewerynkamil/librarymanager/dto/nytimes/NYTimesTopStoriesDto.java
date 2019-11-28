package com.sewerynkamil.librarymanager.dto.nytimes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Author Kamil Seweryn
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NYTimesTopStoriesDto {
    private List<NYTimesResultsDto> results;
}