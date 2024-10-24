package prashant.thakur.weathermicroservice.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Data
public class WeatherDescription {
    int id;
    String main;
    String description;
    String icon;
}
