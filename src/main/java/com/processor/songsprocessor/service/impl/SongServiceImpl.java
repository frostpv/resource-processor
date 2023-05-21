package com.processor.songsprocessor.service.impl;

import com.processor.songsprocessor.component.ExternalServicesProperties;
import com.processor.songsprocessor.dto.SongDto;
import com.processor.songsprocessor.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return restTemplate.postForEntity(getUri(), songDto, SongDto.class).getBody();
    }

    private URI getUri() {
        URI uri;
        try {
           uri = getSongServiceUrl();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return uri;
    }

    private URI getSongServiceUrl() throws URISyntaxException {
        return  externalServicesProperties.checkSongServiceProperties()
                ? new URI(getUrlFromProperties())
                : new URI(getUrlAsDefault());
    }

    private String getUrlFromProperties() {
        return externalServicesProperties.getSongServiceProtocol() +
                externalServicesProperties.getSongServiceHost() +
                ":" +
                externalServicesProperties.getSongServicePort() +
                externalServicesProperties.getSongServiceEndpoint() ;
    }

    private String getUrlAsDefault() {
        return DEFAULT_SONG_SERVICE_PROTOCOL +
                DEFAULT_SONG_SERVICE_HOST +
                ":" +
                DEFAULT_SONG_SERVICE_PORT +
                DEFAULT_SONG_SERVICE_ENDPOINT;
    }
}
