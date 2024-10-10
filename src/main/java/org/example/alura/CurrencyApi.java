package org.example.alura;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import static spark.Spark.*;

public class CurrencyApi { // Renombrar la clase a CurrencyApi
    private static final Map<String, Double> conversionRates = new HashMap<>();

    public static void main(String[] args) {
        // Definir tasas de cambio (esto debería venir de una base de datos o un servicio real)
        initializeConversionRates();

        // Configurar el puerto del servidor
        port(8080); // Configura el servidor para escuchar en el puerto 8080

        // Iniciar el servidor
        get("/api/currency-exchange", (req, res) -> {
            String baseCurrency = req.queryParams("base");
            res.type("application/json");

            // Devolver tasas de cambio solo si la moneda base es válida
            if (conversionRates.containsKey(baseCurrency)) {
                Moneda moneda = new Moneda(baseCurrency, conversionRates);
                return new Gson().toJson(moneda);
            } else {
                res.status(400);
                return "{\"error\":\"Moneda base no válida\"}";
            }
        });
    }

    private static void initializeConversionRates() {
        conversionRates.put("USD", 1.0);
        conversionRates.put("ARS", 364.0); // Ejemplo de tasa de cambio
        conversionRates.put("BOB", 6.96);
        conversionRates.put("BRL", 5.25);
        conversionRates.put("CLP", 845.0);
        conversionRates.put("COP", 4500.0);
        // Agrega más tasas según sea necesario
    }
}
