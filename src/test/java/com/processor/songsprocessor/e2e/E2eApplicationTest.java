package com.processor.songsprocessor.e2e;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class E2eApplicationTest {


    @Test
    @Ignore
    public void test() throws URISyntaxException {
        URI uri = new URI("http://35.208.79.123:8080/resources");

        HttpHeaders headers = new HttpHeaders();

        Map<String, String> boundary = new HashMap<>();
        boundary.put("boundary", "ADC");
        headers.setContentType(new MediaType("multipart", "form-data", boundary));

        Path path = Paths.get("cut.mp3");
        FileSystemResource fileSystemResource = new FileSystemResource(path);


        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("files", fileSystemResource);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.MULTIPART_FORM_DATA));
        messageConverters.add(converter);
        messageConverters.add(new ByteArrayHttpMessageConverter());

        restTemplate.setMessageConverters(messageConverters);
        restTemplate.postForObject(uri, requestEntity, String.class);
    }

}
