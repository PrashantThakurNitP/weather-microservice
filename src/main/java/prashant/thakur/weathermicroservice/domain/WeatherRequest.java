package prashant.thakur.weathermicroservice.domain;


import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel
@SuperBuilder
public class WeatherRequest {
    String city;
    int days;
}