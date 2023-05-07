package com.processor.songsprocessor.service.impl;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.processor.songsprocessor.component.ExternalServicesProperties;
import com.processor.songsprocessor.dto.SongDto;
import com.processor.songsprocessor.service.SongStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class SongStorageImpl implements SongStorage {
    private final static String DEFAULT_SONG_STORAGE_PROTOCOL = "http://";
    private final static int DEFAULT_SONG_STORAGE_PORT = 8080;
    private final static String DEFAULT_SONG_STORAGE_HOST = "localhost";
    private final static String DEFAULT_SONG_STORAGE_ENDPOINT = "/resources/";

    @Autowired
    ExternalServicesProperties externalServicesProperties;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public SongDto getSongMetaData(long id, String name) {
        SongDto songDto = null;
        String url = getSongStorageUrl();

        try {
            byte[] song = restTemplate.getForObject(url, byte[].class);
            songDto = new SongDto();
            songDto.setResourceId((int) id);
            Path path = Paths.get(name);
            Files.write(path, song);
            songDto.setResourceId((int) id);
            Mp3File mp3file = new Mp3File(new File(name));
            songDto.setLength(mp3file.getLengthInSeconds() + "- seconds");
            populateSongDto(mp3file, songDto);
            Files.delete(path);
            return songDto;
        } catch (IOException | UnsupportedTagException | ResourceAccessException | InvalidDataException e ) {
            //fix error and return
            throw new RuntimeException();
        }
    }

    private String getSongStorageUrl() {
        StringBuilder storageUrl = new StringBuilder();
        return externalServicesProperties.checkSongStorageProperties()
                ? getUrlFromProperties(storageUrl)
                : getUrlAsDefault(storageUrl);
    }

    private String getUrlFromProperties(StringBuilder storageUrl) {
        return storageUrl.append(externalServicesProperties.getSongServiceProtocol())
                .append(externalServicesProperties.getSongServiceHost())
                .append(externalServicesProperties.getSongServiceEndpoint())
                .append(":")
                .append(externalServicesProperties.getSongServicePort())
                .toString();
    }

    private String getUrlAsDefault(StringBuilder storageUrl) {
        return storageUrl.append(DEFAULT_SONG_STORAGE_PROTOCOL)
                .append(DEFAULT_SONG_STORAGE_HOST)
                .append(DEFAULT_SONG_STORAGE_ENDPOINT)
                .append(":")
                .append(DEFAULT_SONG_STORAGE_PORT)
                .toString();
    }

    private void populateSongDto(Mp3File mp3file , SongDto songDto) {
        if (mp3file.hasId3v1Tag()) {
            ID3v1 id3v1Tag = mp3file.getId3v1Tag();

            songDto.setName(id3v1Tag.getTitle());
            songDto.setAlbum(id3v1Tag.getAlbum());
            songDto.setYear(Integer.parseInt(id3v1Tag.getYear()));
            songDto.setArtist(id3v1Tag.getArtist());
        }
    }
}
