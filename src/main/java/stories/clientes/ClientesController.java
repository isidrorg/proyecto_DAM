package stories.clientes;

import entities.Cliente;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.apache.commons.lang3.StringUtils;
import utils.Alerta;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ClientesController implements Initializable {

    private final ClientesFacade facade;
    Cliente clienteSeleccionado;

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
    @FXML private TableView tablaClientesBuscados;
    @FXML private TextArea infoCliente;
    @FXML private Button botonEditar;
    @FXML private Button botonBorrar;

    public ClientesController() {
        super();
        facade = new ClientesFacadeImpl();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Obtención de Set de Códigos Postales
        ObservableList<String> codigosPostales = facade.obtenerCodigosPostales();
        buscarCodigoPostal.setItems(codigosPostales);

        // Añadir Listener a la tabla de productos buscados para mostrar los detalles del producto seleccionado
        tablaClientesBuscados.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                clienteSeleccionado = (Cliente) observable.getValue();
                infoCliente.setText(clienteSeleccionado.detalles());
                botonEditar.setDisable(false);
                botonBorrar.setDisable(false);
            }
        });
    }

    @FXML
    public void limpiarCliente() {
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
    public void modificarCliente() throws SQLException {
        if (facade.modificarCliente(clienteSeleccionado.getId(),
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
                                    cuentaBancaria.getText())) {
            new Alerta("INFORMATION", "Modificaci\u00f3n de cliente", "Modificaci\u00f3n del cliente realizada con \u00e9xito.");
        }
    }

    @FXML
    public void altaCliente() {
        facade.altaCliente(numeroIdentificacion.getText(),
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
                           cuentaBancaria.getText());
    }

    @FXML
    public void buscarCliente() {
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
            new Alerta("WARNING", "Fallo en la b\u00fasqueda de Clientes", "No ha indicado ning\u00fan campo de b\u00fasqueda.");
            return;
        } else if (camposBusqueda > 1) {
            new Alerta("WARNING", "Fallo en la b\u00fasqueda de Clientes", "Solo puede indicar un campo de b\u00fasqueda");
            return;
        }

        ObservableList<Cliente> clientesEncontrados = facade.buscarClientes(nombreBuscado, nifBuscado, cpBuscado);
        tablaClientesBuscados.setItems(clientesEncontrados);
    }

    @FXML
    public void limpiarBuscarCliente() {
        buscarNombre.clear();
        buscarNifCifNie.clear();
        buscarCodigoPostal.getSelectionModel().clearSelection();
        tablaClientesBuscados.getItems().clear();
        infoCliente.clear();
        botonEditar.setDisable(true);
        botonBorrar.setDisable(true);
    }

    @FXML
    public void editarCliente() {
        numeroIdentificacion.setText(clienteSeleccionado.getnumeroIdentificacion());
        nombre.setText(clienteSeleccionado.getNombre());
        apellidos.setText(clienteSeleccionado.getApellidos());
        calle.setText(clienteSeleccionado.getDireccion());

        if (clienteSeleccionado.getNumeroDireccion() != null) {
            numeroCalle.setText(String.valueOf(clienteSeleccionado.getNumeroDireccion()));
        }

        if (clienteSeleccionado.getPiso() != null) {
            piso.setText(String.valueOf(clienteSeleccionado.getPiso()));
        }

        puerta.setText(clienteSeleccionado.getPuerta());
        municipio.setText(clienteSeleccionado.getMunicipio());
        provincia.setText(clienteSeleccionado.getProvincia());

        if (clienteSeleccionado.getCodigoPostal() != null) {
            codigoPostal.setText(String.valueOf(clienteSeleccionado.getCodigoPostal()));
        }

        telefonoMovil.setText(clienteSeleccionado.getMovil());
        telefonoFijo.setText(clienteSeleccionado.getTelefono());
        email.setText(clienteSeleccionado.getEmail());
        cuentaBancaria.setText(clienteSeleccionado.getCuentaBancaria());

        botonModificar.setDisable(false);
        botonDarAlta.setDisable(true);
    }

    @FXML
    public void borrarCliente() throws SQLException {
        if (facade.confirmarBorrado()) {
            facade.borrarCliente(clienteSeleccionado);
        }
    }
}
