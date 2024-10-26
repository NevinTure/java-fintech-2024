package edu.java.kudagoapi.dtos;

import lombok.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventFilter {
    private String title;
    private String locationSlug;
    private LocalDate fromDate;
    private LocalDate toDate;
}
