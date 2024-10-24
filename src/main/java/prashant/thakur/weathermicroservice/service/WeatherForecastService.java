package prashant.thakur.weathermicroservice.service;

import org.springframework.stereotype.Service;
import prashant.thakur.weathermicroservice.domain.response.WeatherResponse;
import prashant.thakur.weathermicroservice.exception.InternalServerError;
import prashant.thakur.weathermicroservice.exception.NotFoundException;
import prashant.thakur.weathermicroservice.exception.UnAuthorizedException;
import prashant.thakur.weathermicroservice.domain.WeatherRequest;
import reactor.core.publisher.Flux;
@Service
public interface WeatherForecastService {
    Flux<WeatherResponse> getWeatherForCityNonBlocking(WeatherRequest weatherRequest) throws NotFoundException, UnAuthorizedException, InternalServerError;
}
