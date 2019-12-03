package com.sewerynkamil.librarymanager.client;

import com.google.gson.Gson;
import com.sewerynkamil.librarymanager.dto.authentication.RequestJwtDto;
import com.sewerynkamil.librarymanager.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Author Kamil Seweryn
 */

@Component
public class LibraryManagerAuthenticationClient {
    @Autowired
    private RestTemplate restTemplate;

    private HttpHeaders headers = new HttpHeaders();

    public String createAuthenticationToken(RequestJwtDto authenticationRequest){
        Gson gson = new Gson();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonContent = gson.toJson(authenticationRequest);
        HttpEntity<String> request = new HttpEntity<>(jsonContent, headers);
        return restTemplate.postForObject("http://localhost:8080/v1/login", request, String.class);
    }

    public void registerUser(UserDto userDto) {
        Gson gson = new Gson();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonContent = gson.toJson(userDto);
        HttpEntity<String> request = new HttpEntity<>(jsonContent, headers);
        restTemplate.postForObject("http://localhost:8080/v1/register", request, UserDto.class);
    }
}