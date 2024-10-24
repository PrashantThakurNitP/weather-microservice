package prashant.thakur.weathermicroservice.utils;

import prashant.thakur.weathermicroservice.domain.WeatherData;
import prashant.thakur.weathermicroservice.domain.response.DailyWeather;

public class Utility {
    public static String getSuggestionForWeather(WeatherData weatherData) {
        double temperature = weatherData.getMain().getTemp();
        int weatherId = !weatherData.getWeather().isEmpty() ? weatherData.getWeather().get(0).getId():-1;
        double windSpeed = weatherData.getWind().getSpeed();
        int humidity = weatherData.getMain().getHumidity();
        int visibility = weatherData.getVisibility();

        if (Constants.isSuperHot.apply(temperature)) {
            return Constants.MESSAGE_FOR_HIGH_TEMP;
        } else if (Constants.isRainy.apply(weatherId)) {
            return Constants.MESSAGE_FOR_RAIN;
        } else if (Constants.isHighWind.apply(windSpeed)) {
            return Constants.MESSAGE_FOR_HIGH_WINDS;
        } else if (Constants.isThunderstorm.apply(weatherId)) {
            return Constants.MESSAGE_FOR_THUNDERSTORM;
        } else if (Constants.isChilling.apply(temperature)) {
            return Constants.MESSAGE_FOR_LOW_TEMP;
        }
        else if (Constants.isSlightlyCold.apply(temperature)) {
            return Constants.MESSAGE_FOR_SLIGHT_LOW_TEMP;
        }
        else if (Constants.isVisibilityLow.apply(visibility)) {
            return Constants.MESSAGE_FOR_SLIGHT_LOW_VISIBILITY;
        }
        else if (Constants.isHumid.apply(humidity)) {
            return Constants.MESSAGE_FOR_HUMIDITY;
        }else {
            return Constants.MESSAGE_FOR_NICE_WEATHER;
        }
    }

    public static DailyWeather createDailyWeather(WeatherData weatherData) {
        return DailyWeather.builder()
                .minTemperature(Constants.kelvinToCelsius.apply(weatherData.getMain().getTemp_min()))
                .maxTemperature(Constants.kelvinToCelsius.apply(weatherData.getMain().getTemp_max()))
                .temperature(Constants.kelvinToCelsius.apply(weatherData.getMain().getTemp()))
                .windSpeed(weatherData.getWind().getSpeed())
                .date(Constants.extractDate.apply(weatherData))
                .time(Constants.extractTime.apply(weatherData))
                .humidity(weatherData.getMain().getHumidity())
                .pressure(weatherData.getMain().getPressure())
                .feelsLike(Constants.kelvinToCelsius.apply(weatherData.getMain().getFeels_like()))
                .visibility(weatherData.getVisibility())
                .build();
    }
}
