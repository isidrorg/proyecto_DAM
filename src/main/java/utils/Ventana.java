package utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class Ventana {

    private static FXMLLoader plantilla;

    public Ventana(Stage stage, String fxmlView, boolean fxmlTemplate) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/" + fxmlView + ".fxml"));
        Scene scene;

        if (fxmlTemplate) {
            plantilla = new FXMLLoader(getClass().getResource("/views/templates/Base.fxml"));
            scene = new Scene(plantilla.load());
            fxmlLoader.setRoot(plantilla.getNamespace().get("contenido"));
            fxmlLoader.load();
        } else {
            scene = new Scene(fxmlLoader.load());
        }

        Font.loadFont(getClass().getClassLoader().getResource("fonts/Ubuntu-R.ttf").toExternalForm(), 13);
        Font.loadFont(getClass().getClassLoader().getResource("fonts/Ubuntu-B.ttf").toExternalForm(), 13);
        Font.loadFont(getClass().getClassLoader().getResource("fonts/UbuntuMono-R.ttf").toExternalForm(), 13);

        stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("images/logo.png")));
        stage.setTitle("Gestor de Productos");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static FXMLLoader getPlantilla() {
        return plantilla;
    }
}
