package com.processor.songsprocessor.component;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

//@todo this class also should be removed or reused in retry
@Component
@PropertySource("classpath:application.properties")
@Data
public class Properties {

    @Value("${retry.count}")
    private int retryCount;

    @Value("${retry.timeout}")
    private long retryTimeout;

    @Value("${retry.factor}")
    private int retryFactor;
}
