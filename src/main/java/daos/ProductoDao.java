package daos;

import entities.Producto;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.*;

public class ProductoDao extends CommonDao<Producto> {

    private static final Logger logger = LogManager.getLogger(ProductoDao.class);
    private static ProductoDao instance = null;

    /**
     * Constructor Singleton para evitar la repetición de instancias
     */
    protected ProductoDao() {
        super(Producto.class);
    }

    public static ProductoDao getInstance() {
        if (instance == null) {
            instance = new ProductoDao();
        }
        return instance;
    }

    /**
     * Obtiene la lista de todos los productos
     * @return List
     */
    public List<Producto> obtenerProductos() {
        String query = "SELECT p FROM Producto p";
        Map<String, Object> parameters = new HashMap<String, Object>();
        try {
            return list(query, parameters);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ArrayList<Producto>();
        }
    }

    /**
     * Inserta un producto en la base de datos
     * @param producto
     * @return Entity
     */
    public Producto registrarProducto(Producto producto) {
        try {
            return insert(producto);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * Busca productos por categoría y/o marca
     * @param categoria
     * @param marca
     * @return List
     */
    public List<Producto> buscarProductos(String categoria, String marca) {
        Map<String, Object> parameters = new HashMap<String, Object>();

        String query = "SELECT p FROM entities.Producto p WHERE ";
        if (StringUtils.isNotBlank(categoria) && StringUtils.isBlank(marca)) {
            query = query + "p.categoria = :CATEGORIA";
            parameters.put("CATEGORIA", categoria);
        }

        if (StringUtils.isBlank(categoria) && StringUtils.isNotBlank(marca)) {
            query = query + "p.marca = :MARCA";
            parameters.put("MARCA", marca);
        }

        if (StringUtils.isNotBlank(categoria) && StringUtils.isNotBlank(marca)) {
            query = query + "p.categoria=:CATEGORIA AND marca=:MARCA";
            parameters.put("CATEGORIA", categoria);
            parameters.put("MARCA", marca);
        }

        try {
            return list(query, parameters);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * Elimina un producto de la base de datos
     * @param producto
     * @return true/false
     */
    public boolean borrarProducto(Producto producto) {
        try {
            return remove(producto);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }
}
