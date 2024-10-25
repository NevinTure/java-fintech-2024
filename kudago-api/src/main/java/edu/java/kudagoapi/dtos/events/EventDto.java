package edu.java.kudagoapi.dtos.events;

import edu.java.kudagoapi.dtos.LocationDto;
import lombok.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {
    private Long id;
    private String title;
    private String slug;
    private LocalDate date;
    private LocationDto location;

    public EventDto(String title, String slug, LocalDate date, LocationDto location) {
        this.title = title;
        this.slug = slug;
        this.date = date;
        this.location = location;
    }
}
