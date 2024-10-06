package edu.java.customaspect.autoconfiguration;

import edu.java.customaspect.aspects.TimedAspect;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class TimedAnnotationAspectAutoConfiguration {

    @Bean
    public TimedAspect timedAspect() {
        return new TimedAspect();
    }
}
