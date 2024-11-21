package edu.java.mqbenchmark;

import edu.java.mqbenchmark.configuration.ApplicationConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class MqBenchmarkApplication {

    public static void main(String[] args) {
        SpringApplication.run(MqBenchmarkApplication.class, args);
    }

}
