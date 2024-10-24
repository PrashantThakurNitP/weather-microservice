package prashant.thakur.weathermicroservice.domain.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@ApiModel
@SuperBuilder
public class WeatherResponse {

    @ApiModelProperty
    private String message;

    @ApiModelProperty
    private String icon;

    @ApiModelProperty
    private String description;

    @ApiModelProperty
    private DailyWeather dailyWeathers;

    @ApiModelProperty
    private String  weatherType;
    @ApiModelProperty
    private int timezoneOffset;

}
