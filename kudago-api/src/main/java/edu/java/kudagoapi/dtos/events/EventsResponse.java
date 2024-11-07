package edu.java.kudagoapi.dtos.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class EventsResponse {

    @JsonProperty("results")
    private List<EventDtoResponse> events;

    public EventsResponse() {
        this.events = new ArrayList<>();
    }
}
