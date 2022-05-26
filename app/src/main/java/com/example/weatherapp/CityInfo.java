package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class CityInfo extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private TextView cityNameTV, temperatureTV, conditionTV;
    private RecyclerView weatherRV;
    private EditText cityEdt;
    private ImageView iconIV, searchIV;

    private ArrayList<WeatherRVModal> weatherRVModalArrayList;
    private WeatherRVAdapter weatherRVAdapter;

    private Button refreshButton;

    private ViewModel viewModel;

    private String cityName;

    private String data;
    private String dataValues[];

    void setCityName(String cityName) {
        this.cityName = cityName;
    }
    void setData(String data) { this.data = data; }
    String getData() { return data; }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_city_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cityNameTV = view.findViewById(R.id.idTVCityName);
        temperatureTV = view.findViewById(R.id.idTVTemperature);
        conditionTV = view.findViewById(R.id.idTVCondition);
        weatherRV = view.findViewById(R.id.idRVWeather);
        cityEdt = view.findViewById(R.id.idEdtCity);
        iconIV = view.findViewById(R.id.idIVIcon);
        searchIV = view.findViewById(R.id.idIVSearch);
        refreshButton = view.findViewById(R.id.button_refresh);

        weatherRVModalArrayList = new ArrayList<>();
        weatherRVAdapter = new WeatherRVAdapter(weatherRVModalArrayList);
        weatherRV.setAdapter(weatherRVAdapter);

        viewModel = new ViewModelProvider(getActivity()).get(ViewModel.class);

        setCityName("Lodz");
        getWeatherInfo(cityName);

        setData(readFromFile(getActivity()));
        // System.out.println(getData());

        getDataValues(getData());
//        for(String val: dataValues)
//            System.out.println(val);

        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = cityEdt.getText().toString();
                if(city.isEmpty())
                    Toast.makeText(getActivity(), "Please enter city name", Toast.LENGTH_SHORT).show();
                else {
                    cityNameTV.setText(cityName);
                    getWeatherInfo(city);
                }
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWeatherInfo(cityNameTV.getText().toString());
                Toast.makeText(getActivity(), "Data refreshed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    void getDataValues(String str) {
        dataValues = str.split(System.lineSeparator());
    }

    @Override
    public void onResume() {
        super.onResume();
        if(viewModel.getFavoriteCity() != null)
            cityNameTV.setText(viewModel.getFavoriteCity());
    }

    @SuppressLint("SetTextI18n")
    void putDataValues() {
        temperatureTV.setText(dataValues[1] + "°C");
        conditionTV.setText(dataValues[2]);
        Picasso.get().load("https://openweathermap.org/img/wn/".concat(dataValues[3]).concat("@2x.png")).into(iconIV);
    }

    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    private void getWeatherInfo(String cityName) {
        String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + cityName + "&appid=c4943e245639be2b01bad7556a3b6510";
        cityNameTV.setText(cityName);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onResponse(JSONObject response) {
//                loadingPB.setVisibility(View.GONE);
//                homeRL.setVisibility(View.VISIBLE);
                weatherRVModalArrayList.clear();

                try {

                    String temperature = response.getJSONArray("list").getJSONObject(0).getJSONObject("main").getString("temp");
                    float temperatureFloat = (float) (Float.parseFloat(temperature) - 273.15);
                    DecimalFormat df = new DecimalFormat("##.#");
                    // String temperatureFormatted = df.format(temperatureFloat);
                    temperatureTV.setText(String.valueOf(df.format(temperatureFloat)) + "°C");

                    String condition = response.getJSONArray("list").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("main");
                    String conditionIcon = response.getJSONArray("list").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("icon");

                    Picasso.get().load("https://openweathermap.org/img/wn/".concat(conditionIcon).concat("@2x.png")).into(iconIV);

                    conditionTV.setText(condition);

                    // tu zapisywac dane more_info
                    String temperatureFeel = response.getJSONArray("list").getJSONObject(0).getJSONObject("main").getString("feels_like");
                    float temperatureFeelFloat = (float) (Float.parseFloat(temperatureFeel) - 273.15);

                    String temperatureMin = response.getJSONArray("list").getJSONObject(0).getJSONObject("main").getString("temp_min");
                    float temperatureMinFloat = (float) (Float.parseFloat(temperatureMin) - 273.15);

                    String temperatureMax = response.getJSONArray("list").getJSONObject(0).getJSONObject("main").getString("temp_max");
                    float temperatureMaxFloat = (float) (Float.parseFloat(temperatureMax) - 273.15);

                    String pressure = response.getJSONArray("list").getJSONObject(0).getJSONObject("main").getString("pressure");
                    String seaLevel = response.getJSONArray("list").getJSONObject(0).getJSONObject("main").getString("sea_level");
                    String grndLevel = response.getJSONArray("list").getJSONObject(0).getJSONObject("main").getString("grnd_level");
                    String humidity = response.getJSONArray("list").getJSONObject(0).getJSONObject("main").getString("humidity");
                    String description = response.getJSONArray("list").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("description");

                    // zapis do view model
                    viewModel.setWeatherData(new CityMoreInfoModal(String.valueOf(df.format(temperatureFeelFloat)) + "°C",String.valueOf(df.format(temperatureMinFloat)) + "°C",String.valueOf(df.format(temperatureMaxFloat)) + "°C",pressure + "hPa",seaLevel + "hPa",grndLevel + "hPa",humidity + "%", description));

                    JSONArray forecastObj = response.getJSONArray("list");

                    for (int i=1;i<9;i++) {
                        JSONObject hourObj = forecastObj.getJSONObject(i);

                        String time = hourObj.getString("dt_txt");

                        String temper = hourObj.getJSONObject("main").getString("temp");
                        temperatureFloat = (float) (Float.parseFloat(temper) - 273.15);
                        temper = String.valueOf(df.format(temperatureFloat));

                        String img = hourObj.getJSONArray("weather").getJSONObject(0).getString("icon");

                        String wind = hourObj.getJSONObject("wind").getString("speed");

                        weatherRVModalArrayList.add(new WeatherRVModal(time, temper, img, wind));
                    }
                    //viewModel.setWeatherData(weatherRVModalArrayList.get(0));

                    weatherRVAdapter.notifyDataSetChanged();

                    //zapis do stringa
                    String dt = String.valueOf(df.format(temperatureFloat)) + '\n'
                            + condition + '\n'
                            + conditionIcon + '\n'
                            + String.valueOf(df.format(temperatureFeelFloat)) + '\n'
                            + String.valueOf(df.format(temperatureMinFloat)) + '\n'
                            + String.valueOf(df.format(temperatureMaxFloat)) + '\n'
                            + pressure + '\n'
                            + seaLevel + '\n'
                            + grndLevel + '\n'
                            + humidity + '\n'
                            + description + '\n';

                    try {
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getActivity().openFileOutput("config.txt", Context.MODE_PRIVATE));
                        outputStreamWriter.write(dt);
                        outputStreamWriter.close();
                    }
                    catch (IOException e) {
                        Log.e("Exception", "File write failed: " + e.toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                if(error instanceof NoConnectionError)
                {
                    putDataValues();
                    viewModel.setWeatherData(new CityMoreInfoModal(dataValues[4] + "°C",dataValues[5] + "°C",dataValues[6] + "°C",dataValues[7] + "hPa",dataValues[8] + "hPa",dataValues[9] + "hPa",dataValues[10] + "%", dataValues[11]));
                    Toast.makeText(getActivity(), "No conneection",Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getActivity(), "Please enter valid city name...",Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

}