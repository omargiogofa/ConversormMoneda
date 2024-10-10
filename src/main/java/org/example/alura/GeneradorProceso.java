package org.example.alura;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;

public class GeneradorProceso {
    public void guardarJson(Moneda moneda) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter escritura = new FileWriter(moneda.getBaseCode() + ".json")) {
            escritura.write(gson.toJson(moneda));
        }
    }
}
