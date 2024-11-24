package pe.edu.upeu.bibliotecafx.control;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import pe.edu.upeu.bibliotecafx.modelo.Usuario;
import pe.edu.upeu.bibliotecafx.servicio.UsuarioService;

@Component
public class RegistroController {

    @FXML
    private TextField txtUsuario, txtCorreo;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Button btnLimpiar;

    @Autowired
    private UsuarioService usuarioService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @FXML
    public void validarFormulario() {
        limpiarErrores(); // Limpia errores visuales previos

        Usuario usuario = new Usuario();

        // Validar campos obligatorios
        if (txtUsuario.getText().trim().isEmpty()) {
            mostrarMensaje("El nombre de usuario es obligatorio", false);
            txtUsuario.getStyleClass().add("text-field-error");
            return;
        }
        if (txtCorreo.getText().trim().isEmpty()) {
            mostrarMensaje("El correo es obligatorio", false);
            txtCorreo.getStyleClass().add("text-field-error");
            return;
        }
        if (txtPassword.getText().isEmpty()) {
            mostrarMensaje("La contraseña es obligatoria", false);
            txtPassword.getStyleClass().add("text-field-error");
            return;
        }

        // Asignar valores al objeto Usuario
        usuario.setUser(txtUsuario.getText().trim());
        usuario.setCorreo(txtCorreo.getText().trim());
        usuario.setClave(passwordEncoder.encode(txtPassword.getText().trim())); // Encriptar la contraseña
        usuario.setEstado("Activo"); // Valor predeterminado para el estado
        usuario.setIdPerfil(null); // El perfil puede ser asignado más adelante

        try {
            // Guardar usuario
            usuarioService.save(usuario);
            mostrarMensaje("Usuario registrado correctamente", true);
            limpiarFormulario(); // Limpia el formulario tras el registro
        } catch (Exception e) {
            mostrarMensaje("Error al registrar usuario: " + e.getMessage(), false);
        }
    }

    @FXML
    public void actionEvent() {
        limpiarFormulario(); // Acciona la limpieza del formulario
    }

    @FXML
    public void limpiarFormulario() {
        txtUsuario.setText("");
        txtCorreo.setText("");
        txtPassword.setText("");
        limpiarErrores(); // Limpia errores visuales
        mostrarMensaje("", true); // Limpia cualquier mensaje en pantalla
    }

    private void limpiarErrores() {
        txtUsuario.getStyleClass().remove("text-field-error");
        txtCorreo.getStyleClass().remove("text-field-error");
        txtPassword.getStyleClass().remove("text-field-error");
    }

    private void mostrarMensaje(String mensaje, boolean exito) {
    }
}
