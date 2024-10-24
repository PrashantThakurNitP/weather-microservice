package prashant.thakur.weathermicroservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import prashant.thakur.weathermicroservice.domain.response.DailyWeather;
import prashant.thakur.weathermicroservice.domain.WeatherRequest;
import prashant.thakur.weathermicroservice.domain.response.WeatherResponse;
import prashant.thakur.weathermicroservice.exception.NotFoundException;
import prashant.thakur.weathermicroservice.service.WeatherServiceImpl;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class WeatherControllerImplTest {

    private MockMvc mockMvc;

    @Mock
    private WeatherServiceImpl weatherService;

    @InjectMocks
    private WeatherControllerImpl weatherController;

    private AutoCloseable closeable;

    public static String asJsonString(final  Object obj){
        try {
            return new ObjectMapper().writeValueAsString(obj);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Before
    public void setUp(){
        closeable = MockitoAnnotations.openMocks(this);

        this.mockMvc = MockMvcBuilders.standaloneSetup(weatherController)
                .build();
    }

    @After
    public void close() throws Exception {
        closeable.close();
    }

    @Test
    public void testGetWeatherForecast() throws Exception {
        // Mock data
        String cityName = "TestCity";
        int days = 5;

        WeatherRequest weatherRequest = new WeatherRequest();
        weatherRequest.setCity(cityName);
        weatherRequest.setDays(days);
        WeatherResponse weatherResponse1 = WeatherResponse.builder()
                .dailyWeathers(DailyWeather.builder().maxTemperature(300).minTemperature(273)
                        .date("2024-01-20").time("15:00:00").build())
                .icon("01n").description("clear sky").message("Nice Weather").build();
        WeatherResponse weatherResponse2 = WeatherResponse.builder()
                .dailyWeathers(DailyWeather.builder().maxTemperature(310).minTemperature(283)
                        .date("2024-01-20").time("15:00:00").build())
                .icon("01n").description("Cloudy").message("Nice Weather").build();
        List<WeatherResponse> mockWeatherResponseList = Arrays.asList(weatherResponse1, weatherResponse2);

        when(weatherService.getWeatherForCity(weatherRequest)).thenReturn(mockWeatherResponseList);

        mockMvc.perform(get("/v1/weather")
                        .param("cityName", cityName)
                        .param("days", String.valueOf(days))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(mockWeatherResponseList)));

    }
    @Test
    public void testGetWeatherForecastCityNotFound() throws Exception {

        String cityName = "TestCity";
        int days = 5;

        WeatherRequest weatherRequest = new WeatherRequest();
        weatherRequest.setCity(cityName);
        weatherRequest.setDays(days);
        when(weatherService.getWeatherForCity(weatherRequest)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/v1/weather")
                        .param("cityName", cityName)
                        .param("days", String.valueOf(days))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());


    }
}
