package prashant.thakur.weathermicroservice.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@NoArgsConstructor
@Data
public class WeatherData {

    private MainData main;
    private List<WeatherDescription> weather;
    private WindData wind;
    private String dt_txt;
    private int visibility;


}
