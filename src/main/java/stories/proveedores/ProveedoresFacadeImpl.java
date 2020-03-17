package stories.proveedores;

import daos.FacturaDao;
import daos.ProveedorDao;
import daos.ReciboDao;
import entities.Factura;
import entities.Proveedor;
import entities.Recibo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.Alerta;
import utils.Validar;
import utils.ValidarNifCif;

import java.sql.SQLException;
import java.util.*;

public class ProveedoresFacadeImpl implements ProveedoresFacade {

    private static final Logger logger = LogManager.getLogger(ProveedoresFacadeImpl.class);

    /**
     * Devuelve un mensaje de error en caso de que el formulario contenga algún campo
     * que no cumpla las condiciones establecidas
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
    private String mensajeErrorValidarRegistro(
        String numeroIdentificacion,
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
        String cuentaBancaria
    ) {
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

        if (apellidos.length() > 120) {
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
     * Modifica el proveedor seleccionado
     * @param id
     * @param numeroIdentificacion
     * @param nombre
     * @param apellidos
     * @param calle
     * @param numeroCalleString
     * @param pisoString
     * @param puerta
     * @param codigoPostalString
     * @param municipio
     * @param provincia
     * @param telefonoMovil
     * @param telefonoFijo
     * @param email
     * @param cuentaBancaria
     * @return true/false
     * @throws SQLException
     */
    @Override
    public boolean modificarProveedor(Integer id,
                                    String numeroIdentificacion,
                                    String nombre,
                                    String apellidos,
                                    String calle,
                                    String numeroCalleString,
                                    String pisoString,
                                    String puerta,
                                    String codigoPostalString,
                                    String municipio,
                                    String provincia,
                                    String telefonoMovil,
                                    String telefonoFijo,
                                    String email,
                                    String cuentaBancaria) throws SQLException {
        // Comprobación de la validez del formulario
        String mensajeError = mensajeErrorValidarRegistro(
                numeroIdentificacion, nombre, apellidos, calle, numeroCalleString, pisoString, puerta,
                codigoPostalString, municipio, provincia, telefonoMovil, telefonoFijo, email, cuentaBancaria
        );
        if (StringUtils.isNotBlank(mensajeError)) {
            logger.error(mensajeError);
            new Alerta("WARNING", "Error en la modificaci\u00f3n del cliente", mensajeError);
            return false;
        }

        Proveedor cliente = ProveedorDao.getInstance().get(id);

        cliente.setnumeroIdentificacion(numeroIdentificacion);
        cliente.setNombre(nombre);
        cliente.setApellidos(apellidos);
        cliente.setDireccion(calle);
        cliente.setPuerta(puerta);
        cliente.setMunicipio(municipio);
        cliente.setProvincia(provincia);
        cliente.setMovil(telefonoMovil);
        cliente.setTelefono(telefonoFijo);
        cliente.setEmail(email);
        cliente.setCuentaBancaria(cuentaBancaria);

        if (StringUtils.isNotBlank(numeroCalleString)) {
            cliente.setNumeroDireccion(Integer.valueOf(numeroCalleString));
        }

        if (StringUtils.isNotBlank(pisoString)) {
            cliente.setPiso(Integer.valueOf(pisoString));
        }

        if (StringUtils.isNotBlank(codigoPostalString)) {
            cliente.setCodigoPostal(Integer.valueOf(codigoPostalString));
        }

        return ProveedorDao.getInstance().update(cliente);
    }

    /**
     * Inserta un proveedor en la base de datos
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
     */
    @Override
    public void altaProveedor(String numeroIdentificacion,
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
                              String cuentaBancaria) {
        // Comprobación de la validez del formulario
        String mensajeError = mensajeErrorValidarRegistro(
                numeroIdentificacion, nombre, apellidos, calle, numeroCalleString, pisoString, puerta,
                codigoPostalString, municipio, provincia, telefonoMovil, telefonoFijo, email, cuentaBancaria
        );
        if (StringUtils.isNotBlank(mensajeError)) {
            logger.error(mensajeError);
            new Alerta("WARNING", "Error en el registro del producto", mensajeError);
            return;
        }

        // Comprobación de la presencia de campos opcionales que deben ser convertidos a Integer
        Integer numeroCalle = null;
        Integer piso = null;
        Integer codigoPostal = null;

        if (StringUtils.isNotBlank(numeroCalleString)) {
            numeroCalle = Integer.valueOf(numeroCalleString);
        }
        if (StringUtils.isNotBlank(pisoString)) {
            piso = Integer.valueOf(pisoString);
        }
        if (StringUtils.isNotBlank(codigoPostalString)) {
            codigoPostal = Integer.valueOf(codigoPostalString);
        }

        Proveedor cliente = new Proveedor(numeroIdentificacion,
                                          nombre,
                                          apellidos,
                                          calle,
                                          numeroCalle,
                                          piso,
                                          puerta,
                                          municipio,
                                          provincia,
                                          codigoPostal,
                                          telefonoMovil,
                                          telefonoFijo,
                                          email,
                                          cuentaBancaria);

        ProveedorDao.getInstance().altaProveedor(cliente);

        new Alerta("INFORMATION", "Alta de Proveedor", "Alta realizada satisfactoriamente.");
    }

    /**
     * Devuelve la lista de códigos postales obtenidos de los proveedores
     * @return ObservableList
     */
    @Override
    public ObservableList<String> obtenerCodigosPostales() {
        List<Proveedor> proveedores = ProveedorDao.getInstance().obtenerProveedores();
        Set<String> codigosPostales = new HashSet<String>();

        for (Proveedor cliente: proveedores) {
            codigosPostales.add(String.valueOf(cliente.getCodigoPostal()));
        }

        return FXCollections.observableArrayList(codigosPostales);
    }

    /**
     * Devuelve una lista de proveedores buscados por Nombre, Nif/Cif/Nie o Código Postal
     * @param nombre
     * @param nif
     * @param codigoPostal
     * @return ObservableList
     */
    @Override
    public ObservableList<Proveedor> buscarProveedores(String nombre, String nif, String codigoPostal) {
        List<Proveedor> proveedoresEncontrados = null;
        StringBuffer mensajeError = new StringBuffer("");

        if (nombre != null) {
            if (nombre.length() > 60) {
                new Alerta("WARNING", "Error en campo de b\u00fasqueda", "El campo del nombre no puede exceder los 60 caracteres.");
                return null;
            }
            proveedoresEncontrados = ProveedorDao.getInstance().obtenerProveedoresPorNombre(nombre);
        }

        if (nif != null) {
            if (nif.length() != 9 && !ValidarNifCif.isvalido(nif)) {
                new Alerta("WARNING", "Error en campo de b\u00fasqueda", "El NIF/CIF/NIE introducido no es v\u00e1lido.");
                return null;
            }
            proveedoresEncontrados = ProveedorDao.getInstance().obtenerProveedorPorNif(nif);
        }

        if (codigoPostal != null) {
            Integer cpBuscar = Integer.valueOf(codigoPostal);
            proveedoresEncontrados = ProveedorDao.getInstance().obtenerProveedoresPorCP(cpBuscar);
        }


        return FXCollections.observableArrayList(proveedoresEncontrados);
    }

    /**
     * Elimina un proveedor de la base de datos
     * No se permitirá eliminar el proveedor si ya está relacionado con algún recibo
     * @param proveedor
     */
    @Override
    public void borrarProveedor(Proveedor proveedor) {
        List<Recibo> recibos = ReciboDao.getInstance().buscarRecibosPorProveedor(proveedor);
        if (!recibos.isEmpty()) {
            new Alerta("ERROR", "Error en la eliminaci\u00f3n del proveedor", "No se puede eliminar el proveedor porque est\u00e1 \nrelacionado con un recibo.");
            return;
        }
        ProveedorDao.getInstance().borrarProveedor(proveedor);
    }

    /**
     * Solicitud de confirmación para borrar el proveedor
     * @return true/false
     */
    @Override
    public boolean confirmarBorrado() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("CONFIRMACI\u00d3N");
        alert.setHeaderText("Solicitud de borrado de proveedor");
        alert.setContentText("\u00bfEst\u00e1 seguro de que desea borrar el proveedor?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) return true;

        return false;
    }
}
