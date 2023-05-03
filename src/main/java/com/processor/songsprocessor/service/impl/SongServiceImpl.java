package com.processor.songsprocessor.service.impl;

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
    private final static String SONG_SERVICE_HOST = "http://localhost:8082";
    private final static String SONG_SERVICE_ENDPOINT = "/songs";


    @Autowired
    RestTemplate restTemplate;

    @Override
    public SongDto saveSongMeta(SongDto songDto) {

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        URI uri = null;
        try {
            uri = new URI(SONG_SERVICE_HOST+SONG_SERVICE_ENDPOINT);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        ResponseEntity<SongDto> songDtoResponseEntity = restTemplate.postForEntity(uri, songDto, SongDto.class);




        return songDtoResponseEntity.getBody();
    }
}
