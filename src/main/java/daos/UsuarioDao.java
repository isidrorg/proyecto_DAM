package daos;

import entities.Usuario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.*;

public class UsuarioDao extends CommonDao<Usuario> {

    private static final Logger logger = LogManager.getLogger(UsuarioDao.class);
    private static UsuarioDao instance = null;

    /**
     * Constructor Singleton para evitar la repetición de instancias
     */
    protected UsuarioDao() {
        super(Usuario.class);
    }

    public static UsuarioDao getInstance() {
        if (instance == null) {
            instance = new UsuarioDao();
        }
        return instance;
    }

    /**
     * Obtención de usuario por su login
     * @param usuario
     * @return List
     */
    public Optional<Usuario> obtenerPorUsuario(String usuario) {
        String query = "select u from entities.Usuario u where u.usuario = :USUARIO";
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("USUARIO", usuario);
        try {
            return find(query, parameters);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    /**
     * Devuelve la lista de usuarios en base de datos
     * @return List
     */
    public List<Usuario> obtenerUsuarios() {
        String query = "select u from entities.Usuario u";
        Map<String, Object> parameters = new HashMap<String, Object>();
        try {
            return list(query, parameters);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ArrayList<Usuario>();
        }
    }

    /**
     * Devuelve el usuario que ha iniciado sesión
     * @return Optional
     */
    public Optional<Usuario> obtenerUsuarioPorSesion() {
        String query = "select u from entities.Usuario u where u.sesion = true";
        Map<String, Object> parameters = new HashMap<String, Object>();
        try {
            return find(query, parameters);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return Optional.empty();
        }
    }
}
