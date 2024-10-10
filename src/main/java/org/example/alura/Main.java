package org.example.alura;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ConsultaCambio consulta = new ConsultaCambio();

        System.out.println("Ingrese el tipo de moneda que desea consultar (por ejemplo, USD, EUR): ");
        String tipoDeMoneda = scanner.nextLine();

        System.out.println("Ingrese el valor de la moneda a cambiar: ");
        try {
            double valor = Double.parseDouble(scanner.nextLine());
            Moneda moneda = consulta.buscaMoneda(tipoDeMoneda);
            System.out.println("Tasa de cambio: " + moneda);
            GeneradorProceso generador = new GeneradorProceso();
            generador.guardarJson(moneda);
            System.out.println("El JSON de la moneda ha sido guardado exitosamente.");
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido. " + e.getMessage());
        } catch (RuntimeException | IOException e) {
            System.out.println(e.getMessage());
        } finally {
            scanner.close();
            System.out.println("Gracias por su consulta.");
        }
    }
}
