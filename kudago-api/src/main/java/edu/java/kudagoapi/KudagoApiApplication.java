package edu.java.kudagoapi;

import edu.java.kudagoapi.configuration.ApplicationConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class KudagoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(KudagoApiApplication.class, args);
    }

}
