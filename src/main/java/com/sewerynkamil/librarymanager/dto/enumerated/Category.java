package com.sewerynkamil.librarymanager.dto.enumerated;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Author Kamil Seweryn
 */

@Getter
@AllArgsConstructor
public enum  Category {
    ACTION("Action"),
    ADVENTURE("Adventure"),
    AUTOBIOGRAPHY("Autobiography"),
    BIOGRAPHY("Biography"),
    DIARY("Diary"),
    DRAMA("Drama"),
    CLASSIC("Classic"),
    COMIC("Comic"),
    CRIME("Crime"),
    DETECTIVE("Detective"),
    FABLE("Fable"),
    FANTASY("Fantasy"),
    HISTORICAL("Historical"),
    HUMOR("Humor"),
    HORROR("Horror"),
    POETRY("Poetry"),
    ROMANCE("Romance"),
    SCIENCE_FICTION("Science fiction"),
    THRILLER("Thriller"),
    TRAGEDY("Tragedy");

    String name;

    public static List<String> categoryList() {
        return Stream.of(Category.values()).map(Category::getName).collect(toList());
    }
}
