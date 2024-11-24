package pe.edu.upeu.bibliotecafx.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MainViewController {

    @Autowired
    private ApplicationContext context;

    @FXML
    private Button btnIniciarSesion;
    @FXML
    private Button btnRegistar;
    @FXML
    private StackPane containerForm;

    // Acción para el botón "Iniciar sesión"
    @FXML
    public void actionEvent(ActionEvent event) throws Exception {
        Button clickedButton = (Button) event.getSource();

        if (clickedButton.equals(btnIniciarSesion)) {
            // Cargar el formulario de login (suponiendo que tienes un archivo FXML de login)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
            loader.setControllerFactory(context::getBean); // Usar Spring para inyectar el controlador
            Parent loginRoot = loader.load();

// Limpiar la StackPane actual
            containerForm.getChildren().clear();

// Agregar el formulario de login al contenedor
            containerForm.getChildren().add(loginRoot);

        } else if (clickedButton.equals(btnRegistar)) {
            // Use FXMLLoader with the Spring context to load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/registro.fxml"));
            loader.setControllerFactory(context::getBean);  // Use Spring's controller factory

            // Load the FXML file
            Parent signUpRoot = loader.load();

            // Clear the current container and add the new form
            containerForm.getChildren().clear();
            containerForm.getChildren().add(signUpRoot); // Agregar el formulario de registro al contenedor
        }
    }

    // Este método podría usarse para cargar el contenido inicial o cualquier otra acción que desees agregar al cargar la vista.
    @FXML
    public void initialize() {
        // Si quieres ejecutar alguna acción cuando la vista se inicialice, puedes hacerlo aquí.
        System.out.println("Vista Principal cargada");
    }
}

