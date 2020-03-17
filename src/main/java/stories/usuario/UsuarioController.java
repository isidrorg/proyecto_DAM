package stories.usuario;

import daos.UsuarioDao;
import entities.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import utils.Alerta;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class UsuarioController implements Initializable {

    private final UsuarioFacade facade;
    Usuario usuarioSesion;

    @FXML private TextField numeroIdentificacion;
    @FXML private TextField nombre;
    @FXML private TextField apellidos;
    @FXML private TextField calle;
    @FXML private TextField numeroCalle;
    @FXML private TextField piso;
    @FXML private TextField puerta;
    @FXML private TextField codigoPostal;
    @FXML private TextField municipio;
    @FXML private TextField provincia;
    @FXML private TextField telefonoMovil;
    @FXML private TextField telefonoFijo;
    @FXML private TextField email;
    @FXML private TextField cuentaBancaria;
    @FXML private PasswordField contrasenyaAntigua;
    @FXML private PasswordField contrasenyaNueva;
    @FXML private PasswordField contrasenyaNuevaConfirmacion;


    public UsuarioController() {
        super();
        facade = new UsuarioFacadeImpl();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Optional<Usuario> usuarioOptional = UsuarioDao.getInstance().obtenerUsuarioPorSesion();

        if (usuarioOptional.isPresent()) {
            usuarioSesion = usuarioOptional.get();
            editarUsuario();
        }
    }

    @FXML
    public void modificarUsuario() throws SQLException {
        if (facade.modificarUsuario(usuarioSesion.getId(),
                                    numeroIdentificacion.getText(),
                                    nombre.getText(),
                                    apellidos.getText(),
                                    calle.getText(),
                                    numeroCalle.getText(),
                                    piso.getText(),
                                    puerta.getText(),
                                    municipio.getText(),
                                    provincia.getText(),
                                    codigoPostal.getText(),
                                    telefonoMovil.getText(),
                                    telefonoFijo.getText(),
                                    email.getText(),
                                    cuentaBancaria.getText())) {
            new Alerta("INFORMATION", "Modificaci\u00f3n de Usuario", "Modificaci\u00f3n del Usuario realizada con \u00e9xito.");
        }
    }

    @FXML
    public void modificarContrasenya() throws SQLException {
        if (facade.modificarContrasenya(usuarioSesion.getId(),
                                        contrasenyaAntigua.getText(),
                                        contrasenyaNueva.getText(),
                                        contrasenyaNuevaConfirmacion.getText())) {
            new Alerta("INFORMATION", "Modificaci\u00f3n de contrase\u00f1a", "Contrase\u00f1a modificada correctamente.");
        }
    }

    public void editarUsuario() {
        numeroIdentificacion.setText(usuarioSesion.getNumeroIdentificacion());
        nombre.setText(usuarioSesion.getNombre());
        apellidos.setText(usuarioSesion.getApellidos());
        calle.setText(usuarioSesion.getDireccion());

        if (usuarioSesion.getNumeroDireccion() != null) {
            numeroCalle.setText(String.valueOf(usuarioSesion.getNumeroDireccion()));
        }

        if (usuarioSesion.getPiso() != null) {
            piso.setText(String.valueOf(usuarioSesion.getPiso()));
        }

        puerta.setText(usuarioSesion.getPuerta());
        municipio.setText(usuarioSesion.getMunicipio());
        provincia.setText(usuarioSesion.getProvincia());

        if (usuarioSesion.getCodigoPostal() != null) {
            codigoPostal.setText(String.valueOf(usuarioSesion.getCodigoPostal()));
        }

        telefonoMovil.setText(usuarioSesion.getMovil());
        telefonoFijo.setText(usuarioSesion.getTelefono());
        email.setText(usuarioSesion.getEmail());
        cuentaBancaria.setText(usuarioSesion.getCuentaBancaria());
    }
}
