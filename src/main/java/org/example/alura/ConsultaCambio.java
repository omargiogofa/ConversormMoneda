package org.example.alura;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ConsultaCambio {
    private static final String API_KEY = "878d671a848e06df0bd9a534"; // La clave API
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest"; // URL completa con clave API

    public Moneda buscaMoneda(String baseCurrency) {
        try {
            String jsonResponse = getApiResponse(baseCurrency); // Cambia aquí para usar la moneda base
            Moneda moneda = parseJsonToMoneda(jsonResponse);

            if (moneda == null || moneda.getConversionRates() == null || moneda.getConversionRates().isEmpty()) {
                throw new RuntimeException("Las tasas de cambio son nulas o están vacías.");
            }

            return moneda;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error al consultar la API: " + e.getMessage());
        }
    }

    private String getApiResponse(String baseCurrency) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/" + baseCurrency)) // Cambia la URL para que use el tipo de moneda base correcto
                .GET()
                .build();

        // Manejo de la respuesta
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Manejo de errores de conexión
        if (response.statusCode() != 200) {
            throw new RuntimeException("Error en la conexión: " + response.statusCode());
        }

        return response.body();
    }

    private Moneda parseJsonToMoneda(String jsonResponse) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonResponse, Moneda.class);
    }
}
