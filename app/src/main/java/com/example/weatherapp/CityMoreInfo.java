package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class CityMoreInfo extends Fragment {

    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_city_more_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;

        final Observer<CityMoreInfoModal> weatherDataObserver = this::setValues;

        ViewModel model = new ViewModelProvider(requireActivity()).get(ViewModel.class);
        model.getWeatherData().observe(getViewLifecycleOwner(),weatherDataObserver);

    }

    @SuppressLint("SetTextI18n")
    private void setValues(CityMoreInfoModal weatherData) {
        TextView feelsLike = view.findViewById(R.id.FeelsLike);
        feelsLike.setText(weatherData.getFeelsLike());

        TextView tempMin = view.findViewById(R.id.TempMin);
        tempMin.setText(weatherData.getTempMin());

        TextView tempMax = view.findViewById(R.id.TempMax);
        tempMax.setText(weatherData.getTempMax());

        TextView pressure = view.findViewById(R.id.Pressure);
        pressure.setText(weatherData.getPressure());

        TextView seaLevel = view.findViewById(R.id.SeaLevel);
        seaLevel.setText(weatherData.getSeaLevel());

        TextView grndLevel = view.findViewById(R.id.GrndLevel);
        grndLevel.setText(weatherData.getGrndLevel());

        TextView humidity = view.findViewById(R.id.Humidity);
        humidity.setText(weatherData.getHumidity());

        TextView description = view.findViewById(R.id.Description);
        description.setText(weatherData.getDescription());
    }
}