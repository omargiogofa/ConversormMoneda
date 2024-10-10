package org.example.alura;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class Swing extends JFrame {
    private Map<String, Double> rates; // Almacena las tasas de conversión
    private JComboBox<String> monedaOrigenCombo;
    private JComboBox<String> monedaDestinoCombo;
    private JTextField valorInput;
    private JLabel resultadoLabel;

    public Swing() {
        try {
            // Intenta obtener las tasas de cambio desde la API
            this.rates = obtenerTasasCambio();
            if (rates == null || rates.isEmpty()) {
                throw new IllegalArgumentException("Las tasas de cambio son nulas o están vacías.");
            }
            initUI(); // Inicializa la interfaz de usuario
        } catch (Exception e) {
            mostrarError("Error al obtener las tasas de cambio: " + e.getMessage());
        }
    }

    private void initUI() {
        setTitle("Conversor de Moneda");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2)); // 5 filas y 2 columnas

        // Crear componentes
        JLabel monedaOrigenLabel = new JLabel("Moneda Origen:");
        JLabel monedaDestinoLabel = new JLabel("Moneda Destino:");
        JLabel valorLabel = new JLabel("Valor a Convertir:");
        resultadoLabel = new JLabel("Resultado:");

        monedaOrigenCombo = new JComboBox<>(rates.keySet().toArray(new String[0]));
        monedaDestinoCombo = new JComboBox<>(rates.keySet().toArray(new String[0]));
        valorInput = new JTextField();

        JButton convertirBtn = new JButton("Convertir");
        convertirBtn.addActionListener(e -> convertirMoneda());

        // Añadir componentes al panel
        panel.add(monedaOrigenLabel);
        panel.add(monedaOrigenCombo);
        panel.add(monedaDestinoLabel);
        panel.add(monedaDestinoCombo);
        panel.add(valorLabel);
        panel.add(valorInput);
        panel.add(new JLabel()); // Espacio vacío
        panel.add(convertirBtn);
        panel.add(new JLabel("Resultado:"));
        panel.add(resultadoLabel);

        add(panel);
        setLocationRelativeTo(null);
    }

    private void convertirMoneda() {
        try {
            String monedaOrigen = (String) monedaOrigenCombo.getSelectedItem();
            String monedaDestino = (String) monedaDestinoCombo.getSelectedItem();
            double valor = Double.parseDouble(valorInput.getText());

            double tasaOrigen = rates.get(monedaOrigen);
            double tasaDestino = rates.get(monedaDestino);

            // Realizar la conversión
            double valorConvertido = (valor / tasaOrigen) * tasaDestino;
            resultadoLabel.setText(String.format("%.4f", valorConvertido));
        } catch (NumberFormatException e) {
            mostrarError("Por favor ingresa un valor numérico válido.");
        } catch (Exception e) {
            mostrarError("Error en la conversión: " + e.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private Map<String, Double> obtenerTasasCambio() throws Exception {
        String url = "https://v6.exchangerate-api.com/v6/878d671a848e06df0bd9a534/latest/USD"; // URL de la API
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();

        // Parsear el resultado como JSON
        JSONObject json = new JSONObject(response.toString());
        JSONObject conversionRates = json.getJSONObject("conversion_rates");

        // Almacenar las tasas de conversión en un mapa
        Map<String, Double> tasas = new HashMap<>();
        for (String key : conversionRates.keySet()) {
            tasas.put(key, conversionRates.getDouble(key));
        }

        return tasas;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Swing app = new Swing();
            app.setVisible(true);
        });
    }
}