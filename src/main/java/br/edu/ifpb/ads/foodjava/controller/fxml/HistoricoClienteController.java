package br.edu.ifpb.ads.foodjava.controller.fxml;

import br.edu.ifpb.ads.foodjava.Main;
import br.edu.ifpb.ads.foodjava.controller.PedidoController;
import br.edu.ifpb.ads.foodjava.model.Cliente;
import br.edu.ifpb.ads.foodjava.model.ItemPedido;
import br.edu.ifpb.ads.foodjava.model.Pedido;
import br.edu.ifpb.ads.foodjava.model.StatusPedido;
import br.edu.ifpb.ads.foodjava.util.Sessao;
import br.edu.ifpb.ads.foodjava.util.UI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HistoricoClienteController implements Initializable {

    @FXML private VBox boxPedidos;

    private final PedidoController pedidoCtrl = new PedidoController();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarPedidos();
    }

    private void carregarPedidos() {
        boxPedidos.getChildren().clear();
        String email = ((Cliente) Sessao.get().getUsuarioLogado()).getEmail();
        List<Pedido> pedidos = pedidoCtrl.listarPorCliente(email);

        if (pedidos.isEmpty()) {
            Label vazio = new Label("Você ainda não realizou nenhum pedido.");
            vazio.getStyleClass().add("subtitulo");
            boxPedidos.getChildren().add(vazio);
            return;
        }

        // Exibe do mais recente para o mais antigo
        for (int i = pedidos.size() - 1; i >= 0; i--) {
            boxPedidos.getChildren().add(criarCard(pedidos.get(i)));
        }
    }

    private VBox criarCard(Pedido p) {
        VBox card = new VBox(8);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(14));

        // Linha do topo: id, data, status
        HBox topo = new HBox(10);
        topo.setAlignment(Pos.CENTER_LEFT);

        Label id = new Label("Pedido #" + p.getId().substring(0, 8).toUpperCase());
        id.setStyle("-fx-font-weight:bold; -fx-font-size:14px;");

        Label data = new Label(p.getDataHora());
        data.getStyleClass().add("subtitulo");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label badge = new Label(p.getStatus().getDescricao());
        badge.setStyle(
                "-fx-background-color:" + UI.corStatus(p.getStatus()) + ";" +
                "-fx-text-fill:white; -fx-padding:4 12; -fx-background-radius:12;" +
                "-fx-font-size:12px; -fx-font-weight:bold;");

        topo.getChildren().addAll(id, data, spacer, badge);

        // Itens
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

        card.getChildren().addAll(topo, sep, itensBox, total);

        // Botão cancelar (só AGUARDANDO_CONFIRMACAO)
        if (p.getStatus() == StatusPedido.AGUARDANDO_CONFIRMACAO) {
            Button btnCancelar = new Button("Cancelar pedido");
            btnCancelar.getStyleClass().add("btn-danger");
            btnCancelar.setOnAction(e -> {
                if (!UI.confirmar("Cancelar", "Deseja cancelar este pedido?")) return;
                try {
                    pedidoCtrl.cancelarPedido(p.getId());
                    carregarPedidos();
                } catch (Exception ex) {
                    UI.alerta("Erro", ex.getMessage());
                }
            });
            card.getChildren().add(btnCancelar);
        }

        return card;
    }

    @FXML
    private void voltarCardapio() {
        try { Main.navegarPara("cardapio_cliente.fxml"); }
        catch (Exception e) { UI.alerta("Erro", e.getMessage()); }
    }
}
