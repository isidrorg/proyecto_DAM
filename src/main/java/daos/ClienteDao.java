package daos;

import entities.Cliente;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClienteDao extends CommonDao<Cliente> {

    private static final Logger logger = LogManager.getLogger(ClienteDao.class);
    private static ClienteDao instance = null;

    /**
     * Constructor Singleton para evitar la repetición de instancias
     */
    protected ClienteDao() {
        super(Cliente.class);
    }

    public static ClienteDao getInstance() {
        if (instance == null) {
            instance = new ClienteDao();
        }
        return instance;
    }

    /**
     * Obtención de la lista de Clientes
     * @return List
     */
    public List<Cliente> obtenerClientes() {
        String query = "SELECT c FROM Cliente c";
        Map<String, Object> parameters = new HashMap<String, Object>();
        try {
            return list(query, parameters);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ArrayList<Cliente>();
        }
    }

    /**
     * Obtención de lista de clientes por nombre
     * @param nombre
     * @return List
     */
    public List<Cliente> obtenerClientesPorNombre(String nombre) {
        String query = "select c from entities.Cliente c where c.nombre = :NOMBRE";
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("NOMBRE", nombre);
        try {
            return list(query, parameters);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ArrayList<Cliente>();
        }
    }

    /**
     * Obtención de cliente por Nif/Cif/Nie
     * @param nif
     * @return Entity
     */
    public List<Cliente> obtenerClientePorNif(String nif) {
        String query = "select c from entities.Cliente c where c.numeroIdentificacion = :NIF";
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("NIF", nif);
        try {
            return list(query, parameters);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ArrayList<Cliente>();
        }
    }

    /**
     * Obtención de clientes por código postal
     * @param codigoPostal
     * @return List
     */
    public List<Cliente> obtenerClientesPorCP(Integer codigoPostal) {
        String query = "select c from entities.Cliente c where c.codigoPostal = :CP";
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("CP", codigoPostal);
        try {
            return list(query, parameters);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ArrayList<Cliente>();
        }
    }

    /**
     * Inserción de cliente en la base de datos
     * @param cliente
     * @return Entity
     */
    public Cliente altaCliente(Cliente cliente) {
        try {
            return insert(cliente);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * Eliminación de cliente de la base de datos
     * @param cliente
     * @return true/false
     */
    public boolean borrarCliente(Cliente cliente) {
        try {
            return remove(cliente);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }
}
