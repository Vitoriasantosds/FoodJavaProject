package br.edu.ifpb.ads.foodjava.repository;

import br.edu.ifpb.ads.foodjava.model.*;
import br.edu.ifpb.ads.foodjava.util.Caminhos;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoRepository {
    private final Gson gson = new GsonBuilder().setPrettyPrinting()
            .registerTypeAdapter(Categoria.class, (JsonDeserializer<Categoria>) (json, type, ctx) -> {
                try { return Categoria.valueOf(json.getAsString()); } catch (Exception e) { return null; }
            })
            .registerTypeAdapter(StatusPedido.class, (JsonDeserializer<StatusPedido>) (json, type, ctx) -> {
                try { return StatusPedido.valueOf(json.getAsString()); } catch (Exception e) { return null; }
            }).create();
    private final Type listType = new TypeToken<List<Pedido>>(){}.getType();

    public List<Pedido> listarTodos() {
        if (!Files.exists(Paths.get(Caminhos.PEDIDOS))) return new ArrayList<>();
        try (Reader r = new FileReader(Caminhos.PEDIDOS)) {
            List<Pedido> lista = gson.fromJson(r, listType);
            return lista != null ? lista : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public void salvarTodos(List<Pedido> pedidos) {
        try {
            Files.createDirectories(Paths.get(Caminhos.DATA_DIR));
            try (Writer w = new FileWriter(Caminhos.PEDIDOS)) {
                gson.toJson(pedidos, w);
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar pedidos.");
        }
    }

    public void adicionar(Pedido p) {
        List<Pedido> lista = listarTodos();
        lista.add(p);
        salvarTodos(lista);
    }

    public void atualizar(Pedido atualizado) {
        List<Pedido> lista = listarTodos();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId().equals(atualizado.getId())) {
                lista.set(i, atualizado);
                break;
            }
        }
        salvarTodos(lista);
    }

    public List<Pedido> listarPorCliente(String emailCliente) {
        return listarTodos().stream()
                .filter(p -> p.getEmailCliente().equalsIgnoreCase(emailCliente))
                .toList();
    }

    public Pedido buscarPorId(String id) {
        return listarTodos().stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }
}
