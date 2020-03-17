package stories.recibos;

import daos.ProductoDao;
import daos.ProveedorDao;
import daos.ReciboDao;
import daos.ReciboProductosDao;
import entities.Producto;
import entities.Proveedor;
import entities.Recibo;
import entities.ReciboProductos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.Alerta;
import utils.ProductoEnFactura;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

public class RecibosFacadeImpl implements RecibosFacade {

    private static final Logger logger = LogManager.getLogger(RecibosFacadeImpl.class);

    /**
     * Devuelve la lista de todos los proveedores
     * @return ObservableList
     */
    @Override
    public ObservableList<Proveedor> obtenerListaProveedores() {
        List<Proveedor> proveedores = ProveedorDao.getInstance().obtenerProveedores();
        return FXCollections.observableArrayList(proveedores);
    }

    /**
     * Devuelve la lista de todos los productos
     * @return ObservableList
     */
    @Override
    public ObservableList<Producto> obtenerListaProductos() {
        List<Producto> productos = ProductoDao.getInstance().obtenerProductos();
        return FXCollections.observableArrayList(productos);
    }

    /**
     * Inserta el recibo en la base de datos
     * @param numeroRecibo
     * @param proveedor
     * @param productosEnFactura
     * @param totalRecibo
     * @throws SQLException
     */
    @Override
    public void emitirRecibo(String numeroRecibo,
                             Proveedor proveedor,
                             ObservableList<ProductoEnFactura> productosEnFactura,
                             float totalRecibo) throws SQLException {

        // Creación del recibo sin productos para añadirla a la base de datos
        Recibo recibo = new Recibo();

        recibo.setNumeroRecibo(numeroRecibo);
        recibo.setIdProveedor(proveedor);
        recibo.setImporte(totalRecibo);
        recibo.setFecha(new Date());

        Recibo reciboInsertado = ReciboDao.getInstance().insertarRecibo(recibo);
        if (reciboInsertado == null) {
            new Alerta("ERROR", "Error en la emisi\u00f3n del recibo.", "No se ha posido crear el recibo en la base de datos.");
            return;
        }

        // Recogida del recibo añadido a la base de datos
        List<Recibo> reciboBuscadoPorNumero = ReciboDao.getInstance().buscarReciboPorNumero(numeroRecibo);
        if (reciboBuscadoPorNumero.isEmpty()) {
            new Alerta("ERROR", "Error en la emisi\u00f3n del recibo.", "No se ha posido obtener el recibo desde la base de datos.");
            return;
        }

        // Obtención de la factura desde la base de datos
        Recibo reciboBuscado = reciboBuscadoPorNumero.get(0);

        // Inserción de los productos a la tabla de la relación asociativa
        for (ProductoEnFactura productoEnFactura: productosEnFactura) {
            Producto productoRecibido = ProductoDao.getInstance().get(productoEnFactura.getId());
            if (productoRecibido == null) {
                new Alerta("ERROR", "Error en la emisi\u00f3n de recibo.", "No se ha posido obtener un producto de la base de datos.");
                return;
            }

            ReciboProductos reciboProductos = new ReciboProductos(reciboBuscado,
                                                                  productoRecibido,
                                                                  productoEnFactura.getUnidades(),
                                                                  productoRecibido.getPrecio());

            ReciboProductos fp = ReciboProductosDao.getInstance().insertarProductosEnRecibo(reciboProductos);

            if (fp == null || productoRecibido.getCantidad() < productoEnFactura.getUnidades()) {
                new Alerta("ERROR",
                        "Error en la emisi\u00f3n del recibo.",
                        "No se ha posido registrar el producto " + productoRecibido.getNombre() + " en el recibo.");
                return;
            }

            // Se añade la cantidad de productos recibidos al almacén
            Integer productosRestantes = productoRecibido.getCantidad() + productoEnFactura.getUnidades();
            productoRecibido.setCantidad(productosRestantes);
        }

        new Alerta("INFORMATION", "Emisi\u00f3n de recibo.", "Recibo emitido correctamente.");
    }

    /**
     * Devuelve una lista de recibos buscados por su número o por su proveedor
     * @param numeroReciboBuscado
     * @param proveedorBuscado
     * @return ObservableList
     */
    @Override
    public ObservableList<Recibo> buscarRecibo(String numeroReciboBuscado, Proveedor proveedorBuscado) {
        List<Recibo> recibosEncontrados = null;

        if (StringUtils.isNotBlank(numeroReciboBuscado)) {
            recibosEncontrados = ReciboDao.getInstance().buscarReciboPorNumero(numeroReciboBuscado);
        }

        if (proveedorBuscado != null) {
            recibosEncontrados = ReciboDao.getInstance().buscarRecibosPorProveedor(proveedorBuscado);
        }

        return FXCollections.observableArrayList(recibosEncontrados);
    }

    /**
     * Devuelve la información del recibo
     * @param recibo
     * @return String
     */
    @Override
    public String detallesRecibo(Recibo recibo) {

        Proveedor proveedor = recibo.getIdProveedor();
        List<ReciboProductos> reciboProductos = ReciboProductosDao.getInstance().obtenerProductosDeRecibo(recibo);

        StringBuffer detalleRecibo = new StringBuffer(
                "Recibo Nº:          " + recibo.getNumeroRecibo() + "\n" +
                "Fecha de recepci\u00f3n: " + recibo.getFecha() + "\n"
        );


        detalleRecibo.append("Proveedor:          " );

        if (!proveedor.getApellidos().isEmpty()) detalleRecibo.append(proveedor.getApellidos() + ", ");

        detalleRecibo.append(proveedor.getNombre() + ", " + proveedor.getnumeroIdentificacion() + "\n");

        if (reciboProductos == null) {
            new Alerta("ERROR", "Error en la obtenci\u00f3n de datos", "No se ha podido obtener los productos del recibo.");
            return "";
        }

        detalleRecibo.append("--------------------------------------------------------------------------------\n");

        for (ReciboProductos rp: reciboProductos) {
            Producto producto = rp.getProducto();
            detalleRecibo.append(
                String.format("%-54s", producto.getNombre()) +
                String.format("%7s", rp.getCantidad()) + " uds" +
                String.format("%10s", rp.getPrecioOfertado()) + " €/ud" + "\n"
            );
        }

        detalleRecibo.append("--------------------------------------------------------------------------------\n");

        NumberFormat nf = NumberFormat.getCurrencyInstance();
        float total = recibo.getImporte();

        String baseImponibleEnEuros = nf.format(total / 1.07f) + " ";
        String impuestosEnEuros = nf.format(total * 0.07f / 1.07f) + " ";
        String totalEnEuros = nf.format(total) + " ";

        detalleRecibo.append(
                "Base imponible:       " + String.format("%10s", baseImponibleEnEuros) + "\n" +
                "Impuestos:            " + String.format("%10s", impuestosEnEuros) + "\n" +
                "Importe total:        " + String.format("%10s", totalEnEuros) + "\n"
        );

        return String.valueOf(detalleRecibo);
    }
}
