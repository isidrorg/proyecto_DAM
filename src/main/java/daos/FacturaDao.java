package daos;

import entities.Cliente;
import entities.Factura;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.*;

public class FacturaDao extends CommonDao<Factura> {

    private static final Logger logger = LogManager.getLogger(FacturaDao.class);
    private static FacturaDao instance = null;

    /**
     * Constructor Singleton para evitar la repetición de instancias
     */
    protected FacturaDao() {
        super(Factura.class);
    }

    public static FacturaDao getInstance() {
        if (instance == null) {
            instance = new FacturaDao();
        }
        return instance;
    }

    /**
     * Devuelve una lista de todas las facturas registradas
     * @return List
     */
    public List<Factura> obtenerFacturas() {
        String query = "SELECT f FROM entities.Factura f";
        Map<String, Object> parameters = new HashMap<String, Object>();
        try {
            return list(query, parameters);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ArrayList<Factura>();
        }
    }

    /**
     * Inserta una factura en la base de datos
     * @param factura
     * @return Entity
     */
    public Factura insertarFactura(Factura factura) {
        try {
            return insert(factura);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * Devuelve la factura buscada por su número
     * Se ha indicado que devuelva una lista por el método con el que trabajará
     * @param numeroFactura
     * @return List
     */
    public List<Factura> buscarFacturaPorNumero(String numeroFactura) {
        String query = "SELECT f FROM entities.Factura f WHERE numeroFactura = :NUMERO_FACTURA";
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("NUMERO_FACTURA", numeroFactura);
        try {
            return list(query, parameters);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * Devuelve las facturas expedidas a un clietne
     * @param clienteBuscado
     * @return List
     */
    public List<Factura> buscarFacturasPorCliente(Cliente clienteBuscado) {
        String query = "select f from entities.Factura f where idCliente = :CLIENTE";
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("CLIENTE", clienteBuscado);
        try {
            return list(query, parameters);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}
