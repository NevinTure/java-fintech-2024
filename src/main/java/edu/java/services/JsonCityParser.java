package edu.java.services;

import edu.java.model.City;
import java.io.File;
import java.util.Optional;

public interface JsonCityParser {
    Optional<City> parseJson(String jsonFilePath);
    Optional<City> parseJson(File jsonFile);
}
