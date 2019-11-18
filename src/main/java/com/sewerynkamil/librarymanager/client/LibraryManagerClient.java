package com.sewerynkamil.librarymanager.client;

import com.google.gson.Gson;
import com.sewerynkamil.librarymanager.dto.BookDto;
import com.sewerynkamil.librarymanager.dto.RequestJwtDto;
import com.sewerynkamil.librarymanager.dto.UserDto;
import com.sewerynkamil.librarymanager.dto.WolneLekturyAudiobookDto;
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
        return restTemplate.getForObject("http://localhost:8080/v1/books/exist/" + title, Boolean.class);
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

    public UserDto getOneUserByEmail(String email) {
        headers.set(HttpHeaders.AUTHORIZATION, jwttoken);
        HttpEntity<String> request = new HttpEntity<>("authentication", headers);
        ResponseEntity<UserDto> response =
                restTemplate.exchange(
                        "http://localhost:8080/v1/users/email/" + email,
                        HttpMethod.GET,
                        request,
                        UserDto.class);
        return response.getBody();
    }

    public boolean isUserExist(String email) {
        return restTemplate.getForObject("http://localhost:8080/v1/users/exist/" + email, Boolean.class);
    }

    public void setJwttoken(String jwttoken) {
        this.jwttoken = jwttoken;
    }
}