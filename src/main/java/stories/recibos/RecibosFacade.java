package stories.recibos;

import entities.Producto;
import entities.Proveedor;
import entities.Recibo;
import javafx.collections.ObservableList;
import utils.ProductoEnFactura;

import java.sql.SQLException;

public interface RecibosFacade {

    ObservableList<Proveedor> obtenerListaProveedores();

    ObservableList<Producto> obtenerListaProductos();

    void emitirRecibo(String numeroRecibo,
                      Proveedor proveedor,
                      ObservableList<ProductoEnFactura> productosEnFactura,
                      float totalRecibo) throws SQLException;

    ObservableList<Recibo> buscarRecibo(String numeroReciboBuscado,
                                        Proveedor proveedorBuscado);

    String detallesRecibo(Recibo recibo);
}
