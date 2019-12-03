package com.sewerynkamil.librarymanager.security;

import com.sewerynkamil.librarymanager.client.LibraryManagerUsersClient;
import com.sewerynkamil.librarymanager.dto.UserDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Author Kamil Seweryn
 */

public class CurrentUser {
    public UserDto getCurrentUser(LibraryManagerUsersClient usersClient) {
        return usersClient.getOneUserByEmail(getPrincipalUsername());
    }

    private String getPrincipalUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }
}