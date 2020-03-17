package daos;

import entities.Cliente;
import entities.Factura;
import entities.Proveedor;
import entities.Recibo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReciboDao extends CommonDao<Recibo> {

    private static final Logger logger = LogManager.getLogger(ReciboDao.class);
    private static ReciboDao instance = null;

    /**
     * Constructor Singleton para evitar la repetición de instancias
     */
    protected ReciboDao() {
        super(Recibo.class);
    }

    public static ReciboDao getInstance() {
        if (instance == null) {
            instance = new ReciboDao();
        }
        return instance;
    }

    /**
     * Obtiene la lista de recibos
     * @return List
     */
    public List<Recibo> obtenerRecibos() {
        String query = "SELECT r FROM entities.Recibo r";
        Map<String, Object> parameters = new HashMap<String, Object>();
        try {
            return list(query, parameters);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ArrayList<Recibo>();
        }
    }

    /**
     * Inserta un recibo en la base de datos
     * @param recibo
     * @return Entity
     */
    public Recibo insertarRecibo(Recibo recibo) {
        try {
            return insert(recibo);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * Devuelve los recibos por según su número
     * @param numeroRecibo
     * @return List
     */
    public List<Recibo> buscarReciboPorNumero(String numeroRecibo) {
        String query = "SELECT r FROM entities.Recibo r WHERE numeroRecibo = :NUMERO_RECIBO";
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("NUMERO_RECIBO", numeroRecibo);
        try {
            return list(query, parameters);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * Devuelve los recibos según su proveedor
     * @param proveedorBuscado
     * @return List
     */
    public List<Recibo> buscarRecibosPorProveedor(Proveedor proveedorBuscado) {
        String query = "select r from entities.Recibo r where idProveedor = :PROVEEDOR";
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("PROVEEDOR", proveedorBuscado);
        try {
            return list(query, parameters);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}
