package edu.java.kudagoapi.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    private Long id;
    private String name;
    private String slug;
    private String language;
}
