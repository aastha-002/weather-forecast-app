package com.example.weather.dto;

public class ForecastItem {

    private String dateTime;
    private double minTemperature;
    private double maxTemperature;
    private String description;
    private String icon;

    public ForecastItem(
            String dateTime,
            double minTemperature,
            double maxTemperature,
            String description,
            String icon) {

        this.dateTime = dateTime;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.description = description;
        this.icon = icon;
    }

    public String getDateTime() {
        return dateTime;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }
}