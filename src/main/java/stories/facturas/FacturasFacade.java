package stories.facturas;

import entities.Cliente;
import entities.Factura;
import entities.Producto;
import javafx.collections.ObservableList;
import utils.ProductoEnFactura;

import java.io.IOException;
import java.sql.SQLException;

public interface FacturasFacade {

    ObservableList<Cliente> obtenerListaClientes();

    ObservableList<Producto> obtenerListaProductos();

    String obtenerNumeroSiguienteFactura();

    void emitirFactura(String numeroFactura,
                       Cliente cliente,
                       ObservableList<ProductoEnFactura> productosEnFactura,
                       float totalFactura) throws SQLException;

    void imprimirFactura(String numeroFactura) throws IOException, SQLException;

    ObservableList<Factura> buscarFactura(String numeroFacturaBuscado,
                                          Cliente clienteBuscado);

    String detallesFactura(Factura factura);
}
