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
public enum Role {
    USER("User"),
    ADMIN("Admin");

    String role;

    public static List<String> roleList() {
        return Stream.of(Role.values()).map(Role::getRole).collect(toList());
    }
}