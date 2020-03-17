package stories.login;

import daos.UsuarioDao;
import entities.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utils.Ventana;

import java.util.List;
import java.util.Optional;

public class LoginController {

    @FXML TextField usuario;
    @FXML PasswordField contrasenya;
    @FXML Label aviso;
    @FXML AnchorPane ventanaLogin;

    /**
     * Identifica al empleado solicitando su usuario y contraseña, en caso de éxito
     * permite el acceso a la aplicación y marca al usuario como que se encuentra en sesión
     * @param actionEvent
     * @throws Exception
     */
    @FXML
    private void iniciarSesión(ActionEvent actionEvent) throws Exception {

        // Obtención de campos introducidos y eliminación de posibles espacios insertados
        String usuarioIntroducido = usuario.getText();
        String contrasenyaIntroducida = contrasenya.getText();
        usuarioIntroducido = usuarioIntroducido.replaceAll("\\s+","");
        contrasenyaIntroducida = contrasenyaIntroducida.replaceAll("\\s+","");

        // Obtención del usuario de la base de datos y comprobación de que exista
        Optional<Usuario> usuarioValidando = UsuarioDao.getInstance().obtenerPorUsuario(usuarioIntroducido);

        if (!usuarioValidando.isPresent()) {
            aviso.setVisible(true);
            aviso.setText("No existe dicho usuario.");
            return;
        }

        // Obtención de la contraseña de la base de datos y comprobación de que coincida
        String contrasenyaValida = usuarioValidando.get().getContrasenya();

        if (!contrasenyaIntroducida.equals(contrasenyaValida)) {
            aviso.setVisible(true);
            aviso.setText("La contrase\u00f1a y el usuario no coinciden.");
            return;
        }

        // En caso de exito, se borran las sesiones registradas previamente y se crea una nueva
        List<Usuario> usuarios = UsuarioDao.getInstance().obtenerUsuarios();
        Usuario usuarioValidado = usuarioValidando.get();

        for (Usuario usuario: usuarios) {
            usuario.setSesion(false);
            UsuarioDao.getInstance().update(usuario);
        }

        usuarioValidado.setSesion(true);
        UsuarioDao.getInstance().update(usuarioValidado);

        // En caso de exito se cierra la ventana actual y se abre la aplicación
        Stage stage = (Stage) ventanaLogin.getScene().getWindow();
        stage.close();

        new Ventana(new Stage(), "Entrada", true);
    }
}
