package edu.java.kudagoapi.configuration;

import edu.java.kudagoapi.clients.KudagoClientErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class KudagoClientConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new KudagoClientErrorDecoder();
    }
}
