package edu.java.kudagoapi.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    private Long id;
    private String name;
    private String slug;
}
