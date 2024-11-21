package edu.java.mqbenchmark;

import org.junit.jupiter.api.Test;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

abstract public class AbstractBenchmark {

    @Test
    public void executeJmhRunner() throws RunnerException {
        Options opts = new OptionsBuilder()
                .include(KafkaBenchmarkTest.class.getSimpleName())
                .forks(0)
                .threads(1)
                .build();

        new Runner(opts).run();
    }
}
