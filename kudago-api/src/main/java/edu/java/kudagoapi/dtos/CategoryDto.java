package edu.java.kudagoapi.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    private Long id;
    private String name;
    private String slug;
}
