package com.sewerynkamil.librarymanager.client;

import com.sewerynkamil.librarymanager.dto.wolnelektury.WolneLekturyAudiobookDto;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Author Kamil Seweryn
 */

@Component
public class LibraryManagerAudiobooksClient {
    @Autowired
    private RestTemplate restTemplate;

    private HttpHeaders headers = new HttpHeaders();

    public List<WolneLekturyAudiobookDto> getAllAudiobooksWithLazyLoading(int offset, int limit) {
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
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
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
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
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
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
        headers.set(HttpHeaders.AUTHORIZATION, (String) VaadinSession.getCurrent().getAttribute("token"));
        HttpEntity<String> request = new HttpEntity<>("authentication", headers);
        ResponseEntity<Integer> response =
                restTemplate.exchange(
                        "http://localhost:8080/v1/audiobooks/count",
                        HttpMethod.GET,
                        request,
                        Integer.class);
        return response.getBody();
    }
}