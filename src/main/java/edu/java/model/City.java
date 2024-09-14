package edu.java.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class City {

    private String slug;
    private Coordinates coords;
}
