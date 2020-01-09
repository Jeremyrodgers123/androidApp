package com.main.project1;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import java.util.HashMap;


public class WeatherDataFragment extends Fragment implements View.OnClickListener{

    private TextView mHumidityTv;
    private TextView mWindTv;
    private TextInputEditText mCityTe;
    private TextInputEditText mCountryTe;
    private MaterialButton mSubmitBtn;
    private MaterialTextView mCurrentTempTv;
    private MaterialTextView mCurrentWeatherLocTv;
    private HashMap<String, String> countryToCountryCode = new HashMap<>();
    private AppViewModel mAppViewModel;

    public WeatherDataFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Create the view model
        mAppViewModel = new ViewModelProvider(this).get(AppViewModel.class);

        //Set the observer
        mAppViewModel.getWeatherData().observe(this, weatherObserver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        countryToCountryCode.put("canada", "CA");
        countryToCountryCode.put("united states of america", "US");
        countryToCountryCode.put("united states", "US");
        countryToCountryCode.put("usa", "US");

        View view =  inflater.inflate(R.layout.fragment_weather_data, container, false);
        mHumidityTv = view.findViewById(R.id.humidity_tv);
        mWindTv = view.findViewById(R.id.wind_tv);
        mCityTe = view.findViewById(R.id.weather_city_text_edit);
        mCountryTe = view.findViewById(R.id.weather_country_text_edit);
        mSubmitBtn = view.findViewById(R.id.submit_weather_data_btn);
        mCurrentTempTv = view.findViewById(R.id.current_temp);
        mCurrentWeatherLocTv = view.findViewById(R.id.current_weather_location_tv);
        mSubmitBtn.setOnClickListener(this);

        //get weather from user profile
        UserProfile userProfile = mAppViewModel.getUserProfile().getValue();
        if (userProfile != null) {
            String city = userProfile.getCity().toLowerCase();
            String countryCode = getCountryCode(userProfile.getCountry());
            queryWeatherData(city, countryCode);
        }

        return view;

    }


    void queryWeatherData(String city, String countryCode) {
        if(city == null || countryCode == null) {
            Toast.makeText(getContext(),"Invalid city or country",Toast.LENGTH_SHORT).show();
            return;
        }
        mAppViewModel.setLocation(city, countryCode);
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.submit_weather_data_btn:
                String city = mCityTe.getText().toString().toLowerCase();
                String countryCode = getCountryCode(mCountryTe.getText().toString());
                queryWeatherData(city, countryCode);
                break;
        }
    }

    String getCountryCode(String country){
        return countryToCountryCode.get(country.toLowerCase());
    }

    final Observer<WeatherData> weatherObserver  = new Observer<WeatherData>() {

        @Override
        public void onChanged(@Nullable final WeatherData weatherData) {
            if(weatherData != null) {
                mWindTv.setText(String.format("%.1f", weatherData.getWind().getSpeed()));
                mHumidityTv.setText(String.format("%.0f", weatherData.getCurrentCondition().getHumidity()));
                mCurrentTempTv.setText(String.format("%.1f", weatherData.getTemperature().getTemp()) + " °F");
                mCurrentWeatherLocTv.setText(weatherData.getLocationData().getCity() + ", " + weatherData.getLocationData().getCountry());
            }
            else {
                Toast.makeText(getContext(),"An error occurred retrieving weather data",Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("CITY", mCityTe.getText().toString());
        savedInstanceState.putString("COUNTRY", mCountryTe.getText().toString());
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            String city = savedInstanceState.getString("CITY");
            String country = savedInstanceState.getString("COUNTRY");
            mCityTe.setText(city);
            mCountryTe.setText(country);
        }
        super.onViewStateRestored(savedInstanceState);
    }
}