package edu.java.currencyapi;

import edu.java.currencyapi.configuration.ApplicationConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
@EnableFeignClients
public class CurrencyApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrencyApiApplication.class, args);
    }

}
