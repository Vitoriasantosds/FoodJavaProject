package br.edu.ifpb.ads.foodjava.view;

import br.edu.ifpb.ads.foodjava.repository.RestauranteRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    public static Stage primaryStage;

    private static final double LARGURA = 800;
    private static final double ALTURA  = 600;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        primaryStage.setWidth(LARGURA);
        primaryStage.setHeight(ALTURA);
        primaryStage.setResizable(true);
        primaryStage.setTitle("FoodJava");

        RestauranteRepository repo = new RestauranteRepository();
        String fxml = repo.restauranteConfigurado() ? "login.fxml" : "configuracao_inicial.fxml";
        navegarPara(fxml);

        primaryStage.show();
    }

    public static void navegarPara(String fxml) throws Exception {
        URL url = Main.class.getResource("/fxml/" + fxml);
        if (url == null) throw new RuntimeException("FXML não encontrado: " + fxml);
        Parent root = FXMLLoader.load(url);

        if (primaryStage.getScene() == null) {
            primaryStage.setScene(new Scene(root, LARGURA, ALTURA));
        } else {
            primaryStage.getScene().setRoot(root);
            // Garante que a janela mantenha 800x600 ao trocar de tela
            primaryStage.setWidth(LARGURA);
            primaryStage.setHeight(ALTURA);
        }
    }

    public static FXMLLoader carregarFxml(String fxml) throws Exception {
        URL url = Main.class.getResource("/fxml/" + fxml);
        if (url == null) throw new RuntimeException("FXML não encontrado: " + fxml);
        return new FXMLLoader(url);
    }

    public static void main(String[] args) { launch(args); }
}
