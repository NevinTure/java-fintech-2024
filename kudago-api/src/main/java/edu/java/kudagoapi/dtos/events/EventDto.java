package edu.java.kudagoapi.dtos.events;

import edu.java.kudagoapi.dtos.LocationDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {
    private Long id;
    @Size(min = 1)
    private String title;
    @Size(min = 1)
    @NotNull
    private String slug;
    private LocalDate date;
    @Valid
    @NotNull
    private LocationDto location;
}
