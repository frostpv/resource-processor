package com.processor.songsprocessor.component;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
@Data
public class ExternalServicesProperties {
    //Song service properties block
    @Value("${song.service.port}")
    private int songServicePort;

    @Value("${song.service.host}")
    private String songServiceHost;

    @Value("${song.service.endpoint}")
    private String songServiceEndpoint;

    @Value("${song.service.protocol}")
    private String songServiceProtocol;

    //Song service properties block
    @Value("${song.storage.port}")
    private int songStoragePort;

    @Value("${song.storage.host}")
    private String songStorageHost;

    @Value("${song.storage.endpoint}")
    private String songStorageEndpoint;

    @Value("${song.storage.protocol}")
    private String songStorageProtocol;
}
