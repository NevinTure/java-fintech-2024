package edu.java.kudagoapi;

import edu.java.kudagoapi.configuration.ApplicationConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableFeignClients
@EnableConfigurationProperties(ApplicationConfig.class)
public class KudagoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(KudagoApiApplication.class, args);
    }

}
