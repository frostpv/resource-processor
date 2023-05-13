package com.processor.songsprocessor.service.impl;

import com.processor.songsprocessor.component.ExternalServicesProperties;
import com.processor.songsprocessor.dto.SongDto;
import com.processor.songsprocessor.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

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
        return  externalServicesProperties.checkSongServiceProperties()
                ? new URI(getUrlFromProperties())
                : new URI(getUrlAsDefault());
    }

    private String getUrlFromProperties() {
        return externalServicesProperties.getSongServiceProtocol() +
                externalServicesProperties.getSongServiceHost() +
                externalServicesProperties.getSongServiceEndpoint() +
                ":" +
                externalServicesProperties.getSongServicePort();
    }

    private String getUrlAsDefault() {
        return DEFAULT_SONG_SERVICE_PROTOCOL +
                DEFAULT_SONG_SERVICE_HOST +
                DEFAULT_SONG_SERVICE_ENDPOINT +
                ":" +
                DEFAULT_SONG_SERVICE_PORT;
    }
}
