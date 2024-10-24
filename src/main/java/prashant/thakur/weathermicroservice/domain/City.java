package prashant.thakur.weathermicroservice.domain;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@ApiModel
@SuperBuilder
@NoArgsConstructor
@Data
public class City {
    private int timezone;
}
