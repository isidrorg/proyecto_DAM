package daos;

import entities.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReciboProductosDao extends CommonDao<ReciboProductos> {

    private static final Logger logger = LogManager.getLogger(ReciboProductosDao.class);
    private static ReciboProductosDao instance = null;

    /**
     * Constructor Singleton para evitar la repetición de instancias
     */
    protected ReciboProductosDao() {
        super(ReciboProductos.class);
    }

    public static ReciboProductosDao getInstance() {
        if (instance == null) {
            instance = new ReciboProductosDao();
        }
        return instance;
    }

    /**
     * Inserta la relación de la compra de un producto (y sus propiedades) con su recibo
     * @param reciboProductos
     * @return Entity
     */
    public ReciboProductos insertarProductosEnRecibo(ReciboProductos reciboProductos) {
        try {
            return insert(reciboProductos);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * Devuelve la lista de productos (y sus propiedades) relacionados a un recibo
     * @param reciboBuscado
     * @return List
     */
    public List<ReciboProductos> obtenerProductosDeRecibo(Recibo reciboBuscado) {
        String query = "select rp from entities.ReciboProductos rp where idRecibo = :RECIBO";
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("RECIBO", reciboBuscado);
        try {
            return list(query, parameters);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * Devuelve una lista de recibos que contienen el producto buscado
     * @param productoBuscado
     * @return List
     */
    public List<ReciboProductos> obtenerRecibosConProducto(Producto productoBuscado) {
        String query = "select fp from entities.ReciboProductos fp where idProducto = :PRODUCTO";
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
