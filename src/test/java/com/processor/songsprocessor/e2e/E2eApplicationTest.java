package com.processor.songsprocessor.e2e;

import com.processor.songsprocessor.dto.FileStorageDto;
import com.processor.songsprocessor.dto.SongDto;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;

import java.util.Date;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
public class E2eApplicationTest {
    private final static String SONG_SERVICE_URL = "http://35.208.79.123:8082/";
    private final static String SONG_STORAGE_URL = "http://35.208.79.123:8080/";
    private final static String RABBIT_MESSENGER_URL = "http://35.209.134.187:15672/";

    @Test
    public void shouldRabbitServerWork(){
        pingSomeUrl(RABBIT_MESSENGER_URL);
    }

    @Test
    public void shouldSongStorageWork(){
        pingSomeUrl(SONG_STORAGE_URL+"resources");
    }

    @Test
    public void shouldSongServiceWork(){
        pingSomeUrl(SONG_SERVICE_URL+ "songs/all");
    }

    @Test
    public void shouldMakeAllWhatIWant() throws InterruptedException {
        FileStorageDto fileStorageDto = given()
                .param("timestamp", new Date().getTime())
                .multiPart(new File("deep.mp3"))
                .accept(ContentType.ANY)
                .when()
                .post("http://35.208.79.123:8080/resources")
                .then()
                .statusCode(200)
                .extract()
                .as(FileStorageDto.class);


        SongDto[] timestamps = given()
                .param("timestamp", new Date().getTime())
                .config(config)
                .accept(ContentType.ANY)
                .when()
                .get(SONG_SERVICE_URL + "songs/all")
                .then()
                .statusCode(200)
                .extract()
                .as(SongDto[].class);

        boolean contains = Stream.of(timestamps)
                .map(SongDto::getResourceId)
                .collect(Collectors.toList())
                .contains(fileStorageDto.getId());
        assertThat(contains, is(false));

    }

    private void pingSomeUrl(String url){
        given()
                .param("timestamp", new Date().getTime())
                .accept(ContentType.ANY)
                .when()
                .get(url)
                .then()
                .statusCode(200);
    }
}
