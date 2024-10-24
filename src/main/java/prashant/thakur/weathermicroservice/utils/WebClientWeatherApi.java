package prashant.thakur.weathermicroservice.utils;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import prashant.thakur.weathermicroservice.domain.WeatherApiResponse;
import prashant.thakur.weathermicroservice.domain.WeatherRequest;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@Configuration
public class WebClientWeatherApi {
    @Value("${openweathermap.api.key}")
    private String apiKey;

    @Value("${openweathermap.api.url}")
    private String apiUrl;

    @Value("${openweathermap.api.endpoint}")
    private String endpoint;

    @Value("${openweathermap.api.noOfResponseForDay}")
    private int noOfResponseForDay;

    @Value("${openweathermap.api.apiTimeOut}")
    private int timeOutDuration;

    WebClient getWebClient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeOutDuration)
                .responseTimeout(Duration.ofMillis(timeOutDuration))
                .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(timeOutDuration, TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(timeOutDuration, TimeUnit.MILLISECONDS)));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(apiUrl)
                .build();
    }

    public Mono<WeatherApiResponse> fetchDataFromApi(WeatherRequest weatherRequest) {
        int count = weatherRequest.getDays() * noOfResponseForDay;
        String uri = UriComponentsBuilder
                .fromUriString(endpoint)
                .queryParam(Constants.QUERY_PARAM_CITY, weatherRequest.getCity())
                .queryParam(Constants.QUERY_PARAM_APP_ID, apiKey)
                .queryParam(Constants.QUERY_PARAM_QTY, count)
                .build().toUriString();
        return getWebClient().get()

                .uri(uri).retrieve().bodyToMono(WeatherApiResponse.class);
    }

}
