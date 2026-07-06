package br.edu.ifpb.ads.foodjava.repository;

import br.edu.ifpb.ads.foodjava.model.Cliente;
import br.edu.ifpb.ads.foodjava.util.Caminhos;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepository {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Type listType = new TypeToken<List<Cliente>>(){}.getType();

    public List<Cliente> listarTodos() {
        if (!Files.exists(Paths.get(Caminhos.CLIENTES))) return new ArrayList<>();
        try (Reader r = new FileReader(Caminhos.CLIENTES)) {
            List<Cliente> lista = gson.fromJson(r, listType);
            return lista != null ? lista : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public void salvarTodos(List<Cliente> clientes) {
        try {
            Files.createDirectories(Paths.get(Caminhos.DATA_DIR));
            try (Writer w = new FileWriter(Caminhos.CLIENTES)) {
                gson.toJson(clientes, w);
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar clientes.");
        }
    }

    public void adicionar(Cliente c) {
        List<Cliente> lista = listarTodos();
        lista.add(c);
        salvarTodos(lista);
    }

    public Cliente buscarPorEmail(String email) {
        return listarTodos().stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email))
                .findFirst().orElse(null);
    }

    public boolean existeEmail(String email) {
        return buscarPorEmail(email) != null;
    }

    public boolean existeCpf(String cpf) {
        String limpo = cpf.replaceAll("[^0-9]", "");
        return listarTodos().stream()
                .anyMatch(c -> c.getCpf().replaceAll("[^0-9]", "").equals(limpo));
    }
}
