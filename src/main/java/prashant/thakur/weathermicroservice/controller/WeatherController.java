package prashant.thakur.weathermicroservice.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import prashant.thakur.weathermicroservice.domain.response.WeatherResponse;
import prashant.thakur.weathermicroservice.exception.InternalServerError;
import prashant.thakur.weathermicroservice.exception.NotFoundException;
import prashant.thakur.weathermicroservice.exception.UnAuthorizedException;

import java.util.List;

@RestController
public interface WeatherController {
    ResponseEntity<List<WeatherResponse>> getWeatherForecast(String cityName, int days) throws NotFoundException, UnAuthorizedException, InternalServerError;
}