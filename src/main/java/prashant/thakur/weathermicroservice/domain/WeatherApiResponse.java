package prashant.thakur.weathermicroservice.domain;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@ApiModel
@SuperBuilder
@NoArgsConstructor
@Data
public class WeatherApiResponse {
    private List<WeatherData> list;
    private City city;
}
