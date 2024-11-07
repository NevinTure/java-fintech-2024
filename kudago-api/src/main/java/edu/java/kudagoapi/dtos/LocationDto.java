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

    public LocationDto(String name, String slug, String language) {
        this.name = name;
        this.slug = slug;
        this.language = language;
    }
}
