package prashant.thakur.weathermicroservice.controller;


import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prashant.thakur.weathermicroservice.domain.response.WeatherResponse;
import prashant.thakur.weathermicroservice.exception.NotFoundException;
import prashant.thakur.weathermicroservice.domain.WeatherRequest;
import prashant.thakur.weathermicroservice.exception.InternalServerError;
import prashant.thakur.weathermicroservice.exception.UnAuthorizedException;
import prashant.thakur.weathermicroservice.service.WeatherService;

import java.util.List;

@RestController
@RequestMapping("/v1")
@Slf4j
@CrossOrigin(origins = "*")
public class WeatherControllerImpl implements WeatherController {

    @Autowired
    private WeatherService weatherService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/weather", produces = "application/json")
    @ApiOperation(value= "API to service Weather Forecast for a City")
    public ResponseEntity<List<WeatherResponse>> getWeatherForecast(@RequestParam String cityName, @RequestParam int days) throws NotFoundException, UnAuthorizedException, InternalServerError {
        log.info("Get weather forecast  for city : {} days : {}",cityName,days);
        WeatherRequest weatherRequest = new WeatherRequest();
        weatherRequest.setCity(cityName);
        weatherRequest.setDays(days);
        return new ResponseEntity<>(weatherService.getWeatherForCity(weatherRequest), HttpStatus.OK);
    }
}