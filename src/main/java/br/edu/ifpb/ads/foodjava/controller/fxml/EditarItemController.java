package br.edu.ifpb.ads.foodjava.controller.fxml;

import br.edu.ifpb.ads.foodjava.controller.CardapioController;
import br.edu.ifpb.ads.foodjava.model.Categoria;
import br.edu.ifpb.ads.foodjava.model.ItemCardapio;
import br.edu.ifpb.ads.foodjava.util.UI;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class EditarItemController implements Initializable {

    @FXML private Label    lblTituloEdicao;
    @FXML private TextField  tfNome;
    @FXML private TextField  tfDesc;
    @FXML private TextField  tfPreco;
    @FXML private ComboBox<Categoria> cbCategoria;
    @FXML private CheckBox   cbDisponivel;

    private final CardapioController ctrl = new CardapioController();
    private ItemCardapio itemAtual;

    // Callbacks injetados pelo controller pai
    private Runnable onSalvo;
    private Runnable onCancelado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbCategoria.setItems(FXCollections.observableArrayList(Categoria.values()));
    }

    /** Chamado pelo GerenciarCardapioController antes de exibir o popup. */
    public void preencherItem(ItemCardapio item) {
        this.itemAtual = item;
        lblTituloEdicao.setText("Editar: " + item.getNome());
        tfNome.setText(item.getNome());
        tfDesc.setText(item.getDescricao() == null ? "" : item.getDescricao());
        tfPreco.setText(String.format("%.2f", item.getPreco()));
        cbCategoria.setValue(item.getCategoria());
        cbDisponivel.setSelected(item.isDisponivel());
    }

    public void setOnSalvo(Runnable r)     { this.onSalvo     = r; }
    public void setOnCancelado(Runnable r) { this.onCancelado = r; }

    @FXML
    private void salvar() {
        try {
            double preco = Double.parseDouble(tfPreco.getText().replace(",", ".").trim());
            itemAtual.setNome(tfNome.getText().trim());
            itemAtual.setDescricao(tfDesc.getText().trim());
            itemAtual.setPreco(preco);
            itemAtual.setCategoria(cbCategoria.getValue());
            itemAtual.setDisponivel(cbDisponivel.isSelected());

            ctrl.editarItem(itemAtual);

            if (onSalvo != null) onSalvo.run();
        } catch (NumberFormatException e) {
            UI.alerta("Preço inválido", "Digite um valor numérico válido (ex: 19.90).");
        } catch (Exception e) {
            UI.alerta("Erro ao salvar", e.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        if (onCancelado != null) onCancelado.run();
    }
}
