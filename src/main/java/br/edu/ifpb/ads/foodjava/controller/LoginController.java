package br.edu.ifpb.ads.foodjava.controller;

import br.edu.ifpb.ads.foodjava.view.Main;
import br.edu.ifpb.ads.foodjava.model.Cliente;
import br.edu.ifpb.ads.foodjava.model.Gerente;
import br.edu.ifpb.ads.foodjava.model.Usuario;
import br.edu.ifpb.ads.foodjava.util.Sessao;
import br.edu.ifpb.ads.foodjava.util.UI;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField     tfEmail;
    @FXML private PasswordField tfSenha;

    private final AuthController auth = new AuthController();

    @FXML
    private void entrar() {
        String email = tfEmail.getText().trim();
        String senha = tfSenha.getText();

        if (email.isBlank() || senha.isBlank()) {
            UI.alerta("Campos obrigatórios", "Informe e-mail e senha.");
            return;
        }

        Usuario u = auth.login(email, senha);
        if (u == null) {
            UI.alerta("Acesso negado", "E-mail ou senha inválidos.");
            tfSenha.clear();
            return;
        }

        Sessao.get().setUsuarioLogado(u);

        try {
            if (u instanceof Gerente) {
                Main.navegarPara("painel_gerente.fxml");
            } else if (u instanceof Cliente) {
                Main.navegarPara("cardapio_cliente.fxml");
            }
        } catch (Exception e) {
            UI.alerta("Erro", "Não foi possível carregar a tela: " + e.getMessage());
        }
    }

    @FXML
    private void irParaCadastro() {
        try {
            Main.navegarPara("cadastro_cliente.fxml");
        } catch (Exception e) {
            UI.alerta("Erro", e.getMessage());
        }
    }
}
