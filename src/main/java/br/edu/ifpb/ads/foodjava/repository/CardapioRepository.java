package br.edu.ifpb.ads.foodjava.repository;

import br.edu.ifpb.ads.foodjava.model.Categoria;
import br.edu.ifpb.ads.foodjava.model.ItemCardapio;
import br.edu.ifpb.ads.foodjava.util.Caminhos;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class CardapioRepository {
    private final Gson gson = new GsonBuilder().setPrettyPrinting()
            .registerTypeAdapter(Categoria.class, (JsonDeserializer<Categoria>) (json, type, ctx) -> {
                try { return Categoria.valueOf(json.getAsString()); }
                catch (Exception e) { return null; }
            }).create();
    private final Type listType = new TypeToken<List<ItemCardapio>>(){}.getType();

    public List<ItemCardapio> listarTodos() {
        if (!Files.exists(Paths.get(Caminhos.CARDAPIO))) return new ArrayList<>();
        try (Reader r = new FileReader(Caminhos.CARDAPIO)) {
            List<ItemCardapio> lista = gson.fromJson(r, listType);
            return lista != null ? lista : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public void salvarTodos(List<ItemCardapio> itens) {
        try {
            Files.createDirectories(Paths.get(Caminhos.DATA_DIR));
            try (Writer w = new FileWriter(Caminhos.CARDAPIO)) {
                gson.toJson(itens, w);
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar cardápio.");
        }
    }

    public void adicionar(ItemCardapio item) {
        List<ItemCardapio> lista = listarTodos();
        lista.add(item);
        salvarTodos(lista);
    }

    public void atualizar(ItemCardapio atualizado) {
        List<ItemCardapio> lista = listarTodos();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId().equals(atualizado.getId())) {
                lista.set(i, atualizado);
                break;
            }
        }
        salvarTodos(lista);
    }

    public void remover(String id) {
        List<ItemCardapio> lista = listarTodos();
        lista.removeIf(i -> i.getId().equals(id));
        salvarTodos(lista);
    }

    public ItemCardapio buscarPorId(String id) {
        return listarTodos().stream().filter(i -> i.getId().equals(id)).findFirst().orElse(null);
    }
}
