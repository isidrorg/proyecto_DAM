package stories.productos;

import entities.Producto;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import utils.Alerta;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ProductosController implements Initializable {

    private final ProductosFacade facade;
    Producto productoSeleccionado;

    @FXML private TextField codigoBarras;
    @FXML private TextField marca;
    @FXML private TextField nombre;
    @FXML private TextField categoria;
    @FXML private TextArea descripcion;
    @FXML private TextField precioVenta;
    @FXML private Button botonModificar;
    @FXML private Button botonRegistrar;
    @FXML private ComboBox<String> buscarCategoria;
    @FXML private ComboBox<String> buscarMarca;
    @FXML private TableView<Producto> tablaBuscarProducto;
    @FXML private TextArea infoProducto;
    @FXML private Button botonEditar;
    @FXML private Button botonEliminar;


    public ProductosController() {
        super();
        facade = new ProductosFacadeImpl();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Obtener lista de categorías para añadir en el ComboBox de búsqueda
        ObservableList<String> categorias = facade.obtenerCategorias();
        buscarCategoria.setItems(categorias);

        // Obtener lista de marcas para añadir en el ComboBox de búsqueda
        ObservableList<String> marcas = facade.obtenerMarcas();
        buscarMarca.setItems(marcas);

        // Añadir Listener a la tabla de productos buscados para mostrar los detalles del producto seleccionado
        tablaBuscarProducto.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                productoSeleccionado = observable.getValue();
                infoProducto.setText(productoSeleccionado.detalles());
                botonEditar.setDisable(false);
                botonEliminar.setDisable(false);
            }
        });
    }

    @FXML
    public void limpiarProducto() {
        codigoBarras.clear();
        marca.clear();
        nombre.clear();
        categoria.clear();
        descripcion.clear();
        precioVenta.clear();
        botonModificar.setDisable(true);
        botonRegistrar.setDisable(false);
    }

    @FXML
    public void modificarProducto() throws SQLException {
        if (facade.modificarProducto(
            productoSeleccionado.getId(),
            codigoBarras.getText(),
            nombre.getText(),
            marca.getText(),
            categoria.getText(),
            descripcion.getText(),
            precioVenta.getText()
        )) {
            new Alerta("INFORMATION", "Modificaci\u00f3n de producto", "Modificaci\u00f3n del producto realizada con \u00e9xito.");
        } else {
            new Alerta("ERROR", "Modificaci\u00f3n de producto", "Fallo en la modificaci\u00f3n del producto.");
        }
    }

    @FXML
    public void registrarProducto() {
        facade.registrarProducto(
            codigoBarras.getText(),
            nombre.getText(),
            marca.getText(),
            categoria.getText(),
            descripcion.getText(),
            precioVenta.getText()
        );
    }

    @FXML
    public void buscarProducto() {
        Object categoria = buscarCategoria.getValue();
        Object marca = buscarMarca.getValue();

        // Comprobar que al menos se pasa un parámetro
        if (categoria == null && marca == null) {
            new Alerta("WARNING","Error en la b\u00fasqueda del producto","Tiene que indicar al menos la marca o la categor\u00eda.");
            return;
        }

        // Obtención de la lista de productos y seteo a la tabla
        ObservableList<Producto> productosEncontrados = facade.buscarProductos(categoria, marca);
        tablaBuscarProducto.setItems(productosEncontrados);
    }

    @FXML
    public void limpiarBuscarProducto() {
        buscarCategoria.getSelectionModel().clearSelection();
        buscarMarca.getSelectionModel().clearSelection();
        tablaBuscarProducto.getItems().clear();
        infoProducto.clear();
        botonEditar.setDisable(true);
        botonEliminar.setDisable(true);
    }

    @FXML
    public void editarProducto() {
        codigoBarras.setText(productoSeleccionado.getCodigoBarras());
        marca.setText(productoSeleccionado.getMarca());
        nombre.setText(productoSeleccionado.getNombre());
        categoria.setText(productoSeleccionado.getCategoria());
        descripcion.setText(productoSeleccionado.getDescripcion());
        precioVenta.setText(String.valueOf(productoSeleccionado.getPrecio()));
        botonModificar.setDisable(false);
        botonRegistrar.setDisable(true);
    }

    @FXML
    public void eliminarProducto() {
        if (facade.confirmarBorrado()) {
            facade.eliminarProducto(productoSeleccionado);
        }
    }
}
