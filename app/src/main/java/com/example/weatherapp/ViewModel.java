package com.example.weatherapp;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ViewModel extends androidx.lifecycle.ViewModel {
    private final MutableLiveData<CityMoreInfoModal> weatherData = new MutableLiveData<>();
    private final MutableLiveData<String> favoriteCity = new MutableLiveData<>();

    public String getFavoriteCity() {
        return this.favoriteCity.getValue();
    }

    public void setFavoriteCity(String favoriteCity) {
        this.favoriteCity.setValue(favoriteCity);
    }

    public void setWeatherData(CityMoreInfoModal weatherData) {
        this.weatherData.setValue(weatherData);
    }

    public LiveData<CityMoreInfoModal> getWeatherData(){
        return this.weatherData;
    }
}
