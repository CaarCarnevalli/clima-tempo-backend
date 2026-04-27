package com.weather.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.weather.api.model.LocationResponse;
import com.weather.api.model.WeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {
    
    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);
    
    private final WebClient webClient;
    
    @Value("${weather.api.key}")
    private String apiKey;
    
    @Value("${weather.api.base-url}")
    private String weatherBaseUrl;
    
    @Value("${geocoding.api.base-url}")
    private String geocodingBaseUrl;
    
    public WeatherService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }
    
    public WeatherResponse getWeatherByCity(String city) {
        try {
            logger.info("🌍 Buscando clima para a cidade: {}", city);
            
            String url = String.format("%s/weather?q=%s&appid=%s&units=metric&lang=pt_br", 
                    weatherBaseUrl, city, apiKey);
            
            logger.debug("URL da API: {}", url.replace(apiKey, "***"));
            
            JsonNode response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
            
            WeatherResponse weatherResponse = parseWeatherResponse(response);
            logger.info("✅ Clima obtido com sucesso: {} ({}) - {}°C", 
                    weatherResponse.getCity(), 
                    weatherResponse.getCountry(), 
                    weatherResponse.getTemperature());
            
            return weatherResponse;
        } catch (Exception e) {
            logger.error("❌ Erro ao buscar dados do clima para '{}': {}", city, e.getMessage());
            throw new RuntimeException("Erro ao buscar dados do clima: " + e.getMessage());
        }
    }
    
    public WeatherResponse getWeatherByCoordinates(double lat, double lon) {
        try {
            logger.info("🌍 Buscando clima para coordenadas: lat={}, lon={}", lat, lon);
            
            String url = String.format("%s/weather?lat=%f&lon=%f&appid=%s&units=metric&lang=pt_br", 
                    weatherBaseUrl, lat, lon, apiKey);
            
            logger.debug("URL da API: {}", url.replace(apiKey, "***"));
            
            JsonNode response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
            
            WeatherResponse weatherResponse = parseWeatherResponse(response);
            logger.info("✅ Clima obtido com sucesso: {} ({}) - {}°C", 
                    weatherResponse.getCity(), 
                    weatherResponse.getCountry(), 
                    weatherResponse.getTemperature());
            
            return weatherResponse;
        } catch (Exception e) {
            logger.error("❌ Erro ao buscar dados do clima para coordenadas lat={}, lon={}: {}", 
                    lat, lon, e.getMessage());
            throw new RuntimeException("Erro ao buscar dados do clima: " + e.getMessage());
        }
    }
    
    public List<LocationResponse> searchLocations(String query) {
        try {
            logger.info("🔍 Buscando localizações para: '{}'", query);
            
            String url = String.format("%s/direct?q=%s&limit=5&appid=%s", 
                    geocodingBaseUrl, query, apiKey);
            
            logger.debug("URL da API: {}", url.replace(apiKey, "***"));
            
            JsonNode response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
            
            List<LocationResponse> locations = new ArrayList<>();
            if (response != null && response.isArray()) {
                for (JsonNode node : response) {
                    LocationResponse location = new LocationResponse();
                    location.setName(node.get("name").asText());
                    location.setCountry(node.get("country").asText());
                    location.setState(node.has("state") ? node.get("state").asText() : "");
                    location.setLat(node.get("lat").asDouble());
                    location.setLon(node.get("lon").asDouble());
                    locations.add(location);
                }
                logger.info("✅ Encontradas {} localização(ões) para '{}'", locations.size(), query);
            } else {
                logger.warn("⚠️ Nenhuma localização encontrada para '{}'", query);
            }
            
            return locations;
        } catch (Exception e) {
            logger.error("❌ Erro ao buscar localizações para '{}': {}", query, e.getMessage());
            throw new RuntimeException("Erro ao buscar localizações: " + e.getMessage());
        }
    }
    
    private WeatherResponse parseWeatherResponse(JsonNode node) {
        WeatherResponse weather = new WeatherResponse();
        
        weather.setCity(node.get("name").asText());
        weather.setCountry(node.get("sys").get("country").asText());
        weather.setTemperature(node.get("main").get("temp").asDouble());
        weather.setFeelsLike(node.get("main").get("feels_like").asDouble());
        weather.setHumidity(node.get("main").get("humidity").asInt());
        weather.setWindSpeed(node.get("wind").get("speed").asDouble());
        weather.setDescription(node.get("weather").get(0).get("description").asText());
        weather.setIcon(node.get("weather").get(0).get("icon").asText());
        weather.setTimestamp(node.get("dt").asLong());
        
        WeatherResponse.Coordinates coordinates = new WeatherResponse.Coordinates();
        coordinates.setLat(node.get("coord").get("lat").asDouble());
        coordinates.setLon(node.get("coord").get("lon").asDouble());
        weather.setCoordinates(coordinates);
        
        return weather;
    }
}
