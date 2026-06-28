package br.edu.ifpb.ads.foodjava.view.fxml;

import br.edu.ifpb.ads.foodjava.Main;
import br.edu.ifpb.ads.foodjava.controller.RestauranteController;
import br.edu.ifpb.ads.foodjava.util.UI;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ConfiguracaoInicialController {

    @FXML private TextField     tfNome;
    @FXML private TextField     tfCnpj;
    @FXML private TextField     tfEndereco;
    @FXML private TextField     tfTelefone;
    @FXML private TextField     tfCategoria;
    @FXML private TextField     tfNomeGerente;
    @FXML private TextField     tfEmail;
    @FXML private PasswordField tfSenha;

    private final RestauranteController ctrl = new RestauranteController();

    @FXML
    private void salvar() {
        try {
            ctrl.configurar(
                    tfNome.getText().trim(),
                    tfCnpj.getText().trim(),
                    tfEndereco.getText().trim(),
                    tfTelefone.getText().trim(),
                    tfCategoria.getText().trim(),
                    tfEmail.getText().trim(),
                    tfNomeGerente.getText().trim(),
                    tfSenha.getText()
            );
            UI.info("Sucesso", "Restaurante configurado com sucesso! Faça login para continuar.");
            Main.navegarPara("login.fxml");
        } catch (Exception e) {
            UI.alerta("Erro na configuração", e.getMessage());
        }
    }
}
