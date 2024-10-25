package edu.java.kudagoapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class IntegrationTest extends IntegrationEnvironment {

    @Test
    public void testContainer() {
        assertThat(POSTGRES.isCreated()).isTrue();
        assertThat(POSTGRES.isRunning()).isTrue();
    }
}
