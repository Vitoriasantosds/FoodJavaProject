package br.edu.ifpb.ads.foodjava.view.fxml;

import br.edu.ifpb.ads.foodjava.Main;
import br.edu.ifpb.ads.foodjava.controller.CardapioController;
import br.edu.ifpb.ads.foodjava.model.Categoria;
import br.edu.ifpb.ads.foodjava.model.ItemCardapio;
import br.edu.ifpb.ads.foodjava.util.UI;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GerenciarCardapioController implements Initializable {

    // Formulário de adição
    @FXML private TextField  tfNomeItem;
    @FXML private TextField  tfDescItem;
    @FXML private TextField  tfPrecoItem;
    @FXML private ComboBox<Categoria> cbCategoriaItem;
    @FXML private CheckBox   cbDisponivelItem;
    @FXML private TextField  tfImagemItem;

    // Lista / busca
    @FXML private TextField  tfBuscaItem;
    @FXML private ComboBox<String> cbFiltroCat;
    @FXML private VBox       boxItens;

    private final CardapioController ctrl = new CardapioController();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Popula combo de categoria no formulário
        cbCategoriaItem.setItems(FXCollections.observableArrayList(Categoria.values()));

        // Popula combo de filtro
        cbFiltroCat.getItems().add("Todas as categorias");
        for (Categoria c : Categoria.values()) cbFiltroCat.getItems().add(c.getDescricao());
        cbFiltroCat.setValue("Todas as categorias");

        carregarLista();
    }

    // ----------------------------------------------------------------- ações do formulário

    @FXML
    private void adicionarItem() {
        try {
            double preco = Double.parseDouble(tfPrecoItem.getText().replace(",", ".").trim());
            String imagem = tfImagemItem.getText().isBlank() ? null : tfImagemItem.getText().trim();

            ctrl.adicionarItem(
                    tfNomeItem.getText().trim(),
                    tfDescItem.getText().trim(),
                    preco,
                    cbCategoriaItem.getValue(),
                    cbDisponivelItem.isSelected(),
                    imagem
            );

            limparFormulario();
            carregarLista();
            UI.info("Item adicionado", "O item foi cadastrado no cardápio.");
        } catch (NumberFormatException e) {
            UI.alerta("Preço inválido", "Digite um valor numérico válido (ex: 19.90).");
        } catch (Exception e) {
            UI.alerta("Erro", e.getMessage());
        }
    }

    @FXML
    private void selecionarImagem() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Selecionar imagem do item");
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.webp"));
        File f = fc.showOpenDialog(Main.primaryStage);
        if (f != null) tfImagemItem.setText(f.getAbsolutePath());
    }

    @FXML
    private void importarJson() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Importar cardápio JSON");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
        File f = fc.showOpenDialog(Main.primaryStage);
        if (f == null) return;

        try {
            List<String> erros = ctrl.importarJson(f);
            if (erros.isEmpty()) {
                UI.info("Importação concluída", "Todos os itens foram importados com sucesso!");
            } else {
                UI.info("Importação com erros",
                        "Importação concluída com " + erros.size() + " erro(s):\n\n"
                        + String.join("\n", erros));
            }
            carregarLista();
        } catch (Exception e) {
            UI.alerta("Erro na importação", e.getMessage());
        }
    }

    // ----------------------------------------------------------------- busca / filtro

    @FXML
    private void buscar() {
        carregarLista();
    }

    @FXML
    private void buscar(KeyEvent e) {
        carregarLista();
    }

    // ----------------------------------------------------------------- lista

    private void carregarLista() {
        boxItens.getChildren().clear();

        String busca  = tfBuscaItem == null ? "" : tfBuscaItem.getText().toLowerCase().trim();
        String filtro = cbFiltroCat == null ? "Todas as categorias" : cbFiltroCat.getValue();

        List<ItemCardapio> itens = ctrl.listarTodos().stream()
                .filter(i -> busca.isBlank() || i.getNome().toLowerCase().contains(busca)
                        || (i.getDescricao() != null && i.getDescricao().toLowerCase().contains(busca)))
                .filter(i -> "Todas as categorias".equals(filtro)
                        || i.getCategoria().getDescricao().equals(filtro))
                .toList();

        if (itens.isEmpty()) {
            Label vazio = new Label("Nenhum item encontrado.");
            vazio.getStyleClass().add("subtitulo");
            boxItens.getChildren().add(vazio);
            return;
        }

        for (ItemCardapio item : itens) {
            boxItens.getChildren().add(criarLinhaItem(item));
        }
    }

    private HBox criarLinhaItem(ItemCardapio item) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10, 12, 10, 12));
        row.setStyle(
                "-fx-background-color:white; -fx-background-radius:7;" +
                "-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.08),5,0,0,1);");

        // Indicador de disponibilidade
        Label dot = new Label(item.isDisponivel() ? "●" : "○");
        dot.setStyle("-fx-text-fill:" + (item.isDisponivel() ? "#27ae60" : "#bdc3c7") + "; -fx-font-size:16px;");

        VBox info = new VBox(3);
        HBox.setHgrow(info, Priority.ALWAYS);

        Label nome = new Label(item.getNome());
        nome.setStyle("-fx-font-weight:bold; -fx-font-size:13px;");

        Label sub = new Label(item.getCategoria().getDescricao()
                + "   |   R$ " + String.format("%.2f", item.getPreco())
                + (item.isDisponivel() ? "" : "   [inativo]"));
        sub.getStyleClass().add("subtitulo");

        info.getChildren().addAll(nome, sub);

        // Botões
        Button btnEditar = new Button("✏ Editar");
        btnEditar.getStyleClass().add("btn-info");
        btnEditar.setOnAction(e -> abrirEdicao(item));

        Button btnToggle = new Button(item.isDisponivel() ? "Desativar" : "Ativar");
        btnToggle.getStyleClass().add("btn-warning");
        btnToggle.setOnAction(e -> {
            item.setDisponivel(!item.isDisponivel());
            try { ctrl.editarItem(item); carregarLista(); }
            catch (Exception ex) { UI.alerta("Erro", ex.getMessage()); }
        });

        Button btnExcluir = new Button("🗑 Excluir");
        btnExcluir.getStyleClass().add("btn-danger");
        btnExcluir.setOnAction(e -> {
            if (!UI.confirmar("Excluir item", "Deseja excluir '" + item.getNome() + "'?")) return;
            try { ctrl.removerItem(item.getId()); carregarLista(); }
            catch (Exception ex) { UI.alerta("Erro", ex.getMessage()); }
        });

        row.getChildren().addAll(dot, info, btnEditar, btnToggle, btnExcluir);
        return row;
    }

    // ----------------------------------------------------------------- diálogo de edição

    private void abrirEdicao(ItemCardapio item) {
        try {
            FXMLLoader loader = Main.carregarFxml("editar_item.fxml");
            Parent root = loader.load();

            EditarItemController controller = loader.getController();
            controller.preencherItem(item);

            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.initOwner(Main.primaryStage);
            popup.setTitle("Editar item — " + item.getNome());
            popup.setScene(new Scene(root));
            popup.setResizable(false);

            // Callback: após salvar, recarrega a lista
            controller.setOnSalvo(() -> {
                popup.close();
                carregarLista();
            });
            controller.setOnCancelado(popup::close);

            popup.showAndWait();
        } catch (Exception e) {
            UI.alerta("Erro", "Não foi possível abrir o diálogo de edição: " + e.getMessage());
        }
    }

    // ----------------------------------------------------------------- navegação

    @FXML
    private void voltarPainel() {
        try { Main.navegarPara("painel_gerente.fxml"); }
        catch (Exception e) { UI.alerta("Erro", e.getMessage()); }
    }

    // ----------------------------------------------------------------- auxiliar

    private void limparFormulario() {
        tfNomeItem.clear();
        tfDescItem.clear();
        tfPrecoItem.clear();
        tfImagemItem.clear();
        cbCategoriaItem.setValue(null);
        cbDisponivelItem.setSelected(true);
    }
}
