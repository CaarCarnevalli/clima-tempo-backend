# Weather API - Backend

## Requisitos
- Java 17 ou superior
- Maven 3.6+

## Configuração

1. Obtenha uma API Key gratuita no [OpenWeatherMap](https://openweathermap.org/api)

2. Configure a API Key no arquivo `src/main/resources/application.properties`:
```properties
weather.api.key=SUA_API_KEY_AQUI
```

## Como Executar

### Via Maven
```bash
mvn spring-boot:run
```

### Via JAR
```bash
mvn clean package
java -jar target/weather-api-1.0.0.jar
```

## Endpoints

### Buscar clima por cidade
```
GET /api/weather?city=São Paulo
```

### Buscar clima por coordenadas
```
GET /api/weather/coordinates?lat=-23.5505&lon=-46.6333
```

### Buscar localizações
```
GET /api/locations/search?query=São Paulo
```

## Tecnologias
- Spring Boot 3.2.0
- Spring WebFlux (WebClient)
- Lombok
- Maven
