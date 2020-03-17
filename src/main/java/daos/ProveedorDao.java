package daos;

import entities.Cliente;
import entities.Proveedor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProveedorDao extends CommonDao<Proveedor> {

    private static final Logger logger = LogManager.getLogger(ProveedorDao.class);
    private static ProveedorDao instance = null;

    /**
     * Constructor Singleton para evitar la repetición de instancias
     */
    protected ProveedorDao() {
        super(Proveedor.class);
    }

    public static ProveedorDao getInstance() {
        if (instance == null) {
            instance = new ProveedorDao();
        }
        return instance;
    }

    /**
     * Obtención de la lista de proveedores
     * @return List
     */
    public List<Proveedor> obtenerProveedores() {
        String query = "SELECT p FROM Proveedor p";
        Map<String, Object> parameters = new HashMap<String, Object>();
        try {
            return list(query, parameters);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ArrayList<Proveedor>();
        }
    }

    /**
     * Obtención de lista de proveedores por nombre
     * @param nombre
     * @return List
     */
    public List<Proveedor> obtenerProveedoresPorNombre(String nombre) {
        String query = "select c from entities.Proveedor c where c.nombre = :NOMBRE";
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("NOMBRE", nombre);
        try {
            return list(query, parameters);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ArrayList<Proveedor>();
        }
    }

    /**
     * Obtención de proveedor por Nif/Cif/Nie
     * @param nif
     * @return Entity
     */
    public List<Proveedor> obtenerProveedorPorNif(String nif) {
        String query = "select c from entities.Proveedor c where c.numeroIdentificacion = :NIF";
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("NIF", nif);
        try {
            return list(query, parameters);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ArrayList<Proveedor>();
        }
    }

    /**
     * Obtención de proveedores por código postal
     * @param codigoPostal
     * @return List
     */
    public List<Proveedor> obtenerProveedoresPorCP(Integer codigoPostal) {
        String query = "select c from entities.Proveedor c where c.codigoPostal = :CP";
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("CP", codigoPostal);
        try {
            return list(query, parameters);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ArrayList<Proveedor>();
        }
    }

    /**
     * Inserción de proveedor en la base de datos
     * @param proveedor
     * @return Entity
     */
    public Proveedor altaProveedor(Proveedor proveedor) {
        try {
            return insert(proveedor);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * Eliminación de proveedor de la base de datos
     * @param proveedor
     * @return true/false
     */
    public boolean borrarProveedor(Proveedor proveedor) {
        try {
            return remove(proveedor);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }
}
