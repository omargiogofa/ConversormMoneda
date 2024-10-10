package org.example.alura;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConsultaCambio {
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/878d671a848e06df0bd9a534/latest"; // Cambia la clave por la tuya

    public Moneda buscaMoneda(String baseCurrency) {
        try {
            String jsonResponse = getApiResponse(baseCurrency);
            Moneda moneda = parseJsonToMoneda(jsonResponse);

            if (moneda == null || moneda.getConversionRates() == null || moneda.getConversionRates().isEmpty()) {
                throw new RuntimeException("Las tasas de cambio son nulas o están vacías.");
            }

            return moneda;
        } catch (IOException e) {
            throw new RuntimeException("Error al consultar la API: " + e.getMessage());
        }
    }

    private String getApiResponse(String baseCurrency) throws IOException {
        URL url = new URL(API_URL + "/" + baseCurrency);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Error en la conexión: " + conn.getResponseCode());
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();
        }
    }

    private Moneda parseJsonToMoneda(String jsonResponse) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonResponse, Moneda.class);
    }
}
