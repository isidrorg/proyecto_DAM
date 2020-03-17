package stories.usuario;

import entities.Usuario;

import java.sql.SQLException;

public interface UsuarioFacade {

    boolean modificarUsuario(Integer id,
                             String numeroIdentificacion,
                             String nombre,
                             String apellidos,
                             String calle,
                             String numeroCalle,
                             String piso,
                             String puerta,
                             String municipio,
                             String provincia,
                             String codigoPostal,
                             String telefonoMovil,
                             String telefonoFijo,
                             String email,
                             String cuentaBancaria) throws SQLException;


    boolean modificarContrasenya(Integer id,
                                 String contrasenyaAntigua,
                                 String contrasenyaNueva,
                                 String contrasenyaNuevaConfirmacion) throws SQLException;
}
