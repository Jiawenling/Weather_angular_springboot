package com.example.Day7sb;

import jakarta.json.*;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
public class WeatherService {

    private final String appId ;
    String ENV_OPENWEATHERMAP_KEY = "APIKEY";

    public WeatherService(){
        String id = System.getenv(ENV_OPENWEATHERMAP_KEY);
        if(id!=null & (id.trim().length()>0)){
            appId = id;
        } else {
            appId = "noID";
        }
    }

    public Optional<JsonObject> getWeather(String city) throws IOException {
        RestTemplate template = new RestTemplate();
        String url = UriComponentsBuilder
                .fromUriString("http://api.openweathermap.org/data/2.5/weather")
                .queryParam("q", city)
                .queryParam("appid", appId)
                .queryParam("units","metric")
                .toUriString();
        RequestEntity<Void> req = RequestEntity.get(url).build();
        ResponseEntity<String> resp =  template.exchange(req, String.class);

        try(InputStream is = new ByteArrayInputStream(resp.getBody().getBytes())){
            JsonReader reader = Json.createReader(is);
            JsonObject data = reader.readObject();
            JsonArray weather = data.getJsonArray("weather");
            float temp = (float)data.getJsonObject("main").getJsonNumber("temp").doubleValue();
            String cityname = data.getString("name");

            JsonObject weatherObj = Json.createObjectBuilder()
                    .add("cityname",cityname)
                    .add("temp", temp)
                    .add("weather",weather)
                    .build();
            return Optional.of(weatherObj);
    }
        catch (IOException e){
            System.out.println("City info not found");

            return Optional.empty();
        }

}}
