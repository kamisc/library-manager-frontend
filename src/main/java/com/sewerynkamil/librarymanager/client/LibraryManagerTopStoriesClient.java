package com.sewerynkamil.librarymanager.client;

import com.sewerynkamil.librarymanager.dto.nytimes.NYTimesTopStoriesDto;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Author Kamil Seweryn
 */

@Component
public class LibraryManagerTopStoriesClient {
    @Autowired
    private RestTemplate restTemplate;

    private HttpHeaders headers = new HttpHeaders();

    public NYTimesTopStoriesDto getAllTopStoriesBySection(String section) {
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
        HttpEntity<String> request = new HttpEntity<>("authentication", headers);
        ResponseEntity<NYTimesTopStoriesDto> response =
                restTemplate.exchange(
                        "http://localhost:8080/v1/topstories/" + section,
                        HttpMethod.GET,
                        request,
                        NYTimesTopStoriesDto.class);
        return response.getBody();
    }
}