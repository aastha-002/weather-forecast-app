package com.example.weather.dto;

import java.util.List;

public class ForecastResponse {

    private String city;
    private List<ForecastItem> forecast;

    public ForecastResponse(String city, List<ForecastItem> forecast) {
        this.city = city;
        this.forecast = forecast;
    }

    public String getCity() {
        return city;
    }

    public List<ForecastItem> getForecast() {
        return forecast;
    }
}