package prashant.thakur.weathermicroservice.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import prashant.thakur.weathermicroservice.domain.WeatherApiResponse;
import prashant.thakur.weathermicroservice.domain.WeatherRequest;

@Component
@Slf4j
@Configuration
public class WeatherApiCall {
    @Value("${openweathermap.api.key}")
    private String apiKey;

    @Value("${openweathermap.api.url}")
    private String apiUrl;
    @Value("${openweathermap.api.endpoint}")
    private String endpoint;

    @Value("${openweathermap.api.noOfResponseForDay}")
    private int noOfResponseForDay;

    private final RestTemplate restTemplate;

    @Autowired
    public WeatherApiCall(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public WeatherApiResponse getWeatherInfo(WeatherRequest weatherRequest) {
        int count = weatherRequest.getDays() * noOfResponseForDay;
        String url = String.format("%s/%s/?q=%s&appid=%s&cnt=%d", apiUrl,endpoint, weatherRequest.getCity(), apiKey,count);
        log.info("Calling Open Weather API with City : {}, Count : {}, Url: {}",weatherRequest.getCity(),count,apiUrl);
        return restTemplate.getForObject(url, WeatherApiResponse.class);

    }
}
