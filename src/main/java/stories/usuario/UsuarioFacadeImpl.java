package stories.usuario;

import daos.UsuarioDao;
import entities.Usuario;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.Alerta;
import utils.Validar;
import utils.ValidarNifCif;

import java.sql.SQLException;

public class UsuarioFacadeImpl implements UsuarioFacade {

    private static final Logger logger = LogManager.getLogger(UsuarioFacadeImpl.class);

    /**
     * Devuelve un mensaje de error si en el formularios hay campos que no cumplen
     * con las condiciones exigidas
     * @param numeroIdentificacion
     * @param nombre
     * @param apellidos
     * @param calle
     * @param numeroCalle
     * @param piso
     * @param puerta
     * @param codigoPostal
     * @param municipio
     * @param provincia
     * @param telefonoMovil
     * @param telefonoFijo
     * @param email
     * @param cuentaBancaria
     * @return String
     */
    private String mensajeErrorValidarModificacion(String numeroIdentificacion,
                                                   String nombre,
                                                   String apellidos,
                                                   String calle,
                                                   String numeroCalle,
                                                   String piso,
                                                   String puerta,
                                                   String codigoPostal,
                                                   String municipio,
                                                   String provincia,
                                                   String telefonoMovil,
                                                   String telefonoFijo,
                                                   String email,
                                                   String cuentaBancaria) {
        StringBuffer mensajeError = new StringBuffer("");

        if (StringUtils.isBlank(numeroIdentificacion)) {
            mensajeError.append("Se requiere introducir el NIF/CIF/NIE de la persona f\u00edsica o jur\u00edcidica.\n\n");
        } else if (numeroIdentificacion.length() > 9) {
            mensajeError.append("El NIF/CIF/NIE no puede exceder los 9 caracteres.\n\n");
        } else if (!ValidarNifCif.isvalido(numeroIdentificacion)) {
            mensajeError.append("El NIF/CIF/NIE introducido no es v\u00e1lido.\n\n");
        }

        if (StringUtils.isBlank(nombre)) {
            mensajeError.append("Se requiere introducir el nombre.\n\n");
        } else if (nombre.length() > 60) {
            mensajeError.append("El nombre no puede exceder los 60 caracteres.\n\n");
        }

        if (StringUtils.isBlank(apellidos)) {
            mensajeError.append("Se requiere introducir el/los apellidos.\n\n");
        } else if (apellidos.length() > 120) {
            mensajeError.append("El/los apellidos no pueden exceder los 120 caracteres.\n\n");
        }

        if (calle.length() > 255) {
            mensajeError.append("La direcci\u00f3n no puede exceder los 255 caracteres.\n\n");
        }

        if (StringUtils.isNotBlank(numeroCalle)) {
            if (!Validar.esEntero(numeroCalle)) {
                mensajeError.append("El n\u00famero de la direcci\u00f3n tiene que ser un n\u00famero entero.\n\n");
            }
        }

        if (StringUtils.isNotBlank(piso)) {
            if (!Validar.esEntero(piso)) {
                mensajeError.append("El n\u00famero del piso tiene que ser un n\u00famero entero.\n\n");
            }
        }

        if (puerta.length() > 10) {
            mensajeError.append("El identificador de la puerta no puede exceder los 10 caracteres.\n\n");
        }

        if (municipio.length() > 255) {
            mensajeError.append("El nombre del municipio no puede exceder los 255 caracteres.\n\n");
        }

        if (provincia.length() > 60) {
            mensajeError.append("El nombre de la provincia no puede exceder los 60 caracteres.\n\n");
        }

        if (StringUtils.isNotBlank(codigoPostal)) {
            if (codigoPostal.length() != 5 && !Validar.esEntero(codigoPostal)) {
                mensajeError.append("El C\u00f3digo Postal tiene que contener 5 d\u00edgitos.");
            }
        }

        if (telefonoMovil.length() > 20) {
            mensajeError.append("El n\u00famero del m\u00f3vil no puede exceder los 20 caracteres.\n\n");
        }

        if (telefonoFijo.length() > 20) {
            mensajeError.append("El n\u00famero del tel\u00e9fono fijo no puede exceder los 20 caracteres.\n\n");
        }

        if (StringUtils.isNotBlank(email)) {
            if (email.length() > 255) {
                mensajeError.append("El correo electr\u00f3nico no puede exceder los 255 caracteres.\n\n");
            } else if (!Validar.esEmail(email)) {
                mensajeError.append("El correo electr\u00f3nico introducido no es v\u00e1lido.");
            }
        }

        if (StringUtils.isNotBlank(cuentaBancaria)) {
            if (cuentaBancaria.length() > 24) {
                mensajeError.append("La cuenta bancaria no puede exceder los 24 caracteres.\n\n");
            } else if(!Validar.esCuentaBancaria(cuentaBancaria)) {
                mensajeError.append("La cuenta bancaria no est\u00e1 en formato IBAN.\n\n");
            }
        }
        return mensajeError.toString();
    }

    /**
     * Modifica los datos del usuario en la base de datos
     * @param id
     * @param numeroIdentificacion
     * @param nombre
     * @param apellidos
     * @param calle
     * @param numeroCalleString
     * @param pisoString
     * @param puerta
     * @param municipio
     * @param provincia
     * @param codigoPostalString
     * @param telefonoMovil
     * @param telefonoFijo
     * @param email
     * @param cuentaBancaria
     * @return true/false
     * @throws SQLException
     */
    @Override
    public boolean modificarUsuario(Integer id,
                                    String numeroIdentificacion,
                                    String nombre,
                                    String apellidos,
                                    String calle,
                                    String numeroCalleString,
                                    String pisoString,
                                    String puerta,
                                    String municipio,
                                    String provincia,
                                    String codigoPostalString,
                                    String telefonoMovil,
                                    String telefonoFijo,
                                    String email,
                                    String cuentaBancaria) throws SQLException {
        // Comprobación de la validez del formulario
        String mensajeError = mensajeErrorValidarModificacion(
                numeroIdentificacion, nombre, apellidos, calle, numeroCalleString, pisoString, puerta,
                codigoPostalString, municipio, provincia, telefonoMovil, telefonoFijo, email, cuentaBancaria
        );
        if (StringUtils.isNotBlank(mensajeError)) {
            logger.error(mensajeError);
            new Alerta("WARNING", "Error en la modificaci\u00f3n del usuario", mensajeError);
            return false;
        }

        Usuario usuario = UsuarioDao.getInstance().get(id);

        usuario.setNumeroIdentificacion(numeroIdentificacion);
        usuario.setNombre(nombre);
        usuario.setApellidos(apellidos);
        usuario.setDireccion(calle);
        usuario.setPuerta(puerta);
        usuario.setMunicipio(municipio);
        usuario.setProvincia(provincia);
        usuario.setMovil(telefonoMovil);
        usuario.setTelefono(telefonoFijo);
        usuario.setEmail(email);
        usuario.setCuentaBancaria(cuentaBancaria);

        if (StringUtils.isNotBlank(numeroCalleString)) {
            usuario.setNumeroDireccion(Integer.valueOf(numeroCalleString));
        }

        if (StringUtils.isNotBlank(pisoString)) {
            usuario.setPiso(Integer.valueOf(pisoString));
        }

        if (StringUtils.isNotBlank(codigoPostalString)) {
            usuario.setCodigoPostal(Integer.valueOf(codigoPostalString));
        }

        return UsuarioDao.getInstance().update(usuario);
    }

    /**
     * Modifica la contraseña del usuario
     * @param id
     * @param contrasenyaAntigua
     * @param contrasenyaNueva
     * @param contrasenyaNuevaConfirmacion
     * @return true/false
     * @throws SQLException
     */
    @Override
    public boolean modificarContrasenya(Integer id,
                                        String contrasenyaAntigua,
                                        String contrasenyaNueva,
                                        String contrasenyaNuevaConfirmacion) throws SQLException {

        if (StringUtils.isBlank(contrasenyaAntigua) || StringUtils.isBlank(contrasenyaAntigua) || StringUtils.isBlank(contrasenyaNuevaConfirmacion)) {
            new Alerta("WARNING", "Modificaci\u00f3n de contrase\u00f1a", "Tiene que indicar la contrase\u00f1a antigua, la nueva y confirmarla.");
            return false;
        }

        if (contrasenyaNueva.length() > 30) {
            new Alerta("WARNING", "Modificaci\u00f3n de contrase\u00f1a", "La contrase\u00f1a no puede ser mayor a 30 caracteres.");
            return false;
        }

        if (!contrasenyaNueva.equals(contrasenyaNuevaConfirmacion)) {
            new Alerta("WARNING", "Modificaci\u00f3n de contrase\u00f1a", "La contrase\u00f1a nueva no coincide con su confirmaci\u00f3n.");
            return false;
        }

        Usuario usuario = UsuarioDao.getInstance().get(id);

        if (!contrasenyaAntigua.equals(usuario.getContrasenya())) {
            new Alerta("WARNING", "Modificaci\u00f3n de contrase\u00f1a", "La contrase\u00f1a antigua no coincide.");
            return false;
        }

        usuario.setContrasenya(contrasenyaNueva);

        return UsuarioDao.getInstance().update(usuario);
    }
}
