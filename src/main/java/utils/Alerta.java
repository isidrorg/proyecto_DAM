package utils;

import javafx.scene.control.Alert;

public class Alerta {

    Alert alerta;

    public Alerta(String tipo, String cabecera, String contenido) {
        switch (tipo) {
            case "INFORMATION":
                alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("INFORMACI\u00d3N");
                break;
            case "WARNING":
                alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("AVISO");
                break;
            case "ERROR":
                alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("ERROR");
                break;
            case "CONFIRMATION":
                throw new IllegalStateException("Invoque este tipo de alerta con el m\u00e9todo Alert.");
            default:
                throw new IllegalStateException("Valor no esperado: " + tipo);
        }

        alerta.setHeaderText(cabecera);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}
