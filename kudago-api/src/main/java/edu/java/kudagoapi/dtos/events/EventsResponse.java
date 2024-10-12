package edu.java.kudagoapi.dtos.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventsResponse {

    @JsonProperty("results")
    private List<EventDto> events;
}
