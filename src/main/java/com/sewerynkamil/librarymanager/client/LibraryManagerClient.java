package com.sewerynkamil.librarymanager.client;

import com.google.gson.Gson;
import com.sewerynkamil.librarymanager.dto.RequestJwtDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Author Kamil Seweryn
 */

@Component
public class LibraryManagerClient {
    @Autowired
    private RestTemplate restTemplate;

    private HttpHeaders headers = new HttpHeaders();

    private String jwttoken;

    public String createAuthenticationToken(RequestJwtDto authenticationRequest){
        Gson gson = new Gson();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonContent = gson.toJson(authenticationRequest);
        HttpEntity<String> request = new HttpEntity<>(jsonContent, headers);
        return restTemplate.postForObject("http://localhost:8080/v1/login", request, String.class);
    }

    public void setJwttoken(String jwttoken) {
        this.jwttoken = jwttoken;
    }
}