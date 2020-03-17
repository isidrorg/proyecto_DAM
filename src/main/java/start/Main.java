package start;

import javafx.application.Application;
import javafx.stage.Stage;
import utils.Ventana;

public class Main extends Application {

    /**
     * Arranque de la aplicación, lanza la ventana de Login de usuario
     * o directamente a una vista con plantilla para Desarrollo
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        boolean desarrollo = false;

        if (desarrollo) {
            new Ventana(stage, "Facturas", true);
        } else {
            new Ventana(stage, "Login", false);
        }
    }

    /**
     * Método de arranque
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
