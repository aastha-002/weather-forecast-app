package com.example.weather.service;

import com.example.weather.client.WeatherClient;
import com.example.weather.dto.ForecastItem;
import com.example.weather.dto.ForecastResponse;
import com.example.weather.dto.WeatherResponse;
import com.example.weather.exception.CityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class WeatherService {

    private final WeatherClient weatherClient;

    public WeatherService(WeatherClient weatherClient) {
        this.weatherClient = weatherClient;
    }

    public WeatherResponse getWeather(String city) {

        try {

            JsonNode weatherData =
                    weatherClient.getCurrentWeather(city);

            String cityName =
                    weatherData
                            .get("name")
                            .asString();

            double temperature =
                    weatherData
                            .get("main")
                            .get("temp")
                            .asDouble();

            int humidity =
                    weatherData
                            .get("main")
                            .get("humidity")
                            .asInt();

            String description =
                    weatherData
                            .get("weather")
                            .get(0)
                            .get("description")
                            .asString();

            return new WeatherResponse(
                    cityName,
                    temperature,
                    humidity,
                    description
            );

        } catch (HttpClientErrorException.NotFound exception) {

            throw new CityNotFoundException(
                    "City not found: " + city
            );
        }
    }


    public ForecastResponse getForecast(String city) {

        try {

            JsonNode forecastData =
                    weatherClient.getForecast(city);

            String cityName =
                    forecastData
                            .get("city")
                            .get("name")
                            .asString();


            /*
             * Group all forecast readings
             * according to their date.
             */
            Map<String, List<JsonNode>> dailyData =
                    new LinkedHashMap<>();

            for (JsonNode item : forecastData.get("list")) {

                String dateTime =
                        item
                                .get("dt_txt")
                                .asString();

                String date =
                        dateTime.split(" ")[0];

                dailyData
                        .computeIfAbsent(
                                date,
                                key -> new ArrayList<>()
                        )
                        .add(item);
            }


            List<ForecastItem> forecastItems =
                    new ArrayList<>();


            /*
             * Calculate minimum temperature,
             * maximum temperature and weather
             * information for every day.
             */
            for (Map.Entry<String, List<JsonNode>> entry
                    : dailyData.entrySet()) {

                String date =
                        entry.getKey();

                List<JsonNode> items =
                        entry.getValue();


                double minTemperature =
                        Double.MAX_VALUE;

                double maxTemperature =
                        -Double.MAX_VALUE;


                /*
                 * Use the first forecast entry
                 * of the day for description
                 * and icon.
                 */
                String description =
                        items.get(0)
                                .get("weather")
                                .get(0)
                                .get("description")
                                .asString();

                String icon =
                        items.get(0)
                                .get("weather")
                                .get(0)
                                .get("icon")
                                .asString();


                /*
                 * Go through every 3-hour forecast
                 * for this day and calculate
                 * minimum and maximum temperature.
                 */
                for (JsonNode item : items) {

                    double temperature =
                            item
                                    .get("main")
                                    .get("temp")
                                    .asDouble();

                    minTemperature =
                            Math.min(
                                    minTemperature,
                                    temperature
                            );

                    maxTemperature =
                            Math.max(
                                    maxTemperature,
                                    temperature
                            );
                }


                /*
                 * Create our own forecast object
                 * for the frontend.
                 */
                forecastItems.add(
                        new ForecastItem(
                                date,
                                minTemperature,
                                maxTemperature,
                                description,
                                icon
                        )
                );
            }


            return new ForecastResponse(
                    cityName,
                    forecastItems
            );


        } catch (HttpClientErrorException.NotFound exception) {

            throw new CityNotFoundException(
                    "City not found: " + city
            );
        }
    }
}