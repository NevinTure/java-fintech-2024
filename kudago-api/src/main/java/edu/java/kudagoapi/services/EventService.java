package edu.java.kudagoapi.services;

import edu.java.kudagoapi.dtos.CategoryDto;
import edu.java.kudagoapi.dtos.events.EventDto;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface EventService {

    ResponseEntity<Object> save(EventDto dto);

    ResponseEntity<EventDto> getById(long id);

    ResponseEntity<List<EventDto>> findAll();

    ResponseEntity<Object> fullUpdate(long id, EventDto dto);

    ResponseEntity<Object> deleteById(long id);
}
