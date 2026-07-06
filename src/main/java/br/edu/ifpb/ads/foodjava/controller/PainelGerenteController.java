package br.edu.ifpb.ads.foodjava.controller;

import br.edu.ifpb.ads.foodjava.view.Main;
import br.edu.ifpb.ads.foodjava.model.Gerente;
import br.edu.ifpb.ads.foodjava.model.ItemPedido;
import br.edu.ifpb.ads.foodjava.model.Pedido;
import br.edu.ifpb.ads.foodjava.model.StatusPedido;
import br.edu.ifpb.ads.foodjava.util.Sessao;
import br.edu.ifpb.ads.foodjava.util.UI;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PainelGerenteController implements Initializable {

    @FXML private Label              lblNomeGerente;
    @FXML private ComboBox<String>   cbFiltroStatus;
    @FXML private VBox               boxPedidos;
    @FXML private Label              lblTotalPedidos;
    @FXML private Label              lblFaturamento;

    private final PedidoController pedidoCtrl = new PedidoController();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Gerente g = (Gerente) Sessao.get().getUsuarioLogado();
        lblNomeGerente.setText("🍴 FoodJava  —  " + g.getNome());

        cbFiltroStatus.setItems(FXCollections.observableArrayList(
                "Todos",
                StatusPedido.AGUARDANDO_CONFIRMACAO.name(),
                StatusPedido.CONFIRMADO.name(),
                StatusPedido.EM_PREPARO.name(),
                StatusPedido.SAIU_PARA_ENTREGA.name(),
                StatusPedido.ENTREGUE.name(),
                StatusPedido.CANCELADO.name()
        ));
        cbFiltroStatus.setValue("Todos");

        carregarPedidos();
    }

    @FXML
    private void filtrar() {
        carregarPedidos();
    }

    private void carregarPedidos() {
        boxPedidos.getChildren().clear();

        List<Pedido> todos = pedidoCtrl.listarTodos();
        String filtro = cbFiltroStatus.getValue();

        List<Pedido> filtrados = todos.stream()
                .filter(p -> "Todos".equals(filtro) || p.getStatus().name().equals(filtro))
                .sorted((a, b) -> b.getDataHora().compareTo(a.getDataHora()))
                .toList();

        if (filtrados.isEmpty()) {
            Label vazio = new Label("Nenhum pedido encontrado para o filtro selecionado.");
            vazio.getStyleClass().add("subtitulo");
            boxPedidos.getChildren().add(vazio);
        } else {
            for (Pedido p : filtrados) {
                boxPedidos.getChildren().add(criarCard(p));
            }
        }

        // Atualiza resumo (sempre sobre todos os pedidos)
        long totalQtd = todos.size();
        double faturamento = todos.stream()
                .filter(p -> p.getStatus() != StatusPedido.CANCELADO)
                .mapToDouble(Pedido::getTotal).sum();

        lblTotalPedidos.setText("Total de pedidos: " + totalQtd);
        lblFaturamento.setText("Faturamento geral: R$ " + String.format("%.2f", faturamento));
    }

    private VBox criarCard(Pedido p) {
        VBox card = new VBox(8);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(14));

        // Topo
        HBox topo = new HBox(10);
        topo.setAlignment(Pos.CENTER_LEFT);

        Label id = new Label("Pedido #" + p.getId().substring(0, 8).toUpperCase());
        id.setStyle("-fx-font-weight:bold; -fx-font-size:14px;");

        Label info = new Label(p.getDataHora() + "   |   " + p.getEmailCliente());
        info.getStyleClass().add("subtitulo");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label badge = new Label(p.getStatus().getDescricao());
        badge.setStyle(
                "-fx-background-color:" + UI.corStatus(p.getStatus()) + ";" +
                "-fx-text-fill:white; -fx-padding:4 12; -fx-background-radius:12;" +
                "-fx-font-size:12px; -fx-font-weight:bold;");

        topo.getChildren().addAll(id, info, spacer, badge);

        // Itens do pedido
        Separator sep = new Separator();
        VBox itensBox = new VBox(4);
        for (ItemPedido ip : p.getItens()) {
            Label l = new Label("• " + ip.getQuantidade() + "x  " + ip.getItem().getNome()
                    + "   —   R$ " + String.format("%.2f", ip.getSubtotal()));
            l.getStyleClass().add("subtitulo");
            itensBox.getChildren().add(l);
        }

        Label total = new Label("Total: R$ " + String.format("%.2f", p.getTotal()));
        total.setStyle("-fx-font-weight:bold; -fx-font-size:14px; -fx-text-fill:#e74c3c;");

        // Botões de ação
        HBox acoes = new HBox(10);
        acoes.setAlignment(Pos.CENTER_LEFT);

        StatusPedido proximo = p.getStatus().proximo();
        if (proximo != null) {
            Button btnAvancar = new Button("→  " + proximo.getDescricao());
            btnAvancar.getStyleClass().add("btn-success");
            btnAvancar.setOnAction(e -> {
                try {
                    pedidoCtrl.avancarStatus(p.getId());
                    carregarPedidos();
                } catch (Exception ex) {
                    UI.alerta("Erro", ex.getMessage());
                }
            });
            acoes.getChildren().add(btnAvancar);
        }

        if (p.getStatus() == StatusPedido.AGUARDANDO_CONFIRMACAO) {
            Button btnCancelar = new Button("Cancelar");
            btnCancelar.getStyleClass().add("btn-danger");
            btnCancelar.setOnAction(e -> {
                if (!UI.confirmar("Cancelar pedido", "Confirma o cancelamento deste pedido?")) return;
                try {
                    pedidoCtrl.cancelarPedido(p.getId());
                    carregarPedidos();
                } catch (Exception ex) {
                    UI.alerta("Erro", ex.getMessage());
                }
            });
            acoes.getChildren().add(btnCancelar);
        }

        card.getChildren().addAll(topo, sep, itensBox, total);
        if (!acoes.getChildren().isEmpty()) card.getChildren().add(acoes);

        return card;
    }

    @FXML
    private void irCardapio() {
        try { Main.navegarPara("gerenciar_cardapio.fxml"); }
        catch (Exception e) { UI.alerta("Erro", e.getMessage()); }
    }

    @FXML
    private void sair() {
        Sessao.get().encerrar();
        try { Main.navegarPara("login.fxml"); }
        catch (Exception e) { UI.alerta("Erro", e.getMessage()); }
    }
}
