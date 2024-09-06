package edu.java.services;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import edu.java.model.City;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.io.File;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JacksonXMLCityParser implements XMLCityParser {

    private final XmlMapper mapper;

    @Override
    public void toXML(City city, String xmlFilePath) {
        log.debug("Start toXML method in JacksonXMLCityParser with city: {} and xmlFilePath: {}",
                city,
                xmlFilePath);
        toXML(city, new File(xmlFilePath));
    }

    @Override
    public void toXML(City city, File xmlFile) {
        log.debug("Start toXML method in JacksonXMLCityParser with city: {} and xmlFile: {}",
                city,
                xmlFile.getAbsolutePath());
        try {
            log.info("Start writing to {}", xmlFile.getAbsolutePath());
            mapper.writeValue(xmlFile, city);
            log.info("Finish writing to {}", xmlFile.getAbsolutePath());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
