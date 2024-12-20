package edu.java.kudagoapi;

import edu.java.kudagoapi.configuration.ApplicationConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableFeignClients
@EnableAsync
@EnableConfigurationProperties(ApplicationConfig.class)
public class KudagoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(KudagoApiApplication.class, args);
    }

}
