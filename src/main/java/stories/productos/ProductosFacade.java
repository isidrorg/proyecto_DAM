package stories.productos;

import entities.Producto;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public interface ProductosFacade {
    boolean modificarProducto(Integer id,
                              String codigoBarras,
                              String nombre,
                              String marca,
                              String categoria,
                              String descripcion,
                              String precioVenta) throws SQLException;

    void registrarProducto(String codigoBarras,
                           String nombre,
                           String marca,
                           String categoria,
                           String descripcion,
                           String precioVenta);

    ObservableList<String> obtenerCategorias();

    ObservableList<String> obtenerMarcas();

    ObservableList<Producto> buscarProductos(Object categoria,
                                             Object marca);

    void eliminarProducto(Producto producto);

    boolean confirmarBorrado();
}
