package stories.clientes;

import daos.ClienteDao;
import daos.FacturaDao;
import entities.Cliente;
import entities.Factura;
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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ClientesFacadeImpl implements ClientesFacade {

    private static final Logger logger = LogManager.getLogger(ClientesFacadeImpl.class);

    /**
     * Valida las entradas del formulario de alta de clientes
     * Devuelve un mensaje de error en caso de no cumplirse una condición
     * @param numeroIdentificacion
     * @param nombre
     * @param apellidos
     * @param calle
     * @param numeroCalle
     * @param piso
     * @param puerta
     * @param municipio
     * @param provincia
     * @param codigoPostal
     * @param telefonoMovil
     * @param telefonoFijo
     * @param email
     * @param cuentaBancaria
     * @return String
     */
    private String mensajeErrorValidarRegistro(String numeroIdentificacion,
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
                                               String cuentaBancaria) {
        StringBuffer mensajeError = new StringBuffer("");

        if (StringUtils.isBlank(numeroIdentificacion)) {
            mensajeError.append("Se requiere introducir el NIF/CIF/NIE de la persona f\u00edsica o jur\u00eddica.\n\n");
        } else if (numeroIdentificacion.length() != 9) {
            mensajeError.append("El NIF/CIF/NIE tiene que se de 9 caracteres.\n\n");
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
                mensajeError.append("El n\u00famero de la dirección tiene que ser un \nn\u00famero entero.\n\n");
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
            if (!Validar.esCodigoPostal(codigoPostal)) {
                mensajeError.append("El C\u00f3digo Postal tiene que ser 5 d\u00edgitos.");
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
                mensajeError.append("La cuenta bancaria no est\u00e1 en formato IBAN español.\n\n");
            }
        }
        return mensajeError.toString();
    }

    /**
     * Modifica los datos de un cliente en la base de datos
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
    public boolean modificarCliente(Integer id,
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
        String mensajeError = mensajeErrorValidarRegistro(
                numeroIdentificacion, nombre, apellidos, calle, numeroCalleString, pisoString, puerta,
                 municipio, provincia, codigoPostalString, telefonoMovil, telefonoFijo, email, cuentaBancaria
        );
        if (StringUtils.isNotBlank(mensajeError)) {
            logger.error(mensajeError);
            new Alerta("WARNING", "Error en la modificaci\u00f3n del cliente", mensajeError);
            return false;
        }

        Cliente cliente = ClienteDao.getInstance().get(id);

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

        return ClienteDao.getInstance().update(cliente);
    }

    /**
     * Da alta a un cliente en la base de datos
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
    public void altaCliente(String numeroIdentificacion,
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
        String mensajeError = mensajeErrorValidarRegistro(
                numeroIdentificacion, nombre, apellidos, calle, numeroCalleString, pisoString, puerta,
                municipio, provincia, codigoPostalString, telefonoMovil, telefonoFijo, email, cuentaBancaria
        );
        if (StringUtils.isNotBlank(mensajeError)) {
            logger.error(mensajeError);
            new Alerta("WARNING", "Error en el registro del producto", mensajeError);
            return;
        }

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

        Cliente cliente = new Cliente(numeroIdentificacion,
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

        ClienteDao.getInstance().altaCliente(cliente);

        new Alerta("INFORMATION", "Alta de Cliente", "Alta realizada satisfactoriamente.");
    }

    /**
     * Devuelve una lista de los códigos postales indicados de los cliente
     * @return ObservableList
     */
    @Override
    public ObservableList<String> obtenerCodigosPostales() {
        List<Cliente> clientes = ClienteDao.getInstance().obtenerClientes();
        Set<String> codigosPostales = new HashSet<String>();

        for (Cliente cliente: clientes) {
            if  (cliente.getCodigoPostal() != null) {
                codigosPostales.add(String.valueOf(cliente.getCodigoPostal()));
            }
        }

        return FXCollections.observableArrayList(codigosPostales);
    }

    /**
     * Devuelve una lista de clientes según los parámetros indicados para la búsqueda
     * @param nombre
     * @param nif
     * @param codigoPostal
     * @return ObservableList
     */
    @Override
    public ObservableList<Cliente> buscarClientes(String nombre, String nif, String codigoPostal) {
        List<Cliente> clientesEncontrados = null;
        StringBuffer mensajeError = new StringBuffer("");

        if (nombre != null) {
            if (nombre.length() > 60) {
                new Alerta("WARNING", "Error en campo de b\u00fasqueda", "El campo del nombre no puede exceder los 60 caracteres.");
                return null;
            }
            clientesEncontrados = ClienteDao.getInstance().obtenerClientesPorNombre(nombre);
        }

        if (nif != null) {
            if (nif.length() != 9 && !ValidarNifCif.isvalido(nif)) {
                new Alerta("WARNING", "Error en campo de b\u00fasqueda", "El NIF/CIF/NIE introducido no es v\u00e1lido.");
                return null;
            }
            clientesEncontrados = ClienteDao.getInstance().obtenerClientePorNif(nif);
        }

        if (codigoPostal != null) {
            Integer cpBuscar = Integer.valueOf(codigoPostal);
            clientesEncontrados = ClienteDao.getInstance().obtenerClientesPorCP(cpBuscar);
        }


        return FXCollections.observableArrayList(clientesEncontrados);
    }

    /**
     * Elimina un cliente de la base de datos si no está relacionado con ninguna factura
     * @param cliente
     */
    @Override
    public void borrarCliente(Cliente cliente) throws SQLException {
        List<Factura> facturas = FacturaDao.getInstance().buscarFacturasPorCliente(cliente);
        if (!facturas.isEmpty()) {
            new Alerta("ERROR", "Error en la eliminaci\u00f3n del cliente", "No se puede eliminar el cliente porque est\u00e1 \n relacionado con una factura.");
            return;
        }
        ClienteDao.getInstance().borrarCliente(cliente);
    }

    /**
     * Muestra una alerta pidiendo confirmación para eliminar un cliente
     * @return
     */
    @Override
    public boolean confirmarBorrado() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("CONFIRMACI\u00d3N");
        alert.setHeaderText("Solicitud de borrado de Cliente");
        alert.setContentText("\u00bfEst\u00e1 seguro de que desea borrar el cliente?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        }

        return false;
    }
}
