package edu.java.kudagoapi.dtos;

import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {
    @Size(min = 1)
    private String name;
    @Size(min = 1)
    private String slug;
    @Size(min = 1)
    private String language;
}
