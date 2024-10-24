package prashant.thakur.weathermicroservice.util;

import org.junit.After;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import prashant.thakur.weathermicroservice.domain.*;
import prashant.thakur.weathermicroservice.domain.*;
import prashant.thakur.weathermicroservice.utils.WeatherApiCall;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class WeatherApiCallTest {
    @Mock
    RestTemplate restTemplate;
    @InjectMocks
    private WeatherApiCall weatherApiCall;
    @Value("${openweathermap.api.key}")
    private String apiKey;
    @Value("${openweathermap.api.url}")
    private String apiUrl;
    @Value("${openweathermap.api.noOfResponseForDay}")
    private int noOfResponseForDay;

    @Value("${openweathermap.api.endpoint}")
    private String endpoint;
    private AutoCloseable closeable;
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(weatherApiCall, "apiKey", "your-api-key");
        ReflectionTestUtils.setField(weatherApiCall, "apiUrl", "http://example.com");
        ReflectionTestUtils.setField(weatherApiCall, "endpoint", "api");
        ReflectionTestUtils.setField(weatherApiCall, "noOfResponseForDay", 8);
    }
    @After
    public void close() throws Exception {
        closeable.close();
    }

    @Test
    void testGetWeatherInfo() {
        // Mock data
        WeatherData weatherData1 = WeatherData.builder().dt_txt("21-04-1995 3:00:00").visibility(500).main(MainData.builder().temp(290).temp_min(275).temp_max(300).feels_like(300).pressure(1012).humidity(70).build()).weather(List.of(WeatherDescription.builder().description("Cloudy").icon("01").main("main").id(301).build())).wind(WindData.builder().speed(5.0).build()).build();//nice weather
        WeatherData weatherData2 = WeatherData.builder().dt_txt("21-04-1995 6:00:00").visibility(500).main(MainData.builder().temp(290).temp_min(275).temp_max(300).feels_like(300).pressure(1012).humidity(70).build()).weather(List.of(WeatherDescription.builder().description("Cloudy").icon("01").main("main").id(301).build())).wind(WindData.builder().speed(1.0).speed(5.0).build()).build();
        WeatherApiResponse mockApiResponse = WeatherApiResponse.builder().city(City.builder().timezone(18000).build()).list(List.of(weatherData1, weatherData2)).build();

        // Mock behavior of RestTemplate
        when(restTemplate.getForObject(anyString(), eq(WeatherApiResponse.class))).thenReturn(mockApiResponse);
        WeatherRequest weatherRequest = WeatherRequest.builder().days(1).city("TestCity").build();

        // Perform the test
        WeatherApiResponse result = weatherApiCall.getWeatherInfo(weatherRequest);
        Assertions.assertNotNull(result);
        assertEquals(2,result.getList().size());
        assertEquals(290,result.getList().get(0).getMain().getTemp());
        assertEquals(500,result.getList().get(0).getVisibility());
        assertEquals(5,result.getList().get(0).getWind().getSpeed());
        assertEquals("Cloudy",result.getList().get(0).getWeather().get(0).getDescription());
        assertEquals(18000,result.getCity().getTimezone());
        String expectedUrl = "http://example.com/api/?q=TestCity&appid=your-api-key&cnt=8";
        verify(restTemplate, times(1)).getForObject(eq(expectedUrl), eq(WeatherApiResponse.class));
    }
}
