package edu.java.kudagoapi.services.event;

import edu.java.kudagoapi.dtos.EventFilter;
import edu.java.kudagoapi.dtos.events.EventDto;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface EventService {

    ResponseEntity<Object> save(EventDto dto);

    ResponseEntity<EventDto> getById(long id);

    ResponseEntity<List<EventDto>> findAll();

    ResponseEntity<List<EventDto>> findAllByFilter(EventFilter filter);

    ResponseEntity<Object> fullUpdate(long id, EventDto dto);

    ResponseEntity<Object> deleteById(long id);
}
