package daos;

import entities.Factura;
import entities.FacturaProductos;
import entities.Producto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FacturaProductosDao extends CommonDao<FacturaProductos> {

    private static final Logger logger = LogManager.getLogger(FacturaProductosDao.class);
    private static FacturaProductosDao instance = null;

    /**
     * Constructor Singleton para evitar la repetición de instancias
     */
    protected FacturaProductosDao() {
        super(FacturaProductos.class);
    }

    public static FacturaProductosDao getInstance() {
        if (instance == null) {
            instance = new FacturaProductosDao();
        }
        return instance;
    }

    /**
     * Inserta la relación de la venta de un producto (y sus propiedades) con su factura
     * @param facturaProductos
     * @return Entity
     */
    public FacturaProductos insertarProductosEnFactura(FacturaProductos facturaProductos) {
        try {
            return insert(facturaProductos);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * Devuelve la lista de productos relacionados con una factura
     * @param facturaBuscada
     * @return List
     */
    public List<FacturaProductos> obtenerProductosDeFactura(Factura facturaBuscada) {
        String query = "select fp from entities.FacturaProductos fp where idFactura = :FACTURA";
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("FACTURA", facturaBuscada);
        try {
            return list(query, parameters);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * Devuelve una lista de facturas que contienen el producto buscado
     * @param productoBuscado
     * @return List
     */
    public List<FacturaProductos> obtenerFacturasConProducto(Producto productoBuscado) {
        String query = "select fp from entities.FacturaProductos fp where idProducto = :PRODUCTO";
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("PRODUCTO", productoBuscado);
        try {
            return list(query, parameters);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}
