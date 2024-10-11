package org.example.alura;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class Moneda {
    @SerializedName("base") // Asegúrate de que el nombre coincide con el JSON que recibes
    private String baseCode; // Código de la moneda base

    @SerializedName("conversion_rates") // Asegúrate de que el nombre coincide con el JSON que recibes
    private Map<String, Double> conversionRates; // Tasas de conversión

    // Constructor sin parámetros (requerido por Gson)
    public Moneda() {
    }

    // Constructor con parámetros
    public Moneda(String baseCode, Map<String, Double> conversionRates) {
        this.baseCode = baseCode;
        this.conversionRates = conversionRates;
    }

    // Getters y Setters
    public String getBaseCode() {
        return baseCode;
    }

    public void setBaseCode(String baseCode) {
        this.baseCode = baseCode;
    }

    public Map<String, Double> getConversionRates() {
        return conversionRates;
    }

    public void setConversionRates(Map<String, Double> conversionRates) {
        this.conversionRates = conversionRates;
    }

    // Método para obtener la tasa de conversión para una moneda específica
    public double getRate(String currency) {
        return conversionRates.getOrDefault(currency, 0.0); // Devuelve 0 si la moneda no se encuentra
    }

    // Método para obtener las tasas de conversión en formato legible
    public String getFilteredRates() {
        if (conversionRates == null || conversionRates.isEmpty()) {
            return "No hay tasas de conversión disponibles.";
        }
        StringBuilder rates = new StringBuilder("Tasas de conversión:\n");
        conversionRates.forEach((currency, rate) ->
                rates.append(String.format("%s: %.2f\n", currency, rate))
        );
        return rates.toString(); // Devuelve una cadena con las tasas de conversión
    }
}
