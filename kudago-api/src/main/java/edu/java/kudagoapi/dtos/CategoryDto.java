package edu.java.kudagoapi.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    @Min(0)
    private Long id;
    @Size(min = 2)
    private String name;
    @Size(min = 1)
    private String slug;

    public CategoryDto(String name, String slug) {
        this.name = name;
        this.slug = slug;
    }
}
