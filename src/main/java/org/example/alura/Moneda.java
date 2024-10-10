package org.example.alura;

import java.util.HashMap;
import java.util.Map;

public class Moneda {
    private String baseCode;
    private Map<String, Double> conversionRates;

    // Constructor
    public Moneda(String baseCode, Map<String, Double> conversionRates) {
        this.baseCode = baseCode;
        this.conversionRates = conversionRates;
    }

    // Getters
    public String getBaseCode() {
        return baseCode;
    }

    public Map<String, Double> getConversionRates() {
        return conversionRates;
    }

    // Método para filtrar tasas de conversión por códigos específicos
    public Map<String, Double> getFilteredRates() {
        String[] validCodes = {"ARS", "BOB", "BRL", "CLP", "COP", "USD"};
        Map<String, Double> filteredRates = new HashMap<>();

        for (String code : validCodes) {
            if (conversionRates.containsKey(code)) {
                filteredRates.put(code, conversionRates.get(code));
            }
        }
        return filteredRates;
    }

    // Método toString
    @Override
    public String toString() {
        return "Moneda{" +
                "baseCode='" + baseCode + '\'' +
                ", conversionRates=" + conversionRates +
                '}';
    }
}
