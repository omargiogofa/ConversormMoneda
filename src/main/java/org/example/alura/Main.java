package org.example.alura;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ConsultaCambio consulta = new ConsultaCambio(); // Crear instancia de ConsultaCambio

        System.out.println("Ingrese el tipo de moneda que desea consultar:");
        String tipoDeMoneda = scanner.nextLine().toUpperCase(); // Convertir a mayúsculas para la API.

        System.out.println("Ingrese el valor de la moneda a cambiar: ");
        try {
            double valor = Double.parseDouble(scanner.nextLine()); // Obtener valor de entrada
            Moneda moneda = consulta.buscaMoneda(tipoDeMoneda); // Buscar moneda base

            // Verificar si la moneda se encontró
            if (moneda != null && moneda.getConversionRates() != null) {
                procesarConversion(scanner, valor, tipoDeMoneda, moneda);
                guardarJson(moneda); // Guardar JSON de la moneda
            } else {
                System.out.println("No se encontró información para la moneda " + tipoDeMoneda);
            }
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido. Debe ser un número. " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error al guardar el JSON: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Error al procesar la solicitud: " + e.getMessage());
        } finally {
            scanner.close(); // Cerrar el escáner
            System.out.println("Gracias por su consulta.");
        }
    }

    private static void procesarConversion(Scanner scanner, double valor, String tipoDeMoneda, Moneda moneda) {
        System.out.println("Tasa de cambio para " + tipoDeMoneda + ": " + moneda.getFilteredRates());
        System.out.println("Ingrese la moneda a la que desea convertir:");
        String monedaDestino = scanner.nextLine().toUpperCase();
        double tasaCambio = moneda.getRate(monedaDestino);

        if (tasaCambio > 0) {
            double resultado = valor * tasaCambio; // Realizar la conversión
            System.out.printf("El valor convertido de %.2f %s a %s es: %.2f%n", valor, tipoDeMoneda, monedaDestino, resultado);
        } else {
            System.out.println("No se encontró la tasa de cambio para " + monedaDestino);
        }
    }

    private static void guardarJson(Moneda moneda) throws IOException {
        GeneradorProceso generador = new GeneradorProceso();
        generador.guardarJson(moneda); // Guardar el objeto moneda como JSON
        System.out.println("El JSON de la moneda ha sido guardado exitosamente.");
    }
}
