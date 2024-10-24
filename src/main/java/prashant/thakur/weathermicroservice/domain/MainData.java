package prashant.thakur.weathermicroservice.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Data
public class MainData {
    int temp;
    int temp_min;
    int temp_max;
    int feels_like;
    int pressure;
    int humidity;
}
