package com.sewerynkamil.librarymanager.client;

import com.google.gson.Gson;
import com.sewerynkamil.librarymanager.dto.BookDto;
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
public class LibraryManagerBooksClient {
    @Autowired
    private RestTemplate restTemplate;

    private HttpHeaders headers = new HttpHeaders();

    public List<BookDto> getAllBooksWithLazyLoading(int offset, int limit) {
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
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
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
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
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
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
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
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
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
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
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
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
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
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
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
        Gson gson = new Gson();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonContent = gson.toJson(bookDto);
        HttpEntity<String> request = new HttpEntity<>(jsonContent, headers);
        restTemplate.postForObject("http://localhost:8080/v1/books", request, BookDto.class);
    }

    public void updateBook(BookDto bookDto) {
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
        Gson gson = new Gson();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonContent = gson.toJson(bookDto);
        HttpEntity<String> request = new HttpEntity<>(jsonContent, headers);
        restTemplate.put("http://localhost:8080/v1/books", request, BookDto.class);
    }

    public void deleteBook(Long id) {
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("authentication", headers);
        restTemplate.exchange("http://localhost:8080/v1/books?id=" + id, HttpMethod.DELETE, request, Void.class, 1);
    }
}