package edu.java.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.model.City;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JacksonJsonCityParser implements JsonCityParser {

    private final ObjectMapper mapper;

    @Override
    public Optional<City> parseJson(String jsonFilePath) {
        log.debug("Start parseJson method in JacksonJsonCityParser with jsonFilePath: {}",
                jsonFilePath);
        return parseJson(new File(jsonFilePath));
    }

    @Override
    public Optional<City> parseJson(File jsonFile) {
        log.debug("Start parseJson method in JacksonJsonCityParser with jsonFile: {}",
                jsonFile.getAbsolutePath());
        try {
            log.info("Start parsing json file {}", jsonFile.getAbsolutePath());
            City city = mapper.readValue(jsonFile, City.class);
            log.info("Finish parsing json file {}", jsonFile.getAbsolutePath());
            return Optional.of(city);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        log.warn("Finish parsing json file {} with error", jsonFile.getAbsolutePath());
        return Optional.empty();
    }
}
