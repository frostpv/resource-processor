package com.processor.songsprocessor.service.impl;

import com.processor.songsprocessor.component.ExternalServicesProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SongStorageImplTest {

    private static final String DEFAULT_URL_VALUE = "http://localhost:8080/resources/";
    private static final String CONFIGURED_URL_VALUE = "https://11.23.23.43:8082/resources/";

    @InjectMocks
    private SongStorageImpl sut;

    @Mock
    ExternalServicesProperties externalServicesProperties;

    @Mock
    RestTemplate restTemplate;

    @Captor
    private ArgumentCaptor<String> uriCaptor;

    @Before
    public void setup() throws IOException {
        Path path = Paths.get("johnny_cash.mp3");
        byte[] bytes = Files.readAllBytes(path);
        when(restTemplate.getForObject(any(String.class), any())).thenReturn(bytes);

        when(externalServicesProperties.getSongStorageProtocol()).thenReturn("https://");
        when(externalServicesProperties.getSongStorageHost()).thenReturn("11.23.23.43");
        when(externalServicesProperties.getSongStorageEndpoint()).thenReturn("/resources/");
        when(externalServicesProperties.getSongStoragePort()).thenReturn(8082);


    }

    @Test
    public void shouldGetDefaultUrl(){
        when(externalServicesProperties.checkSongStorageProperties()).thenReturn(false);

        sut.getSongMetaData(101L, "someName.mp3");

        verify(restTemplate).getForObject(uriCaptor.capture(), any());
        String value = uriCaptor.getValue();

        verify(externalServicesProperties, times(0)).getSongStorageProtocol();
        verify(externalServicesProperties, times(0)).getSongStorageHost();
        verify(externalServicesProperties, times(0)).getSongStorageHost();
        verify(externalServicesProperties, times(0)).getSongStoragePort();

        assertThat(value, is(DEFAULT_URL_VALUE));
    }

    @Test
    public void shouldGetUrlFromProperties(){
        when(externalServicesProperties.checkSongStorageProperties()).thenReturn(true);

        sut.getSongMetaData(101L, "someName.mp3");

        verify(restTemplate).getForObject(uriCaptor.capture(), any());
        String value = uriCaptor.getValue();

        verify(externalServicesProperties, times(1)).getSongStorageProtocol();
        verify(externalServicesProperties, times(1)).getSongStorageHost();
        verify(externalServicesProperties, times(1)).getSongStorageHost();
        verify(externalServicesProperties, times(1)).getSongStoragePort();

        assertThat(value, is(CONFIGURED_URL_VALUE));
    }


}