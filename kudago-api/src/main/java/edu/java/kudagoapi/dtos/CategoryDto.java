package edu.java.kudagoapi.dtos;

import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    @Size(min = 2)
    private String name;
    @Size(min = 1)
    private String slug;
}
