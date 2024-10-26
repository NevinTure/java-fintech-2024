package edu.java.kudagoapi.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {
    private Long id;
    private String name;
    @Size(min = 1)
    @NotNull
    private String slug;
    private String language;
}
