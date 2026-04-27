package com.weather.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class WeatherApiApplication {

    @Autowired
    private Environment env;

    public static void main(String[] args) {
        SpringApplication.run(WeatherApiApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        String port = env.getProperty("server.port", "8080");
        
        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                            ║");
        System.out.println("║   🌤️  WEATHER API - BACKEND INICIADO COM SUCESSO!         ║");
        System.out.println("║                                                            ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println("║                                                            ║");
        System.out.println("║   🚀 Servidor rodando em: http://localhost:" + port + "            ║");
        System.out.println("║   📡 API endpoint: http://localhost:" + port + "/api/weather      ║");
        System.out.println("║                                                            ║");
        System.out.println("║   ✅ Pronto para receber requisições!                      ║");
        System.out.println("║                                                            ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println("\n");
    }
}
