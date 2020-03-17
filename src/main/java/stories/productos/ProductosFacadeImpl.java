package stories.productos;

import daos.FacturaProductosDao;
import daos.ProductoDao;
import daos.ReciboProductosDao;
import entities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.Alerta;
import utils.Validar;

import java.sql.SQLException;
import java.util.*;

public class ProductosFacadeImpl implements ProductosFacade {
    private static final Logger logger = LogManager.getLogger(ProductosFacadeImpl.class);

    /**
     * Devuelve un mensaje de error en caso de que algún campo no cumpla las condiciones establecidas
     * @param codigoBarras
     * @param nombre
     * @param marca
     * @param categoria
     * @param precioVenta
     * @return String
     */
    private String mensajeErrorValidarRegistro(String codigoBarras,
                                               String nombre,
                                               String marca,
                                               String categoria,
                                               String precioVenta) {
        StringBuffer mensajeError = new StringBuffer("");

        if (StringUtils.isBlank(codigoBarras)) {
            mensajeError.append("Se requiere introducir el c\u00f3digo de barras\n\n");
        } else if (codigoBarras.length() > 16) {
            mensajeError.append("El c\u00f3digo de barras no puede exceder los 16 caracteres\n\n");
        }

        if (StringUtils.isBlank(nombre)) {
            mensajeError.append("Se requiere introducir el nombre\n\n");
        } else if (nombre.length() > 120) {
            mensajeError.append("El nombre del producto no puede exceder los 120 caracteres\n\n");
        }

        if (StringUtils.isBlank(marca)) {
            mensajeError.append("Se requiere introducir la marca\n\n");
        } else if (marca.length() > 30) {
            mensajeError.append("La marca del producto no puede exceder los 30 caracteres\n\n");
        }

        if (StringUtils.isBlank(categoria)) {
            mensajeError.append("Se requiere introducir la categor\u00eda\n\n");
        } else if (categoria.length() > 60) {
            mensajeError.append("La categor\u00eda del producto no puede exceder los 30 caracteres\n\n");
        }

        if (StringUtils.isBlank(precioVenta)) {
            mensajeError.append("Se requiere introducir el precio al que se va a vender el producto\n\n");
        } else if (!Validar.esDecimal(precioVenta)) {
            mensajeError.append("El precio del producto debe indicarse con un número separando los decimales con un punto\n\n");
        }

        return mensajeError.toString();
    }

    /**
     * Modifica el producto seleccionado
     * @param id
     * @param codigoBarras
     * @param nombre
     * @param marca
     * @param categoria
     * @param descripcion
     * @param precioVenta
     * @return true/false
     * @throws SQLException
     */
    @Override
    public boolean modificarProducto(Integer id,
                                     String codigoBarras,
                                     String nombre,
                                     String marca,
                                     String categoria,
                                     String descripcion,
                                     String precioVenta) throws SQLException {
        // Comprobación de la validez del formulario
        String mensajeError = mensajeErrorValidarRegistro(codigoBarras, nombre, marca, categoria, precioVenta);
        if (StringUtils.isNotBlank(mensajeError)) {
            logger.error(mensajeError);
            new Alerta("WARNING", "Error en la modificaci\u00f3n del producto", mensajeError);
            return false;
        }

        Producto producto = ProductoDao.getInstance().get(id);
        producto.setCodigoBarras(codigoBarras);
        producto.setNombre(nombre);
        producto.setMarca(marca);
        producto.setCategoria(categoria);
        producto.setDescripcion(descripcion);
        producto.setPrecio(Float.valueOf(precioVenta));

        return ProductoDao.getInstance().update(producto);
    }

    /**
     * Inserta un producto en la base de datos
     * @param codigoBarras
     * @param nombre
     * @param marca
     * @param categoria
     * @param descripcion
     * @param precioVenta
     */
    @Override
    public void registrarProducto(String codigoBarras,
                                  String nombre,
                                  String marca,
                                  String categoria,
                                  String descripcion,
                                  String precioVenta) {
        // Comprobación de la validez del formulario
        String mensajeError = mensajeErrorValidarRegistro(codigoBarras, nombre, marca, categoria, precioVenta);
        if (StringUtils.isNotBlank(mensajeError)) {
            logger.error(mensajeError);
            new Alerta("WARNING", "Error en el registro del producto", mensajeError);
            return;
        }

        Producto producto = new Producto();

        producto.setCodigoBarras(codigoBarras);
        producto.setNombre(nombre);
        producto.setMarca(marca);
        producto.setCategoria(categoria);
        producto.setDescripcion(descripcion);
        producto.setPrecio(Float.valueOf(precioVenta));
        producto.setCantidad(0);

        ProductoDao.getInstance().registrarProducto(producto);

        new Alerta("INFORMATION", "Registro de producto", "Registro realizado satisfactoriamente.");
    }

    /**
     * Devuelve la lista de categorias utilizadas en los productos
     * @return ObservableList
     */
    @Override
    public ObservableList<String> obtenerCategorias() {
        List<Producto> productos = ProductoDao.getInstance().obtenerProductos();
        // Utilizar un Set para no permitir la adición de contenido duplicado
        Set<String> categorias = new HashSet<String>();

        for (Producto producto: productos) {
            categorias.add(producto.getCategoria());
        }

        return FXCollections.observableArrayList(categorias);
    }

    /**
     * Devuelve la lista de marcas utilizadas en los productos
     * @return
     */
    @Override
    public ObservableList<String> obtenerMarcas() {
        List<Producto> productos = ProductoDao.getInstance().obtenerProductos();
        Set<String> marcas = new HashSet<String>();

        for (Producto producto: productos) {
            marcas.add(producto.getMarca());
        }

        return FXCollections.observableArrayList(marcas);
    }

    /**
     * Devuelve una lista de productos buscados por su categoría y marca
     * @param categoriaObjet
     * @param marcaObject
     * @return ObservableList
     */
    @Override
    public ObservableList<Producto> buscarProductos(Object categoriaObjet, Object marcaObject) {

        String categoriaString = null;
        String marcaString = null;

        if (categoriaObjet != null) categoriaString = categoriaObjet.toString();

        if (marcaObject != null) marcaString = marcaObject.toString();

        List<Producto> productos = ProductoDao.getInstance().buscarProductos(categoriaString, marcaString);

        // Comprobar de que al menos se ha recogido un resultado
        if (productos == null || productos.isEmpty()) {
            new Alerta("INFORMATION","Resultado de la b\u00fasqueda del producto","No se ha encontrado productos que coincidan con\nlos campos indicados.");
            return FXCollections.observableArrayList();
        }

        return FXCollections.observableArrayList(productos);
    }

    /**
     * Elimina un producto de la base de datos
     * No permitirá borrar el producto si ya está relacionado a una factura o recibo
     * @param producto
     */
    @Override
    public void eliminarProducto(Producto producto) {
        List<FacturaProductos> productoEnFacturas = FacturaProductosDao.getInstance().obtenerFacturasConProducto(producto);
        List<ReciboProductos> productoEnRecibos = ReciboProductosDao.getInstance().obtenerRecibosConProducto(producto);

        if (!productoEnFacturas.isEmpty() && !productoEnRecibos.isEmpty()) {
            new Alerta("ERROR", "Error en la eliminaci\u00f3n del producto", "No se puede eliminar el producto porque est\u00e1 \nrelacionado con una factura o recibo.");
            return;
        }

        ProductoDao.getInstance().borrarProducto(producto);
    }

    /**
     * Muestra una alerta solicitando la confirmar el borrado de un producto
     * @return true/false
     */
    @Override
    public boolean confirmarBorrado() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("CONFIRMACI\u00d3N");
        alert.setHeaderText("Solicitud de borrado de producto");
        alert.setContentText("\u00bfEst\u00e1 seguro de que desea borrar el producto?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) return true;

        return false;
    }
}
