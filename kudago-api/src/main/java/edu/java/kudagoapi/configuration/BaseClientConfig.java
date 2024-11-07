package edu.java.kudagoapi.configuration;

import edu.java.kudagoapi.clients.ErrorDecoderImpl;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class BaseClientConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new ErrorDecoderImpl();
    }
}
