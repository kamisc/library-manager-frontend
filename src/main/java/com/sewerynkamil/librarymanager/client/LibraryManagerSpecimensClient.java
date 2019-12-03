package com.sewerynkamil.librarymanager.client;

import com.google.gson.Gson;
import com.sewerynkamil.librarymanager.dto.SpecimenDto;
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
public class LibraryManagerSpecimensClient {
    @Autowired
    private RestTemplate restTemplate;

    private HttpHeaders headers = new HttpHeaders();

    public List<SpecimenDto> getAllSpecimensForOneBook(Long bookId) {
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
        HttpEntity<String> request = new HttpEntity<>("authentication", headers);
        ResponseEntity<SpecimenDto[]> response =
                restTemplate.exchange(
                        "http://localhost:8080/v1/specimens?bookId=" + bookId,
                        HttpMethod.GET,
                        request,
                        SpecimenDto[].class);
        List<SpecimenDto> responseList = Arrays.asList(response.getBody());
        return responseList;
    }

    public List<SpecimenDto> getAllAvailableSpecimensForOneBook(String status, Long bookId) {
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
        HttpEntity<String> request = new HttpEntity<>("authentication", headers);
        ResponseEntity<SpecimenDto[]> response =
                restTemplate.exchange(
                        "http://localhost:8080/v1/specimens/" + bookId + "?status=" + status,
                        HttpMethod.GET,
                        request,
                        SpecimenDto[].class);
        List<SpecimenDto> responseList = Arrays.asList(response.getBody());
        return responseList;
    }

    public SpecimenDto getOneSpecimen(Long id) {
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
        HttpEntity<String> request = new HttpEntity<>("authentication", headers);
        ResponseEntity<SpecimenDto> response =
                restTemplate.exchange(
                        "http://localhost:8080/v1/specimens/get/" + id,
                        HttpMethod.GET,
                        request,
                        SpecimenDto.class);
        return response.getBody();
    }

    public void saveNewSpecimen(SpecimenDto specimenDto) {
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
        Gson gson = new Gson();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonContent = gson.toJson(specimenDto);
        HttpEntity<String> request = new HttpEntity<>(jsonContent, headers);
        restTemplate.postForObject("http://localhost:8080/v1/specimens", request, SpecimenDto.class);
    }

    public void updateSpecimen(SpecimenDto specimenDto) {
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
        Gson gson = new Gson();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonContent = gson.toJson(specimenDto);
        HttpEntity<String> request = new HttpEntity<>(jsonContent, headers);
        restTemplate.put("http://localhost:8080/v1/specimens", request, SpecimenDto.class);
    }

    public void deleteSpecimen(Long id) {
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("authentication", headers);
        restTemplate.exchange("http://localhost:8080/v1/specimens?id=" + id, HttpMethod.DELETE, request, Void.class, 1);
    }
}