package edu.java.currencyapi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.client.RestClient;

@Configuration
public class CbrClientConfig {

    private static final String BASE_URL = "https://api.stackexchange.com/2.3/questions/";

    @Bean
    public RestClient cbrClient(ApplicationConfig applicationConfig) {
        String baseUrl = applicationConfig.cbrBaseApi() == null ? BASE_URL : applicationConfig.cbrBaseApi();
        return RestClient
                .builder()
                .baseUrl(baseUrl)
                .messageConverters(converters -> converters.add(new MappingJackson2XmlHttpMessageConverter()))
                .build();
    }
}
