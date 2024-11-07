package edu.java.kudagoapi.configuration;

import edu.java.kudagoapi.model.Location;
import edu.java.kudagoapi.model.LocationSnapshot;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper mapper() {
        ModelMapper mapper = new ModelMapper();
        TypeMap<Location, LocationSnapshot> toLocationSH = mapper.createTypeMap(Location.class, LocationSnapshot.class);
        TypeMap<LocationSnapshot, Location> fromLocationSH = mapper.createTypeMap(LocationSnapshot.class, Location.class);
        toLocationSH.addMappings(im -> im.map(Location::getId, LocationSnapshot::setOriginId));
        fromLocationSH.addMappings(im -> im.map(LocationSnapshot::getOriginId, Location::setId));
        return mapper;
    }
}
