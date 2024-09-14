package edu.java;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import edu.java.model.City;
import edu.java.model.Coordinates;
import edu.java.services.JacksonXMLCityParser;
import edu.java.services.XMLCityParser;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class XMLCityParserTest {

    public final XMLCityParser parser = new JacksonXMLCityParser(new XmlMapper());


    @Test
    @SneakyThrows
    public void testToXML(@TempDir Path tempDir) {
        //given
        City city = new City(
                "spb",
                new Coordinates(
                        59.939095,
                        30.315868
                )
        );
        File file = new File(tempDir.toFile(), "test.xml");

        //when
        parser.toXML(city, file.getAbsolutePath());
        String result = Files.readString(file.toPath());

        //then
        String expectedResult =
                "<City><slug>spb</slug><coords><lat>59.939095</lat><lon>30.315868</lon></coords></City>";
        assertThat(result).isEqualTo(expectedResult);
    }
}
