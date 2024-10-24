package prashant.thakur.weathermicroservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import prashant.thakur.weathermicroservice.domain.response.WeatherResponse;
import prashant.thakur.weathermicroservice.exception.InternalServerError;
import prashant.thakur.weathermicroservice.exception.NotFoundException;
import prashant.thakur.weathermicroservice.exception.UnAuthorizedException;
import prashant.thakur.weathermicroservice.domain.WeatherApiResponse;
import prashant.thakur.weathermicroservice.domain.WeatherRequest;
import prashant.thakur.weathermicroservice.utils.WebClientWeatherApi;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import static prashant.thakur.weathermicroservice.utils.Utility.getSuggestionForWeather;
import static prashant.thakur.weathermicroservice.utils.Utility.createDailyWeather;

@Service
@Slf4j
public class WeatherForecastServiceImpl implements WeatherForecastService{


    @Autowired
    private WebClientWeatherApi webClientWeatherApi;

    @Override
    public Flux<WeatherResponse> getWeatherForCityNonBlocking(WeatherRequest weatherRequest) throws NotFoundException, UnAuthorizedException, InternalServerError {


        return webClientWeatherApi.fetchDataFromApi(weatherRequest)
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(this::mapWeatherResponseNonBlocking)
                .onErrorMap(throwable -> {
                    if (throwable instanceof WebClientResponseException.NotFound) {
                        log.error("NotFound HttpClientErrorException Exception Occurred", throwable);
                        return new NotFoundException(throwable.getMessage());
                    } else if (throwable instanceof WebClientResponseException.Unauthorized) {
                        log.error("Unauthorized HttpClientErrorException Exception Occurred", throwable);
                        return new UnAuthorizedException("Unable to fetch Weather!");
                    } else {
                        log.error("Exception Occurred", throwable);
                        return new InternalServerError(throwable.getMessage());
                    }
                });


    }


    private Flux<WeatherResponse> mapWeatherResponseNonBlocking(WeatherApiResponse response) {
        return Flux.fromIterable(response.getList()).map(weatherData ->
                WeatherResponse.builder()
                .dailyWeathers(createDailyWeather(weatherData))
                .message(getSuggestionForWeather(weatherData))
                .weatherType(weatherData.getWeather().get(0).getMain())
                .icon(weatherData.getWeather().get(0).getIcon())
                .description(weatherData.getWeather().get(0).getDescription())
                .timezoneOffset(response.getCity().getTimezone())
                .build());
    }

}
