package edu.java.kudagoapi.model;

import lombok.*;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class CategorySnapshot {

    private Long id;
    private String name;
    private String slug;
    private OffsetDateTime changeAt;

    public CategorySnapshot() {
        changeAt = OffsetDateTime.now().withNano(0);
    }
}
