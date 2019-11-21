package com.sewerynkamil.librarymanager.client;

import com.google.gson.Gson;
import com.sewerynkamil.librarymanager.dto.BookDto;
import com.sewerynkamil.librarymanager.dto.RequestJwtDto;
import com.sewerynkamil.librarymanager.dto.UserDto;
import com.sewerynkamil.librarymanager.dto.WolneLekturyAudiobookDto;
import com.sewerynkamil.librarymanager.dto.enumerated.Role;
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

    public void registerUser(UserDto userDto) {
        Gson gson = new Gson();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonContent = gson.toJson(userDto);
        HttpEntity<String> request = new HttpEntity<>(jsonContent, headers);
        restTemplate.postForObject("http://localhost:8080/v1/register", request, UserDto.class);
    }

    public List<BookDto> getAllBooksWithLazyLoading(int offset, int limit) {
        headers.set(HttpHeaders.AUTHORIZATION, jwttoken);
        HttpEntity<String> request = new HttpEntity<>("authentication", headers);
        ResponseEntity<BookDto[]> response =
                restTemplate.exchange(
                        "http://localhost:8080/v1/books?offset=" + offset + "&limit=" + limit,
                        HttpMethod.GET,
                        request,
                        BookDto[].class);
        List<BookDto> responseList = Arrays.asList(response.getBody());
        return responseList;
    }

    public List<BookDto> getAllBooksByAuthorStartsWithIgnoreCase(String author) {
        headers.set(HttpHeaders.AUTHORIZATION, jwttoken);
        HttpEntity<String> request = new HttpEntity<>("authentication", headers);
        ResponseEntity<BookDto[]> response =
                restTemplate.exchange(
                        "http://localhost:8080/v1/books/authors/" + author,
                        HttpMethod.GET,
                        request,
                        BookDto[].class);
        List<BookDto> responseList = Arrays.asList(response.getBody());
        return responseList;
    }

    public List<BookDto> getAllBooksByTitleStartsWithIgnoreCase(String title) {
        headers.set(HttpHeaders.AUTHORIZATION, jwttoken);
        HttpEntity<String> request = new HttpEntity<>("authentication", headers);
        ResponseEntity<BookDto[]> response =
                restTemplate.exchange(
                        "http://localhost:8080/v1/books/titles/" + title,
                        HttpMethod.GET,
                        request,
                        BookDto[].class);
        List<BookDto> responseList = Arrays.asList(response.getBody());
        return responseList;
    }

    public List<BookDto> getAllBooksByCategoryStartsWithIgnoreCase(String category) {
        headers.set(HttpHeaders.AUTHORIZATION, jwttoken);
        HttpEntity<String> request = new HttpEntity<>("authentication", headers);
        ResponseEntity<BookDto[]> response =
                restTemplate.exchange(
                        "http://localhost:8080/v1/books/categories/" + category,
                        HttpMethod.GET,
                        request,
                        BookDto[].class);
        List<BookDto> responseList = Arrays.asList(response.getBody());
        return responseList;
    }

    public BookDto getOneBook(Long id) {
        headers.set(HttpHeaders.AUTHORIZATION, jwttoken);
        HttpEntity<String> request = new HttpEntity<>("authentication", headers);
        ResponseEntity<BookDto> response =
                restTemplate.exchange(
                        "http://localhost:8080/v1/books/" + id,
                        HttpMethod.GET,
                        request,
                        BookDto.class);
        return response.getBody();
    }

    public boolean isBookExist(String title) {
        headers.set(HttpHeaders.AUTHORIZATION, jwttoken);
        HttpEntity<String> request = new HttpEntity<>("authentication", headers);
        ResponseEntity<Boolean> response =
                restTemplate.exchange(
                        "http://localhost:8080/v1/books/exist/" + title,
                        HttpMethod.GET,
                        request,
                        Boolean.class);
        return response.getBody();
    }

    public Long countBooks() {
        headers.set(HttpHeaders.AUTHORIZATION, jwttoken);
        HttpEntity<String> request = new HttpEntity<>("authentication", headers);
        ResponseEntity<Long> response =
                restTemplate.exchange(
                        "http://localhost:8080/v1/books/count",
                        HttpMethod.GET,
                        request,
                        Long.class);
        return response.getBody();
    }

    public void saveNewBook(BookDto bookDto) {
        headers.set(HttpHeaders.AUTHORIZATION, jwttoken);
        Gson gson = new Gson();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonContent = gson.toJson(bookDto);
        HttpEntity<String> request = new HttpEntity<>(jsonContent, headers);
        restTemplate.postForObject("http://localhost:8080/v1/books", request, BookDto.class);
    }

    public void updateBook(BookDto bookDto) {
        headers.set(HttpHeaders.AUTHORIZATION, jwttoken);
        Gson gson = new Gson();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonContent = gson.toJson(bookDto);
        HttpEntity<String> request = new HttpEntity<>(jsonContent, headers);
        restTemplate.put("http://localhost:8080/v1/books", request, BookDto.class);
    }

    public void deleteBook(Long id) {
        headers.set(HttpHeaders.AUTHORIZATION, jwttoken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("authentication", headers);
        restTemplate.exchange("http://localhost:8080/v1/books?id=" + id, HttpMethod.DELETE, request, Void.class, 1);
    }

    public List<WolneLekturyAudiobookDto> getAllAudiobooksWithLazyLoading(int offset, int limit) {
        headers.set(HttpHeaders.AUTHORIZATION, jwttoken);
        HttpEntity<String> request = new HttpEntity<>("authentication", headers);
        ResponseEntity<WolneLekturyAudiobookDto[]> response =
                restTemplate.exchange(
                        "http://localhost:8080/v1/audiobooks?offset=" + offset + "&limit=" + limit,
                        HttpMethod.GET,
                        request,
                        WolneLekturyAudiobookDto[].class);
        List<WolneLekturyAudiobookDto> responseList = Arrays.asList(response.getBody());
        return responseList;
    }

    public List<WolneLekturyAudiobookDto> getAllAudiobooksByAuthorStartsWithIgnoreCase(String author) {
        headers.set(HttpHeaders.AUTHORIZATION, jwttoken);
        HttpEntity<String> request = new HttpEntity<>("authentication", headers);
        ResponseEntity<WolneLekturyAudiobookDto[]> response =
                restTemplate.exchange(
                        "http://localhost:8080/v1/audiobooks/authors/" + author,
                        HttpMethod.GET,
                        request,
                        WolneLekturyAudiobookDto[].class);
        List<WolneLekturyAudiobookDto> responseList = Arrays.asList(response.getBody());
        return responseList;
    }

    public List<WolneLekturyAudiobookDto> getAllAudiobooksByTitleStartsWithIgnoreCase(String title) {
        headers.set(HttpHeaders.AUTHORIZATION, jwttoken);
        HttpEntity<String> request = new HttpEntity<>("authentication", headers);
        ResponseEntity<WolneLekturyAudiobookDto[]> response =
                restTemplate.exchange(
                        "http://localhost:8080/v1/audiobooks/titles/" + title,
                        HttpMethod.GET,
                        request,
                        WolneLekturyAudiobookDto[].class);
        List<WolneLekturyAudiobookDto> responseList = Arrays.asList(response.getBody());
        return responseList;
    }

    public int countAudiobooks() {
        headers.set(HttpHeaders.AUTHORIZATION, jwttoken);
        HttpEntity<String> request = new HttpEntity<>("authentication", headers);
        ResponseEntity<Integer> response =
                restTemplate.exchange(
                        "http://localhost:8080/v1/audiobooks/count",
                        HttpMethod.GET,
                        request,
                        Integer.class);
        return response.getBody();
    }

    public List<UserDto> getAllUsersWithLazyLoading(int offset, int limit) {
        headers.set(HttpHeaders.AUTHORIZATION, jwttoken);
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
        headers.set(HttpHeaders.AUTHORIZATION, jwttoken);
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
        headers.set(HttpHeaders.AUTHORIZATION, jwttoken);
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
        headers.set(HttpHeaders.AUTHORIZATION, jwttoken);
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
        headers.set(HttpHeaders.AUTHORIZATION, jwttoken);
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

    public boolean isUserExist(String email) {
        return restTemplate.getForObject("http://localhost:8080/v1/users/exist/" + email, Boolean.class);
    }

    public Long countUsers() {
        headers.set(HttpHeaders.AUTHORIZATION, jwttoken);
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
        headers.set(HttpHeaders.AUTHORIZATION, jwttoken);
        Gson gson = new Gson();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonContent = gson.toJson(userDto);
        HttpEntity<String> request = new HttpEntity<>(jsonContent, headers);
        restTemplate.postForObject("http://localhost:8080/v1/users", request, UserDto.class);
    }

    public void updateUser(UserDto userDto) {
        headers.set(HttpHeaders.AUTHORIZATION, jwttoken);
        Gson gson = new Gson();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonContent = gson.toJson(userDto);
        HttpEntity<String> request = new HttpEntity<>(jsonContent, headers);
        restTemplate.put("http://localhost:8080/v1/users", request, UserDto.class);
    }

    public void deleteUser(Long id) {
        headers.set(HttpHeaders.AUTHORIZATION, jwttoken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("authentication", headers);
        restTemplate.exchange("http://localhost:8080/v1/users?id=" + id, HttpMethod.DELETE, request, Void.class, 1);
    }

    public void setJwttoken(String jwttoken) {
        this.jwttoken = jwttoken;
    }

}