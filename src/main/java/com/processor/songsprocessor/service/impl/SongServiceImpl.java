package com.processor.songsprocessor.service.impl;

import com.processor.songsprocessor.component.ExternalServicesProperties;
import com.processor.songsprocessor.dto.SongDto;
import com.processor.songsprocessor.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

@Service
public class SongServiceImpl implements SongService {
    private final static String DEFAULT_SONG_SERVICE_PROTOCOL = "http://";
    private final static int DEFAULT_SONG_SERVICE_PORT = 8082;
    private final static String DEFAULT_SONG_SERVICE_HOST = "localhost";
    private final static String DEFAULT_SONG_SERVICE_ENDPOINT = "/songs";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ExternalServicesProperties externalServicesProperties;

    @Override
    public SongDto saveSongMeta(SongDto songDto) {

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        URI uri = null;
        try {
            getSongServiceUrl();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        ResponseEntity<SongDto> songDtoResponseEntity = restTemplate.postForEntity(uri, songDto, SongDto.class);
        return songDtoResponseEntity.getBody();
    }

    private URI getSongServiceUrl() throws URISyntaxException {
        StringBuilder serviceUrl = new StringBuilder();

        if (externalServicesProperties.checkSongServiceProperties()) {
            populateUrlFromProperties(serviceUrl);
            return new URI(serviceUrl.toString());
        }

        populateUrlAsDefault(serviceUrl);
        return new URI(serviceUrl.toString());
    }

    private void populateUrlFromProperties(StringBuilder serviceUrl) {
        serviceUrl.append(externalServicesProperties.getSongServiceProtocol())
                .append(externalServicesProperties.getSongServiceHost())
                .append(externalServicesProperties.getSongServiceEndpoint())
                .append(":")
                .append(externalServicesProperties.getSongServicePort());
    }

    private void populateUrlAsDefault(StringBuilder serviceUrl) {
        serviceUrl.append(DEFAULT_SONG_SERVICE_PROTOCOL)
                .append(DEFAULT_SONG_SERVICE_HOST)
                .append(DEFAULT_SONG_SERVICE_ENDPOINT)
                .append(":")
                .append(DEFAULT_SONG_SERVICE_PORT);
    }
}
