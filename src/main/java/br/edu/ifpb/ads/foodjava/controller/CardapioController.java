package br.edu.ifpb.ads.foodjava.controller;

import br.edu.ifpb.ads.foodjava.exception.*;
import br.edu.ifpb.ads.foodjava.model.*;
import br.edu.ifpb.ads.foodjava.repository.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.*;

public class CardapioController {
    private final CardapioRepository cardapioRepo = new CardapioRepository();
    private final PedidoRepository pedidoRepo     = new PedidoRepository();

    public void adicionarItem(String nome, String descricao, double preco,
                               Categoria categoria, boolean disponivel, String imagemPath)
            throws PrecoInvalidoException {
        if (preco <= 0) throw new PrecoInvalidoException();
        ItemCardapio item = new ItemCardapio(nome, descricao, preco, categoria, disponivel, imagemPath);
        cardapioRepo.adicionar(item);
    }

    public void editarItem(ItemCardapio item) throws PrecoInvalidoException {
        if (item.getPreco() <= 0) throw new PrecoInvalidoException();
        cardapioRepo.atualizar(item);
    }

    public void removerItem(String id) throws ItemVinculadoException {
        boolean vinculado = pedidoRepo.listarTodos().stream()
                .filter(p -> p.getStatus() != StatusPedido.ENTREGUE && p.getStatus() != StatusPedido.CANCELADO)
                .flatMap(p -> p.getItens().stream())
                .anyMatch(ip -> ip.getItem().getId().equals(id));
        if (vinculado) throw new ItemVinculadoException();
        cardapioRepo.remover(id);
    }

    public List<ItemCardapio> listarTodos() {
        return cardapioRepo.listarTodos();
    }

    public List<ItemCardapio> listarDisponiveis() {
        return cardapioRepo.listarTodos().stream().filter(ItemCardapio::isDisponivel).toList();
    }

    /** Importa cardápio de um arquivo JSON e retorna lista de erros encontrados. */
    public List<String> importarJson(File arquivo) throws ArquivoImportacaoException {
        if (arquivo == null || !arquivo.exists())
            throw new ArquivoImportacaoException("Arquivo não encontrado.");

        List<String> erros = new ArrayList<>();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Categoria.class, (JsonDeserializer<Categoria>) (json, type, ctx) -> {
                    try { return Categoria.valueOf(json.getAsString()); }
                    catch (Exception e) { return null; }
                }).create();

        try (Reader r = new FileReader(arquivo)) {
            JsonObject root = gson.fromJson(r, JsonObject.class);
            if (root == null || !root.has("cardapio"))
                throw new ArquivoImportacaoException("Estrutura inválida: campo 'cardapio' ausente.");

            JsonArray array = root.getAsJsonArray("cardapio");
            int linha = 0;
            for (JsonElement el : array) {
                linha++;
                try {
                    JsonObject obj = el.getAsJsonObject();
                    String nome       = obj.has("nome")      ? obj.get("nome").getAsString()      : null;
                    String descricao  = obj.has("descricao") ? obj.get("descricao").getAsString() : "";
                    double preco      = obj.has("preco")     ? obj.get("preco").getAsDouble()     : 0;
                    String catStr     = obj.has("categoria") ? obj.get("categoria").getAsString() : null;
                    boolean disponivel= !obj.has("disponivel") || obj.get("disponivel").getAsBoolean();
                    String imagemPath = obj.has("imagemPath") ? obj.get("imagemPath").getAsString() : null;

                    if (nome == null || nome.isBlank())
                        throw new ArquivoImportacaoException("nome ausente.");
                    if (preco <= 0)
                        throw new PrecoInvalidoException();

                    Categoria categoria;
                    try { categoria = Categoria.valueOf(catStr); }
                    catch (Exception e) { throw new ArquivoImportacaoException("categoria inválida: " + catStr); }

                    cardapioRepo.adicionar(new ItemCardapio(nome, descricao, preco, categoria, disponivel, imagemPath));
                } catch (Exception e) {
                    erros.add("Linha " + linha + ": " + e.getMessage());
                }
            }
        } catch (ArquivoImportacaoException e) {
            throw e;
        } catch (Exception e) {
            throw new ArquivoImportacaoException("Erro ao ler arquivo: " + e.getMessage());
        }
        return erros;
    }
}
