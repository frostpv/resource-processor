package com.processor.songsprocessor.service.impl;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
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
    private static final String HOST = "http://localhost:8080/";
    private static final String ENDPOINT = "resources/";

    @Autowired
    RestTemplate restTemplate;

    @Override
    public SongDto getSongMetaData(long id, String name) {
        SongDto songDto = null;

        String url = HOST+ENDPOINT+id;


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
