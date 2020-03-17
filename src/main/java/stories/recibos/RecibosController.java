package stories.recibos;

import entities.Producto;
import entities.Proveedor;
import entities.Recibo;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.apache.commons.lang3.StringUtils;
import utils.Alerta;
import utils.ProductoEnFactura;
import utils.Validar;

import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ResourceBundle;

public class RecibosController implements Initializable {

    private final RecibosFacade facade;
    private Recibo reciboSeleccionado;
    private float baseImponibleTemporal = 0f;
    private float totalReciboTemporal = 0f;

    @FXML private TextField numeroDeRecibo;
    @FXML private ComboBox<Proveedor> proveedor;
    @FXML private ComboBox<Producto> producto;
    @FXML private TextField cantidadProducto;
    @FXML private TextField precioCompra;
    @FXML private TableView<ProductoEnFactura> tablaInsertarProductos;
    @FXML private TextField baseImponible;
    @FXML private TextField impuestosBase;
    @FXML private TextField impuestosTotal;
    @FXML private TextField total;
    @FXML private Button botonEmitir;
    @FXML private TextField buscarNumeroRecibo;
    @FXML private ComboBox<Proveedor> buscarProveedor;
    @FXML private TableView<Recibo> tablaBuscarRecibos;
    @FXML private TextArea infoRecibo;

    public RecibosController() {
        super();
        facade = new RecibosFacadeImpl();
    }

    /**
     * Valores de clientes en la pestaña de facturas
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Se recoge la lista de clientes y se inserta en el ComboBox de facturación y se muestra el primero por defecto
        ObservableList<Proveedor> proveedores = facade.obtenerListaProveedores();
        proveedor.setItems(proveedores);
        buscarProveedor.setItems(proveedores);

        // Se recoge la lista de productos y se inserta en el ComboBox de facturación
        ObservableList<Producto> productos = facade.obtenerListaProductos();
        producto.setItems(productos);

        // Añadir Listener a la tabla de facturas buscadas para mostrar los detalles de la factura seleccionada
        tablaBuscarRecibos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                reciboSeleccionado = observable.getValue();
                infoRecibo.setText(facade.detallesRecibo(reciboSeleccionado));
            }
        });
    }

    @FXML
    public void setProductoEnLista() {
        if(producto.getValue() == null) {
            new Alerta("WARNING","Error en la introducción de productos","No ha seleccionado ningún producto");
            return;
        }

        if (!Validar.esEntero(cantidadProducto.getText())) {
            new Alerta("WARNING", "Error en la introducción de productos", "No se ha introducido la cantidad correctamente");
            return;
        }

        if (!Validar.esDecimal(precioCompra.getText())) {
            new Alerta("WARNING", "Error en la introducción de productos", "Tiene que introducir el precio en dígitos y separando los decimales con un punto.");
            return;
        }

        Producto productoARecibir = producto.getValue();

        // Recogida de datos del formulario
        Integer id = productoARecibir.getId();
        String nombre = productoARecibir.getNombre();
        String marca = productoARecibir.getMarca();
        Integer unidades = Integer.valueOf(cantidadProducto.getText());
        float precio = Float.valueOf(precioCompra.getText());

        // Sumar productos a base imponible, calcular impuestos y total
        baseImponibleTemporal = baseImponibleTemporal + unidades * precio;
        float impuestosCantidadNueva = baseImponibleTemporal * 0.07f;
        totalReciboTemporal = baseImponibleTemporal + impuestosCantidadNueva;

        // Formateo de la salida y adición a la tabla de productos
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        String precioEuros = nf.format(precio);
        ProductoEnFactura productoEnFactura = new ProductoEnFactura(id, nombre, marca, unidades, precioEuros);
        tablaInsertarProductos.getItems().add(productoEnFactura);
        baseImponible.setText(nf.format(baseImponibleTemporal));
        impuestosTotal.setText(nf.format(impuestosCantidadNueva));
        total.setText(nf.format(totalReciboTemporal));

        // Limpiar producto seleccionado y unidades
        producto.setValue(null);
        cantidadProducto.clear();
        precioCompra.clear();

        // Habilitar emisión e impresión de facturas
        botonEmitir.setDisable(false);
    }

    @FXML
    public void limpiarRecibo() {
        // Borrado de campos del formulario
        numeroDeRecibo.clear();
        proveedor.getSelectionModel().clearSelection();
        producto.getSelectionModel().clearSelection();
        cantidadProducto.clear();
        tablaInsertarProductos.getItems().clear();
        baseImponible.clear();
        impuestosTotal.clear();
        total.clear();

        // Deshabilitar emisión e impresión de facturas sin contenido
        botonEmitir.setDisable(true);

        // Reiniciar las variables temporales
        baseImponibleTemporal = 0f;
        totalReciboTemporal = 0f;
    }

    @FXML
    public void emitirRecibo() throws SQLException {

        if (StringUtils.isBlank(numeroDeRecibo.getText())) {
            new Alerta("WARNING", "Error en la emisi\u00f3n del recibo", "Tiene que indicar el n\u00famero del recibo.");
            return;
        }

        if (proveedor.getSelectionModel().isEmpty()) {
            new Alerta("WARNING", "Error en la emisi\u00f3n del recibo", "Tiene que indicar el proveedor del recibo.");
            return;
        }

        facade.emitirRecibo(numeroDeRecibo.getText(),
                            proveedor.getValue(),
                            tablaInsertarProductos.getItems(),
                            totalReciboTemporal);

        botonEmitir.setDisable(true);
    }

    @FXML
    public void buscarRecibos() {
        int campoBusqueda = 0;
        String numeroDeReciboBuscado = null;
        Proveedor proveedorBuscado = null;

        if (StringUtils.isNotBlank(buscarNumeroRecibo.getText())) {
            numeroDeReciboBuscado = buscarNumeroRecibo.getText();
            campoBusqueda++;
        }

        if (buscarProveedor.getValue() != null) {
            proveedorBuscado = buscarProveedor.getValue();
            campoBusqueda++;
        }

        if (campoBusqueda != 1) {
            new Alerta("WARNING", "B\u00fasqueda de facturas", "Debe indicar un campo para realizar la b\u00fasqueda y solo se admite uno.");
            return;
        }

        ObservableList<Recibo> recibosBuscados = facade.buscarRecibo(numeroDeReciboBuscado, proveedorBuscado);

        if (recibosBuscados.isEmpty()) {
            new Alerta("INFORMATION", "B\u00fasqueda de facturas", "No se ha encontrado ninguna factura con el par\u00e1metro indicado");
            return;
        }

        tablaBuscarRecibos.setItems(recibosBuscados);
    }

    @FXML
    public void limpiarBusquedaRecibos() {
        buscarNumeroRecibo.clear();
        buscarProveedor.getSelectionModel().clearSelection();
        tablaBuscarRecibos.getItems().clear();
        infoRecibo.clear();
    }
}
