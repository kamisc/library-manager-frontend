package com.sewerynkamil.librarymanager.client;

import com.google.gson.Gson;
import com.sewerynkamil.librarymanager.dto.UserDto;
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
public class LibraryManagerUsersClient {
    @Autowired
    private RestTemplate restTemplate;

    private HttpHeaders headers = new HttpHeaders();

    public List<UserDto> getAllUsersWithLazyLoading(int offset, int limit) {
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
        HttpEntity<String> request = new HttpEntity<>("authentication", headers);
        ResponseEntity<UserDto[]> response =
                restTemplate.exchange(
                        "http://localhost:8080/v1/users?offset=" + offset + "&limit=" + limit,
                        HttpMethod.GET,
                        request,
                        UserDto[].class);
        List<UserDto> responseList = Arrays.asList(response.getBody());
        return responseList;
    }

    public List<UserDto> getAllUsersByNameStartsWithIgnoreCase(String name) {
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
        HttpEntity<String> request = new HttpEntity<>("authentication", headers);
        ResponseEntity<UserDto[]> response =
                restTemplate.exchange(
                        "http://localhost:8080/v1/users/names/" + name,
                        HttpMethod.GET,
                        request,
                        UserDto[].class);
        List<UserDto> responseList = Arrays.asList(response.getBody());
        return responseList;
    }

    public List<UserDto> getAllUsersBySurnameStartsWithIgnoreCase(String surname) {
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
        HttpEntity<String> request = new HttpEntity<>("authentication", headers);
        ResponseEntity<UserDto[]> response =
                restTemplate.exchange(
                        "http://localhost:8080/v1/users/surnames/" + surname,
                        HttpMethod.GET,
                        request,
                        UserDto[].class);
        List<UserDto> responseList = Arrays.asList(response.getBody());
        return responseList;
    }

    public List<UserDto> getAllUsersByEmailStartsWithIgnoreCase(String email) {
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
        HttpEntity<String> request = new HttpEntity<>("authentication", headers);
        ResponseEntity<UserDto[]> response =
                restTemplate.exchange(
                        "http://localhost:8080/v1/users/emails/" + email,
                        HttpMethod.GET,
                        request,
                        UserDto[].class);
        List<UserDto> responseList = Arrays.asList(response.getBody());
        return responseList;
    }

    public UserDto getOneUserById(Long id) {
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
        HttpEntity<String> request = new HttpEntity<>("authentication", headers);
        ResponseEntity<UserDto> response =
                restTemplate.exchange(
                        "http://localhost:8080/v1/users/id/" + id,
                        HttpMethod.GET,
                        request,
                        UserDto.class);
        return response.getBody();
    }

    public UserDto getOneUserByEmail(String email) {
        return restTemplate.getForObject("http://localhost:8080/v1/users/email/" + email, UserDto.class);
    }

    public boolean isUserHasRents(String email) {
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
        HttpEntity<String> request = new HttpEntity<>("authentication", headers);
        ResponseEntity<Boolean> response =
                restTemplate.exchange(
                        "http://localhost:8080/v1/users/hasRents/" + email,
                        HttpMethod.GET,
                        request,
                        Boolean.class);
        return response.getBody();
    }

    public boolean isUserExist(String email) {
        return restTemplate.getForObject("http://localhost:8080/v1/users/exist/" + email, Boolean.class);
    }

    public Long countUsers() {
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
        HttpEntity<String> request = new HttpEntity<>("authentication", headers);
        ResponseEntity<Long> response =
                restTemplate.exchange(
                        "http://localhost:8080/v1/users/count",
                        HttpMethod.GET,
                        request,
                        Long.class);
        return response.getBody();
    }

    public void saveNewUser(UserDto userDto) {
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
        Gson gson = new Gson();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonContent = gson.toJson(userDto);
        HttpEntity<String> request = new HttpEntity<>(jsonContent, headers);
        restTemplate.postForObject("http://localhost:8080/v1/users", request, UserDto.class);
    }

    public void updateUser(UserDto userDto) {
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
        Gson gson = new Gson();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonContent = gson.toJson(userDto);
        HttpEntity<String> request = new HttpEntity<>(jsonContent, headers);
        restTemplate.put("http://localhost:8080/v1/users", request, UserDto.class);
    }

    public void deleteUser(Long id) {
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("authentication", headers);
        restTemplate.exchange("http://localhost:8080/v1/users?id=" + id, HttpMethod.DELETE, request, Void.class, 1);
    }
}