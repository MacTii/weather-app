package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout homeRL; // main
    private ProgressBar loadingPB; // main
    private ImageView backIV; // main

//    private TextView cityNameTV, temperatureTV, conditionTV;
//    private RecyclerView weatherRV;
//    private TextInputEditText cityEdt;
//    private ImageView iconIV, searchIV;

    private ArrayList<WeatherRVModal> weatherRVModalArrayList;
    private WeatherRVAdapter weatherRVAdapter;

    private LocationManager locationManager;
    private int PERMISSON_CODE = 1;
    private String cityName;

    private TabLayout tabLayout; // main
    private ViewPager2 viewPager2; // main

//    private static String cityFavorite;
//
//    String getCityFavorite() { return cityFavorite; }
//    void setCityFavorite(String cityFavorite) { this.cityFavorite = cityFavorite; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);

        homeRL = findViewById(R.id.idRLHome);
        loadingPB = findViewById(R.id.idPBLoading);
        backIV = findViewById(R.id.idIVBack);

//        cityNameTV = findViewById(R.id.idTVCityName);
//        temperatureTV = findViewById(R.id.idTVTemperature);
//        conditionTV = findViewById(R.id.idTVCondition);
//        weatherRV = findViewById(R.id.idRVWeather);
//        cityEdt = findViewById(R.id.idEdtCity);
//        iconIV = findViewById(R.id.idIVIcon);
//        searchIV = findViewById(R.id.idIVSearch);

//        weatherRVModalArrayList = new ArrayList<>();
//        weatherRVAdapter = new WeatherRVAdapter(this, weatherRVModalArrayList);
//        weatherRV.setAdapter(weatherRVAdapter);

//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSON_CODE);
//        }
//
//        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

//        if (location != null){
//            cityName = getCityName(location.getLongitude(),location.getLatitude());
//        } else {
//            cityName = "Lodz";
//        }
//        getWeatherInfo(cityName);

//        searchIV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String city = cityEdt.getText().toString();
//                if(city.isEmpty())
//                    Toast.makeText(MainActivity.this, "Please enter city name", Toast.LENGTH_SHORT).show();
//                else {
//                    cityNameTV.setText(cityName);
//                    getWeatherInfo(city);
//                }
//            }
//        });

        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager);
        viewPager2.setSaveEnabled(false);
        viewPager2.setAdapter(new ViewPagerAdapter(this));

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("CityInfo");
                    break;
                case 1:
                    tab.setText("MoreInfo");
                    break;
                default:
                    tab.setText("SavedCities");
                    break;
            }
        }).attach();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==PERMISSON_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissions granted..", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please provide the permissions...", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

//    private String getCityName(double longitude, double latitude) {
//        String citName = "Not found";
//        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
//        try {
//            List<Address> addresses = gcd.getFromLocation(latitude,longitude,10);
//            for (Address adr : addresses) {
//                if(adr!=null) {
//                    String city = adr.getLocality();
//                    if(city!=null && !city.equals("")) {
//                        citName = city;
//                    } else {
//                        Log.d("TAG","CITY NOT FOUND");
//                        Toast.makeText(this,"User city not found...",Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//        return citName;
//    }

//    private void getWeatherInfo(String cityName) {
//        String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + cityName + "&appid=c4943e245639be2b01bad7556a3b6510";
//        cityNameTV.setText(cityName);
//        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
//            @Override
//            public void onResponse(JSONObject response) {
//                loadingPB.setVisibility(View.GONE);
//                homeRL.setVisibility(View.VISIBLE);
//                weatherRVModalArrayList.clear();
//
//                try {
//
//                    String temperature = response.getJSONArray("list").getJSONObject(0).getJSONObject("main").getString("temp");
//                    float temperatureFloat = (float) (Float.parseFloat(temperature) - 273.15);
//                    DecimalFormat df = new DecimalFormat("##.#");
//                    // String temperatureFormatted = df.format(temperatureFloat);
//                    temperatureTV.setText(String.valueOf(df.format(temperatureFloat)) + "°C");
//
//                    String condition = response.getJSONArray("list").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("main");
//                    String conditionIcon = response.getJSONArray("list").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("icon");
//
//                    Picasso.get().load("https://openweathermap.org/img/wn/".concat(conditionIcon).concat("@2x.png")).into(iconIV);
//
//                    conditionTV.setText(condition);
//
//                    JSONArray forecastObj = response.getJSONArray("list");
//
//                    for (int i=1;i<9;i++) {
//                        JSONObject hourObj = forecastObj.getJSONObject(i);
//
//                        String time = hourObj.getString("dt_txt");
//
//                        String temper = hourObj.getJSONObject("main").getString("temp");
//                        temperatureFloat = (float) (Float.parseFloat(temper) - 273.15);
//                        temper = String.valueOf(df.format(temperatureFloat));
//
//                        String img = hourObj.getJSONArray("weather").getJSONObject(0).getString("icon");
//
//                        String wind = hourObj.getJSONObject("wind").getString("speed");
//
//                        weatherRVModalArrayList.add(new WeatherRVModal(time, temper, img, wind));
//                    }
//
//                    weatherRVAdapter.notifyDataSetChanged();
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                System.out.println(error);
//                Toast.makeText(MainActivity.this, "Please enter valid city name...",Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        requestQueue.add(jsonObjectRequest);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @SuppressLint({"NonConstantResourceId", "ResourceType"})
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}