package edu.java.kudagoapi.services;

import edu.java.kudagoapi.dtos.EventFilter;
import edu.java.kudagoapi.dtos.LocationDto;
import edu.java.kudagoapi.dtos.events.EventDto;
import edu.java.kudagoapi.exceptions.BadRequestApiException;
import edu.java.kudagoapi.exceptions.EventNotFoundApiException;
import edu.java.kudagoapi.model.Event;
import edu.java.kudagoapi.model.Location;
import edu.java.kudagoapi.repositories.JpaEventRepository;
import edu.java.kudagoapi.repositories.JpaLocationRepository;
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

    private final JpaEventRepository eventRepo;
    private final JpaLocationRepository locationRepo;
    private final ModelMapper mapper;

    @Override
    public ResponseEntity<Object> save(EventDto dto) {
        System.out.println(dto.toString());
        Event event = mapper.map(dto, Event.class);
        event.setLocation(getLocation(dto.getLocation()));
        eventRepo.save(event);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Location getLocation(LocationDto dto) {
        List<Location> list = locationRepo.findAll(JpaLocationRepository
                .buildSpecification(dto.getId(), dto.getSlug()));
        if (!list.isEmpty()) {
            return list.getFirst();
        }
        throw new BadRequestApiException("Location not found");
    }

    @Override
    public ResponseEntity<EventDto> getById(long id) {
        Optional<Event> eventOp = eventRepo.findById(id);
        if (eventOp.isPresent()) {
            EventDto dto = mapper.map(eventOp.get(), EventDto.class);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }
        throw new EventNotFoundApiException(id);
    }

    @Override
    public ResponseEntity<List<EventDto>> findAll() {
        return new ResponseEntity<>(eventRepo.findAll()
                .stream()
                .map(v -> mapper.map(v, EventDto.class))
                .toList(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<EventDto>> findAllByFilter(EventFilter filter) {
        Location location = locationRepo.findBySlug(filter.getLocationSlug()).orElse(null);
        return new ResponseEntity<>(eventRepo.findAll(JpaEventRepository
                .buildSpecification(
                        filter.getTitle(),
                        location,
                        filter.getFromDate(),
                        filter.getToDate()))
                .stream()
                .map(v -> mapper.map(v, EventDto.class))
                .toList(),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<Object> fullUpdate(long id, EventDto dto) {
        Optional<Event> eventOp = eventRepo.findById(id);
        if (eventOp.isPresent()) {
            Event event = mapper.map(eventOp.get(), Event.class);
            event.setId(id);
            eventRepo.save(event);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new EventNotFoundApiException(id);
    }

    @Override
    public ResponseEntity<Object> deleteById(long id) {
        Optional<Event> eventOp = eventRepo.findById(id);
        if (eventOp.isPresent()) {
            eventRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new EventNotFoundApiException(id);
    }
}
