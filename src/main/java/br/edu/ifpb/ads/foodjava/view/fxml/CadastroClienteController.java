package br.edu.ifpb.ads.foodjava.view.fxml;

import br.edu.ifpb.ads.foodjava.Main;
import br.edu.ifpb.ads.foodjava.controller.AuthController;
import br.edu.ifpb.ads.foodjava.util.UI;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class CadastroClienteController {

    @FXML private TextField     tfNome;
    @FXML private TextField     tfEmail;
    @FXML private PasswordField tfSenha;
    @FXML private TextField     tfCpf;
    @FXML private TextField     tfTelefone;
    @FXML private TextField     tfEndereco;

    private final AuthController auth = new AuthController();

    @FXML
    private void cadastrar() {
        try {
            auth.cadastrarCliente(
                    tfNome.getText().trim(),
                    tfEmail.getText().trim(),
                    tfSenha.getText(),
                    tfCpf.getText().trim(),
                    tfTelefone.getText().trim(),
                    tfEndereco.getText().trim()
            );
            UI.info("Cadastro realizado", "Conta criada com sucesso! Faça login para continuar.");
            Main.navegarPara("login.fxml");
        } catch (Exception e) {
            UI.alerta("Erro no cadastro", e.getMessage());
        }
    }

    @FXML
    private void voltarLogin() {
        try {
            Main.navegarPara("login.fxml");
        } catch (Exception e) {
            UI.alerta("Erro", e.getMessage());
        }
    }
}
