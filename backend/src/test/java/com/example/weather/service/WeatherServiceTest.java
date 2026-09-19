package com.example.weather.service;

import com.example.weather.client.WeatherClient;
import com.example.weather.dto.WeatherResponse;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WeatherServiceTest {

    @Mock
    private WeatherClient weatherClient;

    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        weatherService = new WeatherService(weatherClient);
    }

    @Test
    void shouldGetWeatherForCity() throws Exception {

        String json = """
                {
                    "name": "Delhi",
                    "main": {
                        "temp": 30.5,
                        "humidity": 65
                    },
                    "weather": [
                        {
                            "description": "clear sky"
                        }
                    ]
                }
                """;

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode weatherData = objectMapper.readTree(json);

        when(weatherClient.getCurrentWeather("Delhi"))
                .thenReturn(weatherData);

        WeatherResponse response = weatherService.getWeather("Delhi");

        assertEquals("Delhi", response.getCity());
        assertEquals(30.5, response.getTemperature());
        assertEquals(65, response.getHumidity());
        assertEquals("clear sky", response.getDescription());

        verify(weatherClient).getCurrentWeather("Delhi");
    }
}