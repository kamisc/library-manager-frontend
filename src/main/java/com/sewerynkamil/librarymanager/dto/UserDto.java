package com.sewerynkamil.librarymanager.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Author Kamil Seweryn
 */

@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    private String name;
    private String surname;
    private String email;
    private Integer phoneNumber;
    private String password;

    public UserDto(String name, String surname, String email, Integer phoneNumber, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }
}