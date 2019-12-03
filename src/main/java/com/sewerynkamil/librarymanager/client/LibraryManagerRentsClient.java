package com.sewerynkamil.librarymanager.client;

import com.sewerynkamil.librarymanager.dto.BookDto;
import com.sewerynkamil.librarymanager.dto.RentDto;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Author Kamil Seweryn
 */

@Component
public class LibraryManagerRentsClient {
    @Autowired
    private RestTemplate restTemplate;

    private HttpHeaders headers = new HttpHeaders();

    public List<RentDto> getAllRentsWithLazyLoading(int offset, int limit) {
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
        HttpEntity<String> request = new HttpEntity<>("authentication", headers);
        ResponseEntity<RentDto[]> response =
                restTemplate.exchange(
                        "http://localhost:8080/v1/rents?offset=" + offset + "&limit=" + limit,
                        HttpMethod.GET,
                        request,
                        RentDto[].class);
        List<RentDto> responseList = Arrays.asList(response.getBody());
        return responseList;
    }

    public List<RentDto> getAllRentsByUserId(Long userId) {
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
        HttpEntity<String> request = new HttpEntity<>("authentication", headers);
        ResponseEntity<RentDto[]> response =
                restTemplate.exchange(
                        "http://localhost:8080/v1/rents/user/" + userId,
                        HttpMethod.GET,
                        request,
                        RentDto[].class);
        List<RentDto> responseList = Arrays.asList(response.getBody());
        return responseList;
    }

    public List<RentDto> getAllRentsByBookTitleStartsWithIgnoreCase(String bookTitle) {
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
        HttpEntity<String> request = new HttpEntity<>("authentication", headers);
        ResponseEntity<RentDto[]> response =
                restTemplate.exchange(
                        "http://localhost:8080/v1/rents/titles/" + bookTitle,
                        HttpMethod.GET,
                        request,
                        RentDto[].class);
        List<RentDto> responseList = Arrays.asList(response.getBody());
        return responseList;
    }

    public List<RentDto> getAllRentsByUserEmailStartsWithIgnoreCase(String email) {
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
        HttpEntity<String> request = new HttpEntity<>("authentication", headers);
        ResponseEntity<RentDto[]> response =
                restTemplate.exchange(
                        "http://localhost:8080/v1/rents/emails/" + email,
                        HttpMethod.GET,
                        request,
                        RentDto[].class);
        List<RentDto> responseList = Arrays.asList(response.getBody());
        return responseList;
    }

    public Long countRents() {
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
        HttpEntity<String> request = new HttpEntity<>("authentication", headers);
        ResponseEntity<Long> response =
                restTemplate.exchange(
                        "http://localhost:8080/v1/rents/count",
                        HttpMethod.GET,
                        request,
                        Long.class);
        return response.getBody();
    }

    public boolean isRentExistBySpecimenId(Long id) {
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
        HttpEntity<String> request = new HttpEntity<>("authentication", headers);
        ResponseEntity<Boolean> response =
                restTemplate.exchange(
                        "http://localhost:8080/v1/rents/exist/id/" + id,
                        HttpMethod.GET,
                        request,
                        Boolean.class);
        return response.getBody();
    }

    public boolean isRentExistBySpecimenBookTitle(String title) {
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
        HttpEntity<String> request = new HttpEntity<>("authentication", headers);
        ResponseEntity<Boolean> response =
                restTemplate.exchange(
                        "http://localhost:8080/v1/rents/exist/title/" + title,
                        HttpMethod.GET,
                        request,
                        Boolean.class);
        return response.getBody();
    }

    public void rentBook(Long specimenId, Long userId) {
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(headers);
        restTemplate.postForObject("http://localhost:8080/v1/rents/" + userId + "?specimenId=" + specimenId, request, RentDto.class);
    }

    public void prolongationRent(Long specimenId, Long userId) {
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(headers);
        restTemplate.put("http://localhost:8080/v1/rents/" + userId + "?specimenId=" + specimenId, request, BookDto.class);
    }

    public void returnBook(Long id) {
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("authentication", headers);
        restTemplate.exchange("http://localhost:8080/v1/rents?id=" + id, HttpMethod.DELETE, request, Void.class, 1);
    }
}