package edu.java.kudagoapi.dtos.events;

import edu.java.kudagoapi.dtos.LocationDto;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {
    private Long id;
    private PlaceDto place;
    private LocationDto location;
    private String price;
}
