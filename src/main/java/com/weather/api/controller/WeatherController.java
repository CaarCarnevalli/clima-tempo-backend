package com.weather.api.controller;

import com.weather.api.model.LocationResponse;
import com.weather.api.model.WeatherResponse;
import com.weather.api.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class WeatherController {
    
    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);
    private final WeatherService weatherService;
    
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }
    
    @GetMapping("/weather")
    public ResponseEntity<WeatherResponse> getWeatherByCity(@RequestParam String city) {
        try {
            logger.info("📥 Requisição recebida - GET /api/weather?city={}", city);
            WeatherResponse weather = weatherService.getWeatherByCity(city);
            logger.info("📤 Resposta enviada com sucesso para cidade: {}", city);
            return ResponseEntity.ok(weather);
        } catch (Exception e) {
            logger.error("❌ Erro ao processar requisição para cidade '{}': {}", city, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/weather/coordinates")
    public ResponseEntity<WeatherResponse> getWeatherByCoordinates(
            @RequestParam double lat, 
            @RequestParam double lon) {
        try {
            logger.info("📥 Requisição recebida - GET /api/weather/coordinates?lat={}&lon={}", lat, lon);
            WeatherResponse weather = weatherService.getWeatherByCoordinates(lat, lon);
            logger.info("📤 Resposta enviada com sucesso para coordenadas: lat={}, lon={}", lat, lon);
            return ResponseEntity.ok(weather);
        } catch (Exception e) {
            logger.error("❌ Erro ao processar requisição para coordenadas lat={}, lon={}: {}", 
                    lat, lon, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/locations/search")
    public ResponseEntity<List<LocationResponse>> searchLocations(@RequestParam String query) {
        try {
            logger.info("📥 Requisição recebida - GET /api/locations/search?query={}", query);
            List<LocationResponse> locations = weatherService.searchLocations(query);
            logger.info("📤 Resposta enviada com {} localização(ões) para query: {}", 
                    locations.size(), query);
            return ResponseEntity.ok(locations);
        } catch (Exception e) {
            logger.error("❌ Erro ao processar busca de localizações para '{}': {}", 
                    query, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
