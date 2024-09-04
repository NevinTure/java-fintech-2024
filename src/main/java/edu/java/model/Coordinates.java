package edu.java.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Coordinates {

    @JsonProperty("lat")
    private Double latitude;
    @JsonProperty("lon")
    private Double longitude;
}
