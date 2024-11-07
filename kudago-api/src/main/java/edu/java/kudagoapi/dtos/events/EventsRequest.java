package edu.java.kudagoapi.dtos.events;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventsRequest {

    private List<String> fields;
    private List<String> expand;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Integer pageSize;
}
