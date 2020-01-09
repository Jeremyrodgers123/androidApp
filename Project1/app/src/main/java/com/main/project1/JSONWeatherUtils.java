package com.main.project1;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONWeatherUtils {
    public static WeatherData getWeatherData(JSONObject jsonObject) throws JSONException {
        WeatherData weatherData = new WeatherData();
        LocationData locationData = new LocationData();

        WeatherData.CurrentCondition currentCondition = weatherData.getCurrentCondition();
        JSONObject jsonMain = jsonObject.getJSONObject("main");
        currentCondition.setHumidity(jsonMain.getInt("humidity"));
        JSONObject weather = (JSONObject) jsonObject.getJSONArray("weather").get(0);
        currentCondition.setCondition(weather.getString("description"));
        weatherData.setCurrentCondition(currentCondition);

        //Get the temperature, wind and cloud data.
        WeatherData.Temperature temperature = weatherData.getTemperature();
        WeatherData.Wind wind = weatherData.getWind();
        wind.setSpeed(jsonObject.getJSONObject("wind").getDouble("speed"));
        weatherData.setWind(wind);
        WeatherData.Clouds clouds = weatherData.getClouds();
        temperature.setMaxTemp(jsonMain.getDouble("temp_max"));
        temperature.setMinTemp(jsonMain.getDouble("temp_min"));
        temperature.setTemp(jsonMain.getDouble("temp"));
        weatherData.setTemperature(temperature);

        JSONObject coord = jsonObject.getJSONObject("coord");
        locationData.setCity(jsonObject.getString("name"));
        locationData.setCountry(jsonObject.getJSONObject("sys").getString("country"));
        locationData.setLongitude(coord.getDouble("lon"));
        locationData.setLatitude(coord.getDouble("lat"));
        weatherData.setLocationData(locationData);

        return weatherData;
    }
}
