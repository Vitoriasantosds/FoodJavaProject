package br.edu.ifpb.ads.foodjava.view.fxml;

import br.edu.ifpb.ads.foodjava.Main;
import br.edu.ifpb.ads.foodjava.controller.CardapioController;
import br.edu.ifpb.ads.foodjava.controller.PedidoController;
import br.edu.ifpb.ads.foodjava.model.Categoria;
import br.edu.ifpb.ads.foodjava.model.Cliente;
import br.edu.ifpb.ads.foodjava.model.ItemCardapio;
import br.edu.ifpb.ads.foodjava.util.Sessao;
import br.edu.ifpb.ads.foodjava.util.UI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.*;

public class CardapioClienteController implements Initializable {

    @FXML private Label lblNomeRestaurante;
    @FXML private Label lblNomeCliente;
    @FXML private VBox  boxCardapio;
    @FXML private VBox  boxCarrinho;
    @FXML private Label lblTotal;

    private final CardapioController cardapioCtrl = new CardapioController();
    private final PedidoController   pedidoCtrl   = new PedidoController();

    // carrinho: item -> quantidade
    private final Map<ItemCardapio, Integer> carrinho = new LinkedHashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Cliente cliente = (Cliente) Sessao.get().getUsuarioLogado();
        lblNomeCliente.setText("Olá, " + cliente.getNome());
        carregarCardapio();
    }

    // ------------------------------------------------------------------ cardápio

    private void carregarCardapio() {
        boxCardapio.getChildren().clear();
        List<ItemCardapio> itens = cardapioCtrl.listarDisponiveis();

        for (Categoria cat : Categoria.values()) {
            List<ItemCardapio> porCat = itens.stream()
                    .filter(i -> i.getCategoria() == cat).toList();
            if (porCat.isEmpty()) continue;

            Label titulo = new Label("── " + cat.getDescricao() + " ──");
            titulo.getStyleClass().add("titulo-secao");
            boxCardapio.getChildren().add(titulo);

            for (ItemCardapio item : porCat) {
                boxCardapio.getChildren().add(criarCardItem(item));
            }
        }

        if (itens.isEmpty()) {
            boxCardapio.getChildren().add(new Label("Nenhum item disponível no momento."));
        }
    }

    private HBox criarCardItem(ItemCardapio item) {
        HBox card = new HBox(12);
        card.getStyleClass().add("card");
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(10));

        VBox info = new VBox(4);
        HBox.setHgrow(info, Priority.ALWAYS);

        Label nome = new Label(item.getNome());
        nome.getStyleClass().add("label-nome-item");

        Label desc = new Label(item.getDescricao() == null ? "" : item.getDescricao());
        desc.setWrapText(true);
        desc.getStyleClass().add("subtitulo");

        Label preco = new Label("R$ " + String.format("%.2f", item.getPreco()));
        preco.getStyleClass().add("label-preco");

        info.getChildren().addAll(nome, desc, preco);

        Button btnAdd = new Button("+ Adicionar");
        btnAdd.getStyleClass().add("btn-success");
        btnAdd.setOnAction(e -> {
            carrinho.merge(item, 1, Integer::sum);
            atualizarCarrinho();
        });

        card.getChildren().addAll(info, btnAdd);
        return card;
    }

    // ------------------------------------------------------------------ carrinho

    private void atualizarCarrinho() {
        boxCarrinho.getChildren().clear();
        double total = 0;

        for (Map.Entry<ItemCardapio, Integer> entry : new LinkedHashMap<>(carrinho).entrySet()) {
            ItemCardapio item = entry.getKey();
            int qtd = entry.getValue();

            HBox linha = new HBox(6);
            linha.setAlignment(Pos.CENTER_LEFT);

            Label nome = new Label(item.getNome());
            nome.setWrapText(true);
            nome.setMaxWidth(110);
            HBox.setHgrow(nome, Priority.ALWAYS);

            Spinner<Integer> spinner = new Spinner<>(1, 99, qtd);
            spinner.setMaxWidth(72);
            spinner.setEditable(true);
            spinner.valueProperty().addListener((obs, o, n) -> {
                carrinho.put(item, n);
                atualizarCarrinho();
            });

            Button btnRem = new Button("✕");
            btnRem.setStyle(
                    "-fx-background-color:#e74c3c; -fx-text-fill:white;" +
                    "-fx-padding:4 8; -fx-background-radius:4; -fx-cursor:hand;");
            btnRem.setOnAction(e -> {
                carrinho.remove(item);
                atualizarCarrinho();
            });

            linha.getChildren().addAll(nome, spinner, btnRem);
            boxCarrinho.getChildren().add(linha);

            total += item.getPreco() * qtd;
        }

        lblTotal.setText("Total: R$ " + String.format("%.2f", total));
    }

    // ------------------------------------------------------------------ ações

    @FXML
    private void confirmarPedido() {
        String email = ((Cliente) Sessao.get().getUsuarioLogado()).getEmail();
        try {
            pedidoCtrl.criarPedido(email, carrinho);
            carrinho.clear();
            atualizarCarrinho();
            UI.info("Pedido enviado!", "Seu pedido foi recebido. Acompanhe em 'Meus Pedidos'.");
        } catch (Exception e) {
            UI.alerta("Erro", e.getMessage());
        }
    }

    @FXML
    private void verHistorico() {
        try { Main.navegarPara("historico_cliente.fxml"); }
        catch (Exception e) { UI.alerta("Erro", e.getMessage()); }
    }

    @FXML
    private void sair() {
        Sessao.get().encerrar();
        try { Main.navegarPara("login.fxml"); }
        catch (Exception e) { UI.alerta("Erro", e.getMessage()); }
    }
}
