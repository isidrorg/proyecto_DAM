package stories.base;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.Ventana;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BaseController implements Initializable  {

    private static final Logger logger = LogManager.getLogger(BaseController.class);

    @FXML private VBox ventanaPrincipal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    /**
     * Cierre de sesión
     * @throws IOException
     */
    @FXML
    private void cerrarSesion() throws IOException {
        Stage stage = (Stage) ventanaPrincipal.getScene().getWindow();
        stage.close();
        new Ventana(new Stage(), "Login", false);
    }

    /**
     * Salida de la aplicación
     */
    @FXML
    private void salir() {
        Platform.exit();
    }

    /**
     * Carga la vista FXML indicada en la plantilla
     * @param vistaFXML
     */
    protected void cargarContenido(String vistaFXML){
        try {
            AnchorPane plantillaContenido = (AnchorPane) Ventana.getPlantilla().getNamespace().get("contenido");
            plantillaContenido.getChildren().clear();
            FXMLLoader contenido = new FXMLLoader(
                getClass().getResource("/views/" + vistaFXML + ".fxml")
            );
            contenido.setRoot(Ventana.getPlantilla().getNamespace().get("contenido"));
            contenido.load();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Acceso a las distintas páginas
     */
    public void cargarFacturas(ActionEvent actionEvent) {
        cargarContenido("Facturas");
    }

    public void cargarClientes(ActionEvent actionEvent) {
        cargarContenido("Clientes");
    }

    public void cargarProductos(ActionEvent actionEvent) {
        cargarContenido("Productos");
    }

    public void cargarProveedores(ActionEvent actionEvent) {
        cargarContenido("Proveedores");
    }

    public void cargarRecibos(ActionEvent actionEvent) {
        cargarContenido("Recibos");
    }

    public void cargarUsuario(ActionEvent actionEvent) {
        cargarContenido("Usuario");
    }
}
