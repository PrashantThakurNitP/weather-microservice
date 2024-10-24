package prashant.thakur.weathermicroservice.controller;

import org.springframework.http.ResponseEntity;
import prashant.thakur.weathermicroservice.domain.response.WeatherResponse;
import prashant.thakur.weathermicroservice.exception.InternalServerError;
import prashant.thakur.weathermicroservice.exception.NotFoundException;
import prashant.thakur.weathermicroservice.exception.UnAuthorizedException;
import reactor.core.publisher.Flux;

public interface WeatherForecastController {
    ResponseEntity<Flux<WeatherResponse>> getWeatherForecastFlux(String cityName, int days) throws NotFoundException, UnAuthorizedException, InternalServerError;
}
