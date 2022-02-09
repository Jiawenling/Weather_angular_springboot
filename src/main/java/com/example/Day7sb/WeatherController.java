package com.example.Day7sb;

import jakarta.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@CrossOrigin
public class WeatherController {

    @Autowired
    WeatherService weatherSvc;

    @GetMapping(path="/api/weather/{city}")
    public ResponseEntity<String> getWeatherData(@PathVariable String city) throws IOException {

        try {
            Optional<JsonObject> weatherObj = weatherSvc.getWeather(city);
            if (weatherObj.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No results found");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(weatherObj.get().toString());
            }
        } catch(IOException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Server side error");
        }
    }
}
