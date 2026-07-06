package br.edu.ifpb.ads.foodjava.util;

import br.edu.ifpb.ads.foodjava.model.StatusPedido;
import javafx.scene.control.*;

/** Utilitários de interface: alertas, diálogos e cores de status. */
public class UI {

    public static void alerta(String titulo, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    public static void info(String titulo, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    public static boolean confirmar(String titulo, String msg) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(msg);
        return a.showAndWait().filter(r -> r == ButtonType.OK).isPresent();
    }

    /** Retorna a cor hexadecimal correspondente ao status do pedido. */
    public static String corStatus(StatusPedido s) {
        return switch (s) {
            case AGUARDANDO_CONFIRMACAO -> "#e67e22";
            case CONFIRMADO             -> "#2980b9";
            case EM_PREPARO             -> "#8e44ad";
            case SAIU_PARA_ENTREGA      -> "#16a085";
            case ENTREGUE               -> "#27ae60";
            case CANCELADO              -> "#c0392b";
        };
    }
}
