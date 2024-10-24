package edu.java.kudagoapi.dtos.events;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceDto {
    private Long id;
    private String title;
    private String address;
    private String phone;
}
