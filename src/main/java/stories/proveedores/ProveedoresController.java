package stories.proveedores;

import entities.Cliente;
import entities.Proveedor;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.apache.commons.lang3.StringUtils;
import utils.Alerta;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ProveedoresController implements Initializable {

    private final ProveedoresFacade facade;
    Proveedor proveedorSeleccionado;

    @FXML private TextField numeroIdentificacion;
    @FXML private TextField nombre;
    @FXML private TextField apellidos;
    @FXML private TextField calle;
    @FXML private TextField numeroCalle;
    @FXML private TextField piso;
    @FXML private TextField puerta;
    @FXML private TextField codigoPostal;
    @FXML private TextField municipio;
    @FXML private TextField provincia;
    @FXML private TextField telefonoMovil;
    @FXML private TextField telefonoFijo;
    @FXML private TextField email;
    @FXML private TextField cuentaBancaria;
    @FXML private Button botonModificar;
    @FXML private Button botonDarAlta;
    @FXML private TextField buscarNombre;
    @FXML private TextField buscarNifCifNie;
    @FXML private ComboBox buscarCodigoPostal;
    @FXML private TableView tablaProveedoresBuscados;
    @FXML private TextArea infoProveedor;
    @FXML private Button botonEditar;
    @FXML private Button botonBorrar;

    public ProveedoresController() {
        super();
        facade = new ProveedoresFacadeImpl();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Obtención de Set de Códigos Postales
        ObservableList<String> codigosPostales = facade.obtenerCodigosPostales();
        buscarCodigoPostal.setItems(codigosPostales);

        // Añadir Listener a la tabla de productos buscados para mostrar los detalles del producto seleccionado
        tablaProveedoresBuscados.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                proveedorSeleccionado = (Proveedor) observable.getValue();
                infoProveedor.setText(proveedorSeleccionado.detalles());
                botonEditar.setDisable(false);
                botonBorrar.setDisable(false);
            }
        });
    }

    @FXML
    public void limpiarProveedor() {
        numeroIdentificacion.clear();
        nombre.clear();
        apellidos.clear();
        calle.clear();
        numeroCalle.clear();
        piso.clear();
        puerta.clear();
        codigoPostal.clear();
        municipio.clear();
        provincia.clear();
        telefonoMovil.clear();
        telefonoFijo.clear();
        email.clear();
        cuentaBancaria.clear();

        botonModificar.setDisable(true);
        botonDarAlta.setDisable(false);
    }

    @FXML
    public void modificarProveedor() throws SQLException {
        if (facade.modificarProveedor(
            proveedorSeleccionado.getId(),
            numeroIdentificacion.getText(),
            nombre.getText(),
            apellidos.getText(),
            calle.getText(),
            numeroCalle.getText(),
            piso.getText(),
            puerta.getText(),
            municipio.getText(),
            provincia.getText(),
            codigoPostal.getText(),
            telefonoMovil.getText(),
            telefonoFijo.getText(),
            email.getText(),
            cuentaBancaria.getText()
        )) {
            new Alerta("INFORMATION", "Modificaci\u00f3n de proveedor", "Modificaci\u00f3n del proveedor realizada con \u00e9xito.");
        } else {
            new Alerta("ERROR", "Modificaci\u00f3n de proveedor", "Fallo en la modificaci\u00f3n del proveedor.");
        }
    }

    @FXML
    public void altaProveedor() {
        facade.altaProveedor(
            numeroIdentificacion.getText(),
            nombre.getText(),
            apellidos.getText(),
            calle.getText(),
            numeroCalle.getText(),
            piso.getText(),
            puerta.getText(),
            municipio.getText(),
            provincia.getText(),
            codigoPostal.getText(),
            telefonoMovil.getText(),
            telefonoFijo.getText(),
            email.getText(),
            cuentaBancaria.getText()
        );
    }

    @FXML
    public void buscarProveedor() {
        int camposBusqueda = 0;
        String nombreBuscado = null;
        String nifBuscado = null;
        String cpBuscado = null;

        if (StringUtils.isNotBlank(buscarNombre.getText())) {
            nombreBuscado = buscarNombre.getText();
            camposBusqueda++;
        }

        if (StringUtils.isNotBlank(buscarNifCifNie.getText())) {
            nifBuscado = buscarNifCifNie.getText();
            camposBusqueda++;
        }

        Object cpObjectBuscado = buscarCodigoPostal.getValue();

        if (cpObjectBuscado != null) {
            cpBuscado = cpObjectBuscado.toString();
            camposBusqueda++;
        }


        if (camposBusqueda == 0) {
            new Alerta("WARNING", "Fallo en la b\u00fasqueda de Proveedores", "No ha indicado ning\u00fan campo de b\u00fasqueda.");
            return;
        } else if (camposBusqueda > 1) {
            new Alerta("WARNING", "Fallo en la b\u00fasqueda de Proveedores", "Solo puede indicar un campo de b\u00fasqueda");
            return;
        }

        ObservableList<Proveedor> proveedoresEncontrados = facade.buscarProveedores(nombreBuscado, nifBuscado, cpBuscado);
        tablaProveedoresBuscados.setItems(proveedoresEncontrados);
    }

    @FXML
    public void limpiarBuscarProveedor() {
        buscarNombre.clear();
        buscarNifCifNie.clear();
        buscarCodigoPostal.getSelectionModel().clearSelection();
        tablaProveedoresBuscados.getItems().clear();
        infoProveedor.clear();
        botonEditar.setDisable(true);
        botonBorrar.setDisable(true);
    }

    @FXML
    public void editarProveedor() {
        numeroIdentificacion.setText(proveedorSeleccionado.getnumeroIdentificacion());
        nombre.setText(proveedorSeleccionado.getNombre());
        apellidos.setText(proveedorSeleccionado.getApellidos());
        calle.setText(proveedorSeleccionado.getDireccion());

        if (proveedorSeleccionado.getNumeroDireccion() != null) {
            numeroCalle.setText(String.valueOf(proveedorSeleccionado.getNumeroDireccion()));
        }

        if (proveedorSeleccionado.getPiso() != null) {
            piso.setText(String.valueOf(proveedorSeleccionado.getPiso()));
        }

        puerta.setText(proveedorSeleccionado.getPuerta());
        municipio.setText(proveedorSeleccionado.getMunicipio());
        provincia.setText(proveedorSeleccionado.getProvincia());

        if (proveedorSeleccionado.getCodigoPostal() != null) {
            codigoPostal.setText(String.valueOf(proveedorSeleccionado.getCodigoPostal()));
        }

        telefonoMovil.setText(proveedorSeleccionado.getMovil());
        telefonoFijo.setText(proveedorSeleccionado.getTelefono());
        email.setText(proveedorSeleccionado.getEmail());
        cuentaBancaria.setText(proveedorSeleccionado.getCuentaBancaria());

        botonModificar.setDisable(false);
        botonDarAlta.setDisable(true);
    }

    @FXML
    public void borrarProveedor() {
        if (facade.confirmarBorrado()) {
            facade.borrarProveedor(proveedorSeleccionado);
        }
    }
}
