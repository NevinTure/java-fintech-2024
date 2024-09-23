package edu.java.kudagoapi.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {
    private String name;
    private String slug;
    private String language;
}
