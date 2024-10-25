package edu.java.kudagoapi.services;

import edu.java.kudagoapi.dtos.events.EventDto;
import edu.java.kudagoapi.exceptions.EventNotFoundApiException;
import edu.java.kudagoapi.model.Event;
import edu.java.kudagoapi.repositories.JpaEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class JpaEventService implements EventService {

    private final JpaEventRepository repo;
    private final ModelMapper mapper;

    @Override
    public ResponseEntity<Object> save(EventDto dto) {
        repo.save(mapper.map(dto, Event.class));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<EventDto> getById(long id) {
        Optional<Event> eventOp = repo.findById(id);
        if (eventOp.isPresent()) {
            EventDto dto = mapper.map(eventOp.get(), EventDto.class);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }
        throw new EventNotFoundApiException(id);
    }

    @Override
    public ResponseEntity<List<EventDto>> findAll() {
        return new ResponseEntity<>(repo.findAll()
                .stream()
                .map(v -> mapper.map(v, EventDto.class))
                .toList(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> fullUpdate(long id, EventDto dto) {
        Optional<Event> eventOp = repo.findById(id);
        if (eventOp.isPresent()) {
            Event event = mapper.map(eventOp.get(), Event.class);
            event.setId(id);
            repo.save(event);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new EventNotFoundApiException(id);
    }

    @Override
    public ResponseEntity<Object> deleteById(long id) {
        Optional<Event> eventOp = repo.findById(id);
        if (eventOp.isPresent()) {
            repo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new EventNotFoundApiException(id);
    }
}
