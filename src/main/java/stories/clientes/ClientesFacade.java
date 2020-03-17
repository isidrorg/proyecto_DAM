package stories.clientes;

import entities.Cliente;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface ClientesFacade {

    boolean modificarCliente(Integer id,
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

    void altaCliente(String numeroIdentificacion,
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
                     String cuentaBancaria);

    ObservableList<String> obtenerCodigosPostales();

    ObservableList<Cliente> buscarClientes(String nombre,
                                           String nif,
                                           String codigoPostal);

    void borrarCliente(Cliente cliente) throws SQLException;

    boolean confirmarBorrado();
}
