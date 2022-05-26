package com.example.weatherapp;

public class CityMoreInfoModal {

    private String feelsLike;
    private String tempMin;
    private String tempMax;
    private String pressure;
    private String seaLevel;
    private String grndLevel;
    private String humidity;
    private String description;

    public String getFeelsLike() {
        return feelsLike;
    }

    public String getTempMin() {
        return tempMin;
    }

    public String getTempMax() {
        return tempMax;
    }

    public String getPressure() {
        return pressure;
    }

    public String getSeaLevel() {
        return seaLevel;
    }

    public String getGrndLevel() {
        return grndLevel;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getDescription() { return description; }

    public void setFeelsLike(String feelsLike) {
        this.feelsLike = feelsLike;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public void setSeaLevel(String seaLevel) {
        this.seaLevel = seaLevel;
    }

    public void setGrndLevel(String grndLevel) {
        this.grndLevel = grndLevel;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public void setDescription(String description) { this.description = description; }

    public CityMoreInfoModal(String feelsLike, String tempMin, String tempMax, String pressure, String seaLevel, String grndLevel, String humidity, String description) {
        this.feelsLike = feelsLike;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.pressure = pressure;
        this.seaLevel = seaLevel;
        this.grndLevel = grndLevel;
        this.humidity = humidity;
        this.description = description;
    }
}
