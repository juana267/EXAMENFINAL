package pe.edu.upeu.bibliotecafx.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import pe.edu.upeu.bibliotecafx.componente.StageManager;
import pe.edu.upeu.bibliotecafx.componente.Toast;
import pe.edu.upeu.bibliotecafx.dto.SessionManager;
import pe.edu.upeu.bibliotecafx.modelo.Usuario;
import pe.edu.upeu.bibliotecafx.servicio.UsuarioService;

import java.awt.event.MouseEvent;
import java.io.IOException;

@Component
public class LoginController {

    @Autowired
    private UsuarioService us;

    @Autowired
    private ApplicationContext context;

    @FXML
    private CheckBox checkViewPassSignIn;

    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtClave;
    @FXML
    private Button btnIngresar;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // Instancia de BCryptPasswordEncoder

    @FXML
    public void login(ActionEvent event) throws IOException {
        try {
            // Intentamos obtener el usuario de la base de datos
            Usuario usu = us.loginUsuario(txtUsuario.getText(), txtClave.getText());

            if (usu != null && passwordEncoder.matches(txtClave.getText(), usu.getClave())) {
                // Si el usuario y la contraseña coinciden
                SessionManager.getInstance().setUserId(usu.getIdUsuario());
                SessionManager.getInstance().setUserName(usu.getUser());
                SessionManager.getInstance().setNombrePerfil(usu.getIdPerfil().getNombre());

                // Cargar la interfaz principal
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/guimainfx.fxml"));
                loader.setControllerFactory(context::getBean);
                Parent mainRoot = loader.load();
                Screen screen = Screen.getPrimary();
                Rectangle2D bounds = screen.getBounds();
                Scene mainScene = new Scene(mainRoot, bounds.getWidth(), bounds.getHeight() - 30);
                mainScene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.getIcons().add(new Image(getClass().getResource("/img/store.png").toExternalForm()));
                stage.setScene(mainScene);
                stage.setTitle("Pantalla Principal");
                stage.setX(bounds.getMinX());
                stage.setY(bounds.getMinY());
                stage.setResizable(true);
                StageManager.setPrimaryStage(stage);
                stage.setWidth(bounds.getWidth());
                stage.setHeight(bounds.getHeight());
                stage.show();
            } else {
                // Si la credencial es inválida, mostramos un mensaje
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                double with = stage.getWidth() * 2;
                double h = stage.getHeight() / 2;
                Toast.showToast(stage, "Credenciales inválidas! Intente nuevamente", 2000, with, h);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // En caso de excepción, mostramos un mensaje de error genérico
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            double with = stage.getWidth() * 2;
            double h = stage.getHeight() / 2;
            Toast.showToast(stage, "Espere confirmacion del ADMIN ", 2000, with, h);
        }
    }

    @FXML
    public void actionEvent() {
        limpiarFormulario(); // Acciona la limpieza del formulario
    }

    @FXML
    public void limpiarFormulario() {
        txtUsuario.setText("");
        txtClave.setText("");
        limpiarErrores(); // Limpia errores visuales
        mostrarMensaje("", true); // Limpia cualquier mensaje en pantalla
    }
    private void limpiarErrores() {
        txtUsuario.getStyleClass().remove("text-field-error");
        txtClave.getStyleClass().remove("text-field-error");
    }
    private void mostrarMensaje(String mensaje, boolean exito) {
    }

    @FXML
    void togglePasswordVisibility(MouseEvent event) {
        if (checkViewPassSignIn.isSelected()) {
            // Mostrar la contraseña (opcional: usar un TextField en lugar de PasswordField)
            System.out.println("Contraseña visible: " + txtClave.getText());
        } else {
            // Ocultar la contraseña
            System.out.println("Contraseña oculta.");
        }
    }


}
