package br.edu.ifpb.ads.foodjava.repository;

import br.edu.ifpb.ads.foodjava.model.Restaurante;
import br.edu.ifpb.ads.foodjava.util.Caminhos;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.*;

public class RestauranteRepository {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public boolean restauranteConfigurado() {
        return Files.exists(Paths.get(Caminhos.RESTAURANTE));
    }

    public void salvar(Restaurante restaurante) {
        try {
            Files.createDirectories(Paths.get(Caminhos.DATA_DIR));
            try (Writer w = new FileWriter(Caminhos.RESTAURANTE)) {
                gson.toJson(restaurante, w);
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar restaurante: " + e.getMessage());
        }
    }

    public Restaurante carregar() {
        try (Reader r = new FileReader(Caminhos.RESTAURANTE)) {
            return gson.fromJson(r, Restaurante.class);
        } catch (IOException e) {
            return null;
        }
    }
}
