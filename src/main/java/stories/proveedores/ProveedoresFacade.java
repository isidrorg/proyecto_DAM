package stories.proveedores;

import entities.Proveedor;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface ProveedoresFacade {

    boolean modificarProveedor(Integer id,
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
                             String cuentaBancaria) throws SQLException;

    void altaProveedor(String numeroIdentificacion,
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
                     String cuentaBancaria);

    ObservableList<String> obtenerCodigosPostales();

    ObservableList<Proveedor> buscarProveedores(String nombre,
                                                String nif,
                                                String codigoPostal);

    void borrarProveedor(Proveedor proveedor);

    boolean confirmarBorrado();
}
