package com.processor.songsprocessor.service.impl;

import com.processor.songsprocessor.component.ExternalServicesProperties;
import com.processor.songsprocessor.dto.SongDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class SongServiceImplTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ExternalServicesProperties externalServicesProperties;

    @Captor
    private ArgumentCaptor<URI> uriCaptor;

    @InjectMocks
    private SongServiceImpl sut = new SongServiceImpl();

    private static final String DEFAULT_URL_VALUE = "http://localhost:8082/songs";
    private static final String CONFIGURED_URL_VALUE = "https://11.23.23.43:8082/songs";

    @Before
    public void setup() {


        when(restTemplate.postForEntity(any(URI.class), any(SongDto.class), any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        when(externalServicesProperties.getSongServiceProtocol()).thenReturn("https://");
        when(externalServicesProperties.getSongServiceHost()).thenReturn("11.23.23.43");
        when(externalServicesProperties.getSongServiceEndpoint()).thenReturn("/songs");
        when(externalServicesProperties.getSongServicePort()).thenReturn(8082);

        when(externalServicesProperties.checkSongServiceProperties()).thenReturn(false);
    }

    @Test
    public void shouldGetDefaultPropertiesForUrlIfConfigIsNotAvailable(){
        SongDto songDto = sut.saveSongMeta(new SongDto());

        verify(restTemplate).postForEntity(uriCaptor.capture(), any(SongDto.class), any());
        URI value = uriCaptor.getValue();

        verify(externalServicesProperties, times(0)).getSongServiceProtocol();
        verify(externalServicesProperties, times(0)).getSongServiceHost();
        verify(externalServicesProperties, times(0)).getSongServiceHost();
        verify(externalServicesProperties, times(0)).getSongServicePort();

        assertThat(value.toString(), is(DEFAULT_URL_VALUE));
    }

    @Test
    public void shouldGetPropertiesForUri(){
        when(externalServicesProperties.checkSongServiceProperties()).thenReturn(true);

        SongDto songDto = sut.saveSongMeta(new SongDto());

        verify(restTemplate).postForEntity(uriCaptor.capture(), any(SongDto.class), any());
        URI value = uriCaptor.getValue();

        verify(externalServicesProperties, times(1)).getSongServiceProtocol();
        verify(externalServicesProperties, times(1)).getSongServiceHost();
        verify(externalServicesProperties, times(1)).getSongServiceHost();
        verify(externalServicesProperties, times(1)).getSongServicePort();

        assertThat(value.toString(), is(CONFIGURED_URL_VALUE));
    }
}