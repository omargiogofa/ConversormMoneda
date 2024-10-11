package org.example.alura;

import com.google.gson.Gson;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class CurrencyApi {
    private static final Map<String, Double> conversionRates = new HashMap<>();

    public static void main(String[] args) {
        // Obtener tasas de cambio de la API
        try {
            initializeConversionRates();
        } catch (Exception e) {
            System.err.println("Error al inicializar tasas de conversión: " + e.getMessage());
            System.exit(1); // Salir si no se pueden obtener tasas
        }

        // Configurar el puerto del servidor
        port(8080); // Configura el servidor para escuchar en el puerto 8080

        // Iniciar el servidor http://localhost:8080/api/currency-exchange?base=COP
        get("/api/currency-exchange", (req, res) -> {
            String baseCurrency = req.queryParams("base");
            res.type("application/json");

            // Devolver tasas de cambio solo si la moneda base es válida
            if (baseCurrency != null && conversionRates.containsKey(baseCurrency)) {
                Moneda moneda = new Moneda(baseCurrency, conversionRates);
                return new Gson().toJson(moneda);
            } else {
                res.status(400);
                return "{\"error\":\"Moneda base no válida\"}";
            }
        });

        // Nueva ruta para la conversión de moneda
        get("/api/currency-conversion", (req, res) -> {
            String fromCurrency = req.queryParams("from");
            String toCurrency = req.queryParams("to");
            double amount = Double.parseDouble(req.queryParams("amount")); // Obtener cantidad a convertir

            res.type("application/json");

            if (conversionRates.containsKey(fromCurrency) && conversionRates.containsKey(toCurrency)) {
                double convertedAmount = convertCurrency(fromCurrency, toCurrency, amount);
                return "{\"convertedAmount\":" + convertedAmount + "}";
            } else {
                res.status(400);
                return "{\"error\":\"Monedas no válidas\"}";
            }
        });
    }

    private static void initializeConversionRates() throws Exception {
        String apiKey = "878d671a848e06df0bd9a534"; // Tu clave API
        String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/COP"; // URL para obtener tasas de cambio base

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        // Manejar la respuesta de la API
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            // Parsear el resultado como JSON
            JSONObject json = new JSONObject(response.body());

            // Comprobar si el objeto tiene conversion_rates
            if (!json.has("conversion_rates")) {
                throw new Exception("No se encontraron tasas de conversión en la respuesta.");
            }

            JSONObject conversionRatesJson = json.getJSONObject("conversion_rates");

            // Almacenar las tasas de conversión en el mapa
            conversionRates.clear(); // Limpiar tasas previas antes de cargar nuevas
            for (String key : conversionRatesJson.keySet()) {
                conversionRates.put(key, conversionRatesJson.getDouble(key));
            }
        } else {
            throw new Exception("Error en la solicitud: " + response.statusCode());
        }
    }

    private static double convertCurrency(String fromCurrency, String toCurrency, double amount) {
        // Obtener la tasa de conversión correspondiente
        double fromRate = conversionRates.get(fromCurrency);
        double toRate = conversionRates.get(toCurrency);

        // Convertir la cantidad
        return (amount / fromRate) * toRate; // Fórmula para convertir entre monedas
    }
}
