package edu.java;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.model.City;
import edu.java.model.Coordinates;
import edu.java.services.JacksonJsonCityParser;
import edu.java.services.JsonCityParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.assertj.core.api.Assertions.assertThat;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Optional;

public class JsonCityParserTest {

    private final JsonCityParser parser = new JacksonJsonCityParser(new ObjectMapper());

    @Test
    public void testParseJsonCity(@TempDir Path tempDir) {
        //given
        File file = new File(tempDir.toFile(), "test.json");
        String json = """
                {
                  "slug": "spb",
                  "coords": {
                    "lat": 59.939095,
                    "lon": 30.315868
                  }
                }""";
        try (PrintWriter out = new PrintWriter(file)) {
            out.println(json);
        } catch (IOException ignored) {
        }

        //when
        Optional<City> result = parser.parseJson(file.getAbsolutePath());

        //then
        City expectedResult = new City(
                "spb",
                new Coordinates(
                        59.939095,
                        30.315868
                )
        );
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(expectedResult);
    }

    @Test
    public void testParseJsonCityWhenError(@TempDir Path tempDir) {
        //given
        File file = new File(tempDir.toFile(), "error.json");
        String json = """
                {
                  "slug": "spb",
                  "coo: {
                    "lat": 59.939095,
                    "lon": 30.315868
                  }
                }
                """;
        try (PrintWriter out = new PrintWriter(file)) {
            out.println(json);
        } catch (IOException ignored) {
        }

        //when
        Optional<City> result = parser.parseJson(file.getAbsolutePath());

        //then
        assertThat(result).isEmpty();
    }
}
