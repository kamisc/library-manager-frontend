package com.sewerynkamil.librarymanager.dto.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Author Kamil Seweryn
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestJwtDto {
    private String username;
    private String password;
}