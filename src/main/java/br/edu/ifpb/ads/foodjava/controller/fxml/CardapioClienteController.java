package br.edu.ifpb.ads.foodjava.controller.fxml;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class CardapioClienteController implements Initializable {

    @FXML private Label lblNomeRestaurante;
    @FXML private Label lblNomeCliente;
    @FXML private VBox  boxCardapio;
    @FXML private VBox  boxCarrinho;
    @FXML private Label lblTotal;

    private static final int    IMG_SIZE    = 90;
    private static final String PLACEHOLDER = "/images/placeholder.png";

    private final CardapioController cardapioCtrl = new CardapioController();
    private final PedidoController   pedidoCtrl   = new PedidoController();
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
            VBox.setMargin(titulo, new Insets(8, 0, 4, 0));
            boxCardapio.getChildren().add(titulo);

            for (ItemCardapio item : porCat) {
                boxCardapio.getChildren().add(criarCardItem(item));
            }
        }

        if (itens.isEmpty()) {
            Label vazio = new Label("Nenhum item disponível no momento.");
            vazio.getStyleClass().add("subtitulo");
            boxCardapio.getChildren().add(vazio);
        }
    }

    private HBox criarCardItem(ItemCardapio item) {
        HBox card = new HBox(14);
        card.getStyleClass().add("card");
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(10));

        // ── Imagem ──────────────────────────────────────────────────────
        ImageView imgView = new ImageView(carregarImagem(item.getImagemPath()));
        imgView.setFitWidth(IMG_SIZE);
        imgView.setFitHeight(IMG_SIZE);
        imgView.setPreserveRatio(true);
        imgView.setSmooth(true);
        // Container com fundo cinza claro para imagens menores que 90x90
        StackPane imgBox = new StackPane(imgView);
        imgBox.setMinSize(IMG_SIZE, IMG_SIZE);
        imgBox.setMaxSize(IMG_SIZE, IMG_SIZE);
        imgBox.setStyle(
            "-fx-background-color:#f0f0f0;" +
            "-fx-background-radius:6;" +
            "-fx-border-color:#e0e0e0;" +
            "-fx-border-radius:6;" +
            "-fx-border-width:1;"
        );

        // ── Informações ─────────────────────────────────────────────────
        VBox info = new VBox(5);
        HBox.setHgrow(info, Priority.ALWAYS);

        Label nome = new Label(item.getNome());
        nome.getStyleClass().add("label-nome-item");

        Label desc = new Label(item.getDescricao() == null ? "" : item.getDescricao());
        desc.setWrapText(true);
        desc.getStyleClass().add("subtitulo");

        Label preco = new Label("R$ " + String.format("%.2f", item.getPreco()));
        preco.getStyleClass().add("label-preco");

        info.getChildren().addAll(nome, desc, preco);

        // ── Botão ────────────────────────────────────────────────────────
        Button btnAdd = new Button("+ Adicionar");
        btnAdd.getStyleClass().add("btn-success");
        btnAdd.setOnAction(e -> {
            carrinho.merge(item, 1, Integer::sum);
            atualizarCarrinho();
        });

        card.getChildren().addAll(imgBox, info, btnAdd);
        return card;
    }

    /**
     * Tenta carregar a imagem do item.
     * Ordem de tentativas:
     *   1. Caminho absoluto no sistema de arquivos
     *   2. Caminho relativo à pasta do projeto (ex: uploads/foto.jpg)
     *   3. Placeholder embutido no JAR
     */
    private Image carregarImagem(String caminho) {
        if (caminho != null && !caminho.isBlank()) {
            // 1. Caminho absoluto
            File f = new File(caminho);
            if (f.exists()) {
                try {
                    return new Image(f.toURI().toString(),
                            IMG_SIZE, IMG_SIZE, true, true, true);
                } catch (Exception ignored) {}
            }
            // 2. Caminho relativo ao diretório de execução
            File rel = new File(System.getProperty("user.dir"), caminho);
            if (rel.exists()) {
                try {
                    return new Image(rel.toURI().toString(),
                            IMG_SIZE, IMG_SIZE, true, true, true);
                } catch (Exception ignored) {}
            }
        }
        // 3. Placeholder interno
        return placeholder();
    }

    private Image placeholder() {
        InputStream is = getClass().getResourceAsStream(PLACEHOLDER);
        if (is != null) return new Image(is, IMG_SIZE, IMG_SIZE, true, true);
        // Fallback: retângulo cinza vazio (não deveria acontecer)
        return new Image("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mN88vz5fwAI3AN6BuYdYwAAAABJRU5ErkJggg==");
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
