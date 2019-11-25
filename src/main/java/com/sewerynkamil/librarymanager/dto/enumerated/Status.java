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
public enum Status {
    AVAILABLE("Available"),
    RENTED("Rented"),
    LOST("Lost");

    String status;

    public static List<String> statusList() {
        return Stream.of(Status.values()).map(Status::getStatus).collect(toList());
    }
}