package com.sewerynkamil.librarymanager.dto.enumerated;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
}