package com.sewerynkamil.librarymanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Author Kamil Seweryn
 */

@AllArgsConstructor
@Getter
public class ResponseJwtDto {
    private final String jwttoken;

    @Override
    public String toString() {
        return "Bearer " + jwttoken;
    }
}