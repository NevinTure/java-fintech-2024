package edu.java.services;

import edu.java.model.City;
import java.io.File;

public interface XMLCityParser {
    void toXML(City city, String xmlFilePath);

    void toXML(City city, File xmlFile);
}
