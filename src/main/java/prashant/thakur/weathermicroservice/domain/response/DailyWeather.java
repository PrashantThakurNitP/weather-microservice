package prashant.thakur.weathermicroservice.domain.response;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@ApiModel
@SuperBuilder
@NoArgsConstructor
public class DailyWeather {
    int minTemperature;
    int maxTemperature;
    int feelsLike;
    int humidity;
    int pressure;
    int temperature;
    String date;
    String time;
    Double windSpeed;
    int visibility;
}