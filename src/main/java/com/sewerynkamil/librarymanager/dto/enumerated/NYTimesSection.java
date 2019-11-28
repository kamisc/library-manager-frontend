package com.sewerynkamil.librarymanager.dto.enumerated;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Author Kamil Seweryn
 */

@AllArgsConstructor
@Getter
public enum NYTimesSection {
    ARTS("Arts"),
    AUTOMOBILES("Automobiles"),
    BOOKS("Books"),
    BUSINESS("Business"),
    FASHION("Fashion"),
    FOOD("Food"),
    HEALTH("Health"),
    HOME("Home"),
    INSIDER("Insider"),
    MAGAZINE("Magazine"),
    MOVIES("Movies"),
    NATIONAL("National"),
    NYREGION("NYRegion"),
    OBITUARIES("Obituaries"),
    OPINION("Opinion"),
    POLITICS("Politics"),
    REALESTATE("Real estate"),
    SCIENCE("Science"),
    SPORTS("Sports"),
    SUNDAYREVIEW("Sunday review"),
    TECHNOLOGY("Technology"),
    THEATER("Theater"),
    TMAGAZINE("TMagazine"),
    TRAVEL("Travel"),
    UPSHOT("Upshot"),
    WORLD("World");

    String section;

    public static List<String> sectionList() {
        return Stream.of(NYTimesSection.values()).map(NYTimesSection::getSection).collect(toList());
    }
}