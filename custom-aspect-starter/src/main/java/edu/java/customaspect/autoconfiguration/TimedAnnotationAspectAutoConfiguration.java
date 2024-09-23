package edu.java.customaspect.autoconfiguration;

import edu.java.customaspect.aspects.TimedAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimedAnnotationAspectAutoConfiguration {

    @Bean
    public TimedAspect timedAspect() {
        return new TimedAspect();
    }
}
