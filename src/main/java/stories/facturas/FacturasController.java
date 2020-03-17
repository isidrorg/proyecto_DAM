package stories.facturas;

import entities.Cliente;
import entities.Factura;
import entities.Producto;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.apache.commons.lang3.StringUtils;
import utils.Alerta;
import utils.ProductoEnFactura;
import utils.Validar;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class FacturasController implements Initializable {

    private final FacturasFacade facade;
    private Factura facturaSeleccionada;
    private float baseImponibleTemporal = 0f;
    private float totalFacturaTemporal = 0f;

    @FXML private TextField numeroFactura;
    @FXML private ComboBox<Cliente> cliente;
    @FXML private ComboBox<Producto> producto;
    @FXML private TextField cantidadProducto;
    @FXML private TableView<ProductoEnFactura> tablaInsertarProductos;
    @FXML private TextField baseImponible;
    @FXML private TextField impuestosBase;
    @FXML private TextField impuestosTotal;
    @FXML private TextField total;
    @FXML private Button botonEmitir;
    @FXML private Button botonImprimir;
    @FXML private TextField buscarNumeroFactura;
    @FXML private ComboBox<Cliente> buscarCliente;
    @FXML private TableView<Factura> tablaBuscarFactura;
    @FXML private TextArea infoFactura;
    @FXML private Button botonImprimirFacturaBuscada;

    public FacturasController() {
        super();
        facade = new FacturasFacadeImpl();
    }

    /**
     * Inicializa la página añadiendo los datos especificados
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Generar automáticamente el número de la factura
        numeroFactura.setText(facade.obtenerNumeroSiguienteFactura());

        // Se recoge la lista de clientes y se inserta en el ComboBox de facturación y se muestra el primero por defecto
        ObservableList<Cliente> clientes = facade.obtenerListaClientes();
        cliente.setItems(clientes);
        buscarCliente.setItems(clientes);

        // Se recoge la lista de productos y se inserta en el ComboBox de facturación
        ObservableList<Producto> productos = facade.obtenerListaProductos();
        producto.setItems(productos);

        // Añadir Listener a la tabla de facturas buscadas para mostrar los detalles de la factura seleccionada
        tablaBuscarFactura.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                facturaSeleccionada = observable.getValue();
                infoFactura.setText(facade.detallesFactura(facturaSeleccionada));
                botonImprimirFacturaBuscada.setDisable(false);
            }
        });
    }

    @FXML
    public void setProductoEnLista() {
        if(producto.getValue() == null) {
            new Alerta("WARNING","Error en la introducci\u00f3n de productos","No ha seleccionado ning\u00fan producto");
            return;
        }

        if (!Validar.esEntero(cantidadProducto.getText())) {
            new Alerta("WARNING", "Error en la introducci\u00f3n de productos", "No se ha introducido la cantidad correctamente");
            return;
        }

        Producto productoAFacturar = producto.getValue();

        // Recogida de datos del formulario
        Integer id = productoAFacturar.getId();
        String nombre = productoAFacturar.getNombre();
        String marca = productoAFacturar.getMarca();
        Integer unidades = Integer.valueOf(cantidadProducto.getText());
        float precio = productoAFacturar.getPrecio();

        // Comprobación de que quedan la cantidad de productos requerida en el almacén
        if (productoAFacturar.getCantidad() < unidades) {
            new Alerta("WARNING", "Error en la introducci\u00f3n de productos", "No quedan suficientes existencias del producto");
            return;
        }

        // Sumar productos a base imponible, calcular impuestos y total
        baseImponibleTemporal = baseImponibleTemporal + unidades * precio;
        float impuestosCantidadNueva = baseImponibleTemporal * 0.07f;
        totalFacturaTemporal = baseImponibleTemporal + impuestosCantidadNueva;

        // Formateo de la salida y adición a la tabla de productos
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        String precioEuros = nf.format(precio);
        ProductoEnFactura productoEnFactura = new ProductoEnFactura(id, nombre, marca, unidades, precioEuros);
        tablaInsertarProductos.getItems().add(productoEnFactura);
        baseImponible.setText(nf.format(baseImponibleTemporal));
        impuestosTotal.setText(nf.format(impuestosCantidadNueva));
        total.setText(nf.format(totalFacturaTemporal));

        // Limpiar producto seleccionado y unidades
        producto.setValue(null);
        cantidadProducto.clear();

        // Habilitar emisión e impresión de facturas
        botonEmitir.setDisable(false);
    }

    @FXML
    public void limpiarFactura() {
        // Borrado de campos del formulario
        numeroFactura.setText(facade.obtenerNumeroSiguienteFactura());
        cliente.getSelectionModel().clearSelection();
        producto.getSelectionModel().clearSelection();
        cantidadProducto.clear();
        tablaInsertarProductos.getItems().clear();
        baseImponible.clear();
        impuestosTotal.clear();
        total.clear();

        // Deshabilitar emisión e impresión de facturas sin contenido
        botonEmitir.setDisable(true);
        botonImprimir.setDisable(true);

        // Reiniciar las variables temporales
        baseImponibleTemporal = 0f;
        totalFacturaTemporal = 0f;
    }

    @FXML
    public void emitirFactura() throws SQLException {
        if (totalFacturaTemporal == 0) {
            new Alerta("WARNING", "Error en la emisi\u00f3n de Factura", "Tiene que añadir productos para poder emitir una factura.");
            return;
        }

        facade.emitirFactura(numeroFactura.getText(),
                             cliente.getValue(),
                             tablaInsertarProductos.getItems(),
                             totalFacturaTemporal);

        botonEmitir.setDisable(true);
        botonImprimir.setDisable(false);
    }

    @FXML
    public void imprimirFactura() throws IOException, SQLException {
        facade.imprimirFactura(numeroFactura.getText());
    }

    @FXML
    public void buscarFactura() {
        int campoBusqueda = 0;
        String numeroFacturaBuscado = null;
        Cliente clienteBuscado = null;

        if (StringUtils.isNotBlank(buscarNumeroFactura.getText())) {
            if (!Validar.esNumeroFactura(numeroFacturaBuscado)) {
                new Alerta ("WARNING", "Error en la b\u00fasqueda de factura", "El n\u00famero de factura indicado no es v\u00e1lido.");
                return;
            }

            numeroFacturaBuscado = buscarNumeroFactura.getText();
            campoBusqueda++;
        }

        if (buscarCliente.getValue() != null) {
            clienteBuscado = buscarCliente.getValue();
            campoBusqueda++;
        }

        if (campoBusqueda != 1) {
            new Alerta("WARNING", "B\u00fasqueda de facturas", "Debe indicar un campo para realizar la b\u00fasqueda y solo se admite uno.");
            return;
        }

        ObservableList<Factura> facturasBuscadas = facade.buscarFactura(numeroFacturaBuscado, clienteBuscado);

        if (facturasBuscadas.isEmpty()) {
            new Alerta("INFORMATION", "B\u00fasqueda de facturas", "No se ha encontrado ninguna factura con el par\u00e1metro indicado");
            return;
        }

        tablaBuscarFactura.setItems(facturasBuscadas);

        botonImprimirFacturaBuscada.setDisable(false);
    }

    @FXML
    public void limpiarBusquedaFactura() {
        buscarNumeroFactura.clear();
        buscarCliente.getSelectionModel().clearSelection();
        tablaBuscarFactura.getItems().clear();
        infoFactura.clear();

        botonImprimirFacturaBuscada.setDisable(true);
    }

    @FXML
    public void imprimirFacturaBuscada() throws IOException, SQLException {
        facade.imprimirFactura(facturaSeleccionada.getNumeroFactura());
    }
}
