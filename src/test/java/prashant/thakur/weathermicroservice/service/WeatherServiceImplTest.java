package prashant.thakur.weathermicroservice.service;

import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import prashant.thakur.weathermicroservice.domain.*;
import prashant.thakur.weathermicroservice.domain.response.WeatherResponse;
import prashant.thakur.weathermicroservice.exception.InternalServerError;
import prashant.thakur.weathermicroservice.exception.NotFoundException;
import prashant.thakur.weathermicroservice.exception.UnAuthorizedException;
import prashant.thakur.weathermicroservice.utils.WeatherApiCall;

import java.nio.charset.Charset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static prashant.thakur.weathermicroservice.utils.Constants.*;

class WeatherServiceImplTest {

    @Mock
    private WeatherApiCall weatherApiCall;

    @InjectMocks
    private WeatherServiceImpl weatherService;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @After
    public void close() throws Exception {
        closeable.close();
    }

    @Test
    void testGetWeatherForCity_Success() throws NotFoundException, UnAuthorizedException, InternalServerError {
        // Mock data
        WeatherRequest weatherRequest = new WeatherRequest("TestCity", 1);
        WeatherApiResponse mockResponse = createMockWeatherApiResponse();

        when(weatherApiCall.getWeatherInfo(any())).thenReturn(mockResponse);

        // Perform the test
        List<WeatherResponse> result = weatherService.getWeatherForCity(weatherRequest);
        // Assertions
        assertNotNull(result);
        assertEquals(8, result.size());
        assertEquals(MESSAGE_FOR_NICE_WEATHER, result.get(0).getMessage());
        assertEquals("21-04-1995", result.get(0).getDailyWeathers().getDate());
        assertEquals("3:00:00", result.get(0).getDailyWeathers().getTime());
        assertEquals(27, result.get(0).getDailyWeathers().getMaxTemperature());
        assertEquals(2, result.get(0).getDailyWeathers().getMinTemperature());
        assertEquals(5, result.get(0).getDailyWeathers().getFeelsLike());
        assertEquals(50, result.get(0).getDailyWeathers().getHumidity());
        assertEquals(1012, result.get(0).getDailyWeathers().getPressure());
        assertEquals(500, result.get(0).getDailyWeathers().getVisibility());
        assertEquals(5, result.get(0).getDailyWeathers().getWindSpeed());
        assertEquals("Cloudy", result.get(0).getDescription());
        assertEquals("01", result.get(0).getIcon());
        assertEquals("Hot", result.get(0).getWeatherType());
        assertEquals(18000, result.get(0).getTimezoneOffset());

    }
    @Test
    void testGetWeatherForCity_VerifyWarningMessage() throws NotFoundException, UnAuthorizedException, InternalServerError {
        // Mock data
        WeatherRequest weatherRequest = new WeatherRequest("TestCity", 1);
        WeatherApiResponse mockResponse = createMockWeatherApiResponseForMessage();

        when(weatherApiCall.getWeatherInfo(any())).thenReturn(mockResponse);

        // Perform the test
        List<WeatherResponse> result = weatherService.getWeatherForCity(weatherRequest);
        // Assertions
        assertNotNull(result);
        assertEquals(8, result.size());
        assertEquals(MESSAGE_FOR_SLIGHT_LOW_TEMP, result.get(0).getMessage());
        assertEquals(MESSAGE_FOR_SLIGHT_LOW_VISIBILITY, result.get(1).getMessage());
        assertEquals(MESSAGE_FOR_HUMIDITY, result.get(2).getMessage());
        assertEquals(MESSAGE_FOR_THUNDERSTORM, result.get(3).getMessage());
        assertEquals(MESSAGE_FOR_RAIN, result.get(4).getMessage());
        assertEquals(MESSAGE_FOR_LOW_TEMP, result.get(5).getMessage());
        assertEquals(MESSAGE_FOR_HIGH_WINDS, result.get(6).getMessage());
        assertEquals(MESSAGE_FOR_HIGH_TEMP, result.get(7).getMessage());


    }

    @Test
    void testGetWeatherForCity_NotFound() {
        WeatherRequest weatherRequest = new WeatherRequest("TestCity", 5);
        when(weatherApiCall.getWeatherInfo(any())).thenThrow(HttpClientErrorException.create("not found", HttpStatus.NOT_FOUND, "400", new HttpHeaders(), new byte[]{}, Charset.defaultCharset()));
        assertThrows(NotFoundException.class, () -> weatherService.getWeatherForCity(weatherRequest));
    }

    @Test
    void testGetWeatherForCity_Unauthorized() {
        WeatherRequest weatherRequest = new WeatherRequest("TestCity", 5);
        when(weatherApiCall.getWeatherInfo(any())).thenThrow(HttpClientErrorException.create("not found", HttpStatus.UNAUTHORIZED, "400", new HttpHeaders(), new byte[]{}, Charset.defaultCharset()));
        assertThrows(UnAuthorizedException.class, () -> weatherService.getWeatherForCity(weatherRequest));
    }

    @Test
    void testGetWeatherForCity_InternalServerError() {
        WeatherRequest weatherRequest = new WeatherRequest("TestCity", 5);
        when(weatherApiCall.getWeatherInfo(any())).thenThrow(new RuntimeException("exception"));
        assertThrows(InternalServerError.class, () -> weatherService.getWeatherForCity(weatherRequest));
    }

    private WeatherApiResponse createMockWeatherApiResponse() {
        WeatherData weatherData1 = WeatherData.builder().dt_txt("21-04-1995 3:00:00").visibility(500).main(MainData.builder().temp(290).temp_min(275).temp_max(300).humidity(50).pressure(1012).feels_like(278).build()).weather(List.of(WeatherDescription.builder().description("Cloudy").icon("01").main("Hot").id(301).build())).wind(WindData.builder().speed(5.0).build()).build();//nice weather
        WeatherData weatherData2 = WeatherData.builder().dt_txt("21-04-1995 6:00:00").visibility(90).main(MainData.builder().temp(290).temp_min(275).temp_max(300).humidity(50).pressure(1012).feels_like(278).build()).weather(List.of(WeatherDescription.builder().description("Cloudy").icon("01").main("main").id(301).build())).wind(WindData.builder().speed(1.0).build()).build();//low visibility
        WeatherData weatherData3 = WeatherData.builder().dt_txt("21-04-1995 9:00:00").visibility(500).main(MainData.builder().temp(290).temp_min(275).temp_max(300).humidity(80).pressure(1012).feels_like(278).build()).weather(List.of(WeatherDescription.builder().description("Cloudy").icon("01").main("main").id(303).build())).wind(WindData.builder().speed(5.0).build()).build();//humid

        WeatherData weatherData4 = WeatherData.builder().dt_txt("21-04-1995 12:00:00").visibility(500).main(MainData.builder().temp(291).temp_min(275).temp_max(300).humidity(50).pressure(1012).feels_like(278).build()).weather(List.of(WeatherDescription.builder().description("Cloudy").icon("01").main("main").id(201).build())).wind(WindData.builder().speed(6.0).build()).build();//thunderstorm
        WeatherData weatherData5 = WeatherData.builder().dt_txt("21-04-1995 15:00:00").visibility(500).main(MainData.builder().temp(290).temp_min(275).temp_max(300).humidity(50).pressure(1012).feels_like(278).build()).weather(List.of(WeatherDescription.builder().description("Cloudy").icon("01").main("main").id(501).build())).wind(WindData.builder().speed(9.0).build()).build();//rain
        WeatherData weatherData6 = WeatherData.builder().dt_txt("21-04-1995 18:00:00").visibility(500).main(MainData.builder().temp(273).temp_min(275).temp_max(300).humidity(50).pressure(1012).feels_like(278).build()).weather(List.of(WeatherDescription.builder().description("Cloudy").icon("01").main("main").id(401).build())).wind(WindData.builder().speed(9.0).build()).build();//low temp

        WeatherData weatherData7 = WeatherData.builder().dt_txt("21-04-1995 21:00:00").visibility(500).main(MainData.builder().temp(293).temp_min(275).temp_max(300).humidity(50).pressure(1012).feels_like(278).build()).weather(List.of(WeatherDescription.builder().description("Cloudy").icon("01").main("main").build())).wind(WindData.builder().speed(20.0).build()).build();//high winds
        WeatherData weatherData8 = WeatherData.builder().dt_txt("22-04-1995 00:00:00").visibility(500).main(MainData.builder().temp(314).temp_min(275).temp_max(300).humidity(50).pressure(1012).feels_like(278).build()).weather(List.of(WeatherDescription.builder().description("Rainy").icon("01").main("main").build())).wind(WindData.builder().speed(6.0).build()).build();//high temp
        return WeatherApiResponse.builder().city(City.builder().timezone(18000).build()).list(List.of(weatherData1, weatherData2, weatherData3, weatherData4, weatherData5, weatherData6, weatherData7, weatherData8)).build();
    }
    private WeatherApiResponse createMockWeatherApiResponseForMessage() {
        WeatherData weatherData1 = WeatherData.builder().dt_txt("21-04-1995 3:00:00").visibility(500).main(MainData.builder().temp(287).temp_min(275).temp_max(300).humidity(50).pressure(1012).feels_like(278).build()).weather(List.of(WeatherDescription.builder().id(301).build())).wind(WindData.builder().speed(5.0).build()).build();//slight low temp
        WeatherData weatherData2 = WeatherData.builder().dt_txt("21-04-1995 6:00:00").visibility(90).main(MainData.builder().temp(290).temp_min(275).temp_max(300).humidity(50).pressure(1012).feels_like(278).build()).weather(List.of(WeatherDescription.builder().id(301).build())).wind(WindData.builder().speed(1.0).build()).build();//low visibility
        WeatherData weatherData3 = WeatherData.builder().dt_txt("21-04-1995 9:00:00").visibility(500).main(MainData.builder().temp(290).temp_min(275).temp_max(300).humidity(80).pressure(1012).feels_like(278).build()).weather(List.of(WeatherDescription.builder().id(303).build())).wind(WindData.builder().speed(5.0).build()).build();//humid

        WeatherData weatherData4 = WeatherData.builder().dt_txt("21-04-1995 12:00:00").visibility(500).main(MainData.builder().temp(291).temp_min(275).temp_max(300).humidity(50).pressure(1012).feels_like(278).build()).weather(List.of(WeatherDescription.builder().id(201).build())).wind(WindData.builder().speed(6.0).build()).build();//thunderstorm
        WeatherData weatherData5 = WeatherData.builder().dt_txt("21-04-1995 15:00:00").visibility(500).main(MainData.builder().temp(290).temp_min(275).temp_max(300).humidity(50).pressure(1012).feels_like(278).build()).weather(List.of(WeatherDescription.builder().id(501).build())).wind(WindData.builder().speed(9.0).build()).build();//rain
        WeatherData weatherData6 = WeatherData.builder().dt_txt("21-04-1995 18:00:00").visibility(500).main(MainData.builder().temp(273).temp_min(275).temp_max(300).humidity(50).pressure(1012).feels_like(278).build()).weather(List.of(WeatherDescription.builder().id(401).build())).wind(WindData.builder().speed(9.0).build()).build();//low temp

        WeatherData weatherData7 = WeatherData.builder().dt_txt("21-04-1995 21:00:00").visibility(500).main(MainData.builder().temp(293).temp_min(275).temp_max(300).humidity(50).pressure(1012).feels_like(278).build()).weather(List.of(WeatherDescription.builder().build())).wind(WindData.builder().speed(20.0).build()).build();//high winds
        WeatherData weatherData8 = WeatherData.builder().dt_txt("22-04-1995 00:00:00").visibility(500).main(MainData.builder().temp(314).temp_min(275).temp_max(300).humidity(50).pressure(1012).feels_like(278).build()).weather(List.of(WeatherDescription.builder().build())).wind(WindData.builder().speed(6.0).build()).build();//high temp
        return WeatherApiResponse.builder().city(City.builder().timezone(18000).build()).list(List.of(weatherData1, weatherData2, weatherData3, weatherData4, weatherData5, weatherData6, weatherData7, weatherData8)).build();
    }
}

