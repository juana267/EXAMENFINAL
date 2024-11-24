package pe.edu.upeu.bibliotecafx.control;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import pe.edu.upeu.bibliotecafx.componente.ColumnInfo;
import pe.edu.upeu.bibliotecafx.componente.ComboBoxAutoComplete;
import pe.edu.upeu.bibliotecafx.componente.TableViewHelper;
import pe.edu.upeu.bibliotecafx.componente.Toast;
import pe.edu.upeu.bibliotecafx.dto.ComboBoxOption;
import pe.edu.upeu.bibliotecafx.modelo.Usuario;
import pe.edu.upeu.bibliotecafx.servicio.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class MainConfirmacionRegistro {

    @FXML
    private AnchorPane miContenedor;
    @FXML
    private TextField txtUsuario, txtCorreo, txtEstado;
    @FXML
    private PasswordField txtClave;
    @FXML
    private ComboBox<ComboBoxOption> cbxPerfil;
    @FXML
    private TableView<Usuario> tableView;
    @FXML
    private Label lbnMsg;

    private Stage stage;

    @Autowired
    private PerfilService perfilService;
    @Autowired
    private UsuarioService usuarioService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private Validator validator;
    private ObservableList<Usuario> listarUsuarios;
    private Usuario formulario;
    private Long idUsuarioCE = 0L;

    public void initialize() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2000), event -> {
            stage = (Stage) miContenedor.getScene().getWindow();
            if (stage != null) {
                System.out.println("El título del stage es: " + stage.getTitle());
            } else {
                System.out.println("Stage aún no disponible.");
            }
        }));
        timeline.setCycleCount(1);
        timeline.play();

        cbxPerfil.setTooltip(new Tooltip());
        cbxPerfil.getItems().addAll(perfilService.listarCombobox());
        cbxPerfil.setOnAction(event -> {
            ComboBoxOption selectedProduct = cbxPerfil.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                String selectedId = selectedProduct.getKey(); // Obtener el ID
                System.out.println("ID del perfil seleccionado: " + selectedId);
            }
        });
        new ComboBoxAutoComplete<>(cbxPerfil);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        // Crear instancia de la clase genérica TableViewHelper
        TableViewHelper<Usuario> tableViewHelper = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        // Configurar columnas para la tabla de usuarios
        columns.put("ID Usuario", new ColumnInfo("idUsuario", 60.0)); // Columna ID del usuario
        columns.put("Usuario", new ColumnInfo("user", 150.0)); // Columna Usuario
        columns.put("Clave", new ColumnInfo("clave", 150.0)); // Columna Usuario
        columns.put("Correo", new ColumnInfo("correo", 150.0)); // Columna Correo
        columns.put("Usuario", new ColumnInfo("user", 150.0)); // Columna Usuario
        columns.put("Estado", new ColumnInfo("estado", 100.0)); // Columna Estado
        columns.put("Perfil", new ColumnInfo("idPerfil.nombre", 150.0)); // Columna Perfil (mapeada al nombre)

        Consumer<Usuario> updateAction = (Usuario usuario) -> {
            System.out.println("Actualizar: " + usuario);
            editForm(usuario);
        };
        Consumer<Usuario> deleteAction = (Usuario usuario) -> {
            System.out.println("Eliminar: " + usuario);
            usuarioService.delete(usuario.getIdUsuario());
            double width = stage.getWidth() / 1.5;
            double height = stage.getHeight() / 2;
            Toast.showToast(stage, "Se eliminó correctamente!!", 2000, width, height);
            listar();
        };

        tableViewHelper.addColumnsInOrderWithSize(tableView, columns, updateAction, deleteAction);
        tableView.setTableMenuButtonVisible(true);
        listar();
    }

    public void listar() {
        try {
            tableView.getItems().clear();
            listarUsuarios = FXCollections.observableArrayList(usuarioService.list());
            tableView.getItems().addAll(listarUsuarios);
        } catch (Exception e) {
            System.out.println("Error al listar los usuarios: " + e.getMessage());
        }
    }

    public void limpiarErrores() {
        txtUsuario.getStyleClass().remove("text-field-error");
        txtCorreo.getStyleClass().remove("text-field-error");
        txtEstado.getStyleClass().remove("text-field-error");
        txtClave.getStyleClass().remove("text-field-error");
        cbxPerfil.getStyleClass().remove("text-field-error");
    }

    public void clearForm() {
        txtUsuario.setText("");
        txtCorreo.setText("");
        txtEstado.setText("");
        txtClave.setText("");
        cbxPerfil.getSelectionModel().select(null);
        idUsuarioCE = 0L;
        limpiarErrores();
    }

    @FXML
    public void cancelarAccion() {
        clearForm();
        limpiarErrores();
    }

    void validarCampos(List<ConstraintViolation<Usuario>> violacionesOrdenadasPorPropiedad) {
        LinkedHashMap<String, String> erroresOrdenados = new LinkedHashMap<>();
        for (ConstraintViolation<Usuario> violacion : violacionesOrdenadasPorPropiedad) {
            String campo = violacion.getPropertyPath().toString();
            switch (campo) {
                case "user":
                    erroresOrdenados.put("user", violacion.getMessage());
                    txtUsuario.getStyleClass().add("text-field-error");
                    break;
                case "correo":
                    erroresOrdenados.put("correo", violacion.getMessage());
                    txtCorreo.getStyleClass().add("text-field-error");
                    break;
                case "estado":
                    erroresOrdenados.put("estado", violacion.getMessage());
                    txtEstado.getStyleClass().add("text-field-error");
                    break;
                case "clave":
                    erroresOrdenados.put("clave", violacion.getMessage());
                    txtClave.getStyleClass().add("text-field-error");
                    break;
                case "idPerfil":
                    erroresOrdenados.put("idPerfil", violacion.getMessage());
                    cbxPerfil.getStyleClass().add("text-field-error");
                    break;
                default:
                    System.out.println("Campo desconocido: " + campo);
                    break;
            }
        }

        if (!erroresOrdenados.isEmpty()) {
            Map.Entry<String, String> primerError = erroresOrdenados.entrySet().iterator().next();
            lbnMsg.setText(primerError.getValue());
            lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
        }
    }

    @FXML
    public void validarFormulario() {
        formulario = new Usuario();

        // Obtener los valores de los campos
        formulario.setUser(txtUsuario.getText());
        formulario.setCorreo(txtCorreo.getText());
        formulario.setEstado(txtEstado.getText());

        // Encriptar la contraseña antes de guardarla
        String encriptadaClave = passwordEncoder.encode(txtClave.getText());
        formulario.setClave(encriptadaClave); // Guardamos la contraseña encriptada

        // Continuar con el resto de la lógica de validación y guardado
        String idxPerfil = cbxPerfil.getSelectionModel().getSelectedItem() == null ? "0" : cbxPerfil.getSelectionModel().getSelectedItem().getKey();
        formulario.setIdPerfil(perfilService.searchById(Long.parseLong(idxPerfil)));

        Set<ConstraintViolation<Usuario>> violaciones = validator.validate(formulario);
        List<ConstraintViolation<Usuario>> violacionesOrdenadasPorPropiedad = violaciones.stream()
                .sorted((v1, v2) -> v1.getPropertyPath().toString().compareTo(v2.getPropertyPath().toString()))
                .collect(Collectors.toList());

        if (violacionesOrdenadasPorPropiedad.isEmpty()) {
            lbnMsg.setText("Formulario válido");
            lbnMsg.setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
            limpiarErrores();

            double width = stage.getWidth() / 1.5;
            double height = stage.getHeight() / 2;
            if (idUsuarioCE != 0L && idUsuarioCE > 0L) {
                formulario.setIdUsuario(idUsuarioCE);
                Toast.showToast(stage, "Se actualizó correctamente!!", 2000, width, height);
            } else {
                usuarioService.save(formulario); // Guardamos el objeto con la contraseña encriptada
                Toast.showToast(stage, "Se guardó correctamente!!", 2000, width, height);
            }

            clearForm();
            listar();
        } else {
            validarCampos(violacionesOrdenadasPorPropiedad);
        }
    }

    public void editForm(Usuario usuario) {
        txtUsuario.setText(usuario.getUser());
        txtCorreo.setText(usuario.getCorreo());
        txtEstado.setText(usuario.getEstado());
        txtClave.setText(usuario.getClave());

        cbxPerfil.getSelectionModel().select(
                cbxPerfil.getItems().stream()
                        .filter(perfil -> Long.parseLong(perfil.getKey()) == usuario.getIdPerfil().getIdPerfil())
                        .findFirst()
                        .orElse(null)
        );

        idUsuarioCE = usuario.getIdUsuario();
        limpiarErrores();
    }
}

