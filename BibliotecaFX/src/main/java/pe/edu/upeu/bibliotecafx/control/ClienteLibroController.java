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
import org.springframework.stereotype.Component;
import pe.edu.upeu.bibliotecafx.componente.ColumnInfo;
import pe.edu.upeu.bibliotecafx.componente.ComboBoxAutoComplete;
import pe.edu.upeu.bibliotecafx.componente.TableViewHelper;
import pe.edu.upeu.bibliotecafx.componente.Toast;
import pe.edu.upeu.bibliotecafx.dto.ComboBoxOption;
import pe.edu.upeu.bibliotecafx.modelo.ClienteLibro;
import pe.edu.upeu.bibliotecafx.servicio.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class ClienteLibroController {

    @FXML
    private AnchorPane miContenedor;
    @FXML
    private TextField txtDni, txtRuc, txtNombre, txtRecLegal;
    @FXML
    private TableView<ClienteLibro> tableView;
    @FXML
    private Label lbnMsg;

    private Stage stage;

    @Autowired
    private ClienteLibroService clienteLibroService;

    private Validator validator;
    private ObservableList<ClienteLibro> listarClientes;
    private ClienteLibro formulario;
    private Long idClienteCE = 0L;

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

        // Configuración de los campos de validación
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        // Crear instancia de la clase genérica TableViewHelper
        TableViewHelper<ClienteLibro> tableViewHelper = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        // Configurar columnas para la tabla de clientes
        columns.put("ID Cliente", new ColumnInfo("idCliente", 60.0)); // Columna ID del cliente
        columns.put("DNI", new ColumnInfo("dni", 150.0)); // Columna DNI
        columns.put("RUC", new ColumnInfo("ruc", 150.0)); // Columna RUC
        columns.put("Nombres", new ColumnInfo("nombres", 200.0)); // Columna Nombres
        columns.put("Rep. Legal", new ColumnInfo("repLegal", 150.0)); // Columna Representante Legal

        Consumer<ClienteLibro> updateAction = (ClienteLibro cliente) -> {
            System.out.println("Actualizar: " + cliente);
            editForm(cliente);
        };
        Consumer<ClienteLibro> deleteAction = (ClienteLibro cliente) -> {
            System.out.println("Eliminar: " + cliente);
            clienteLibroService.delete(cliente.getIdCliente());
            double with = stage.getWidth() / 1.5;
            double height = stage.getHeight() / 2;
            Toast.showToast(stage, "Se eliminó correctamente!!", 2000, with, height);
            listar();
        };

        tableViewHelper.addColumnsInOrderWithSize(tableView, columns, updateAction, deleteAction);

        tableView.setTableMenuButtonVisible(true);
        listar();
    }

    public void listar() {
        try {
            // Limpiar los ítems actuales en la tabla
            tableView.getItems().clear();

            // Obtener la lista de clientes desde el servicio
            listarClientes = FXCollections.observableArrayList(clienteLibroService.list());

            // Agregar los clientes obtenidos a la tabla
            tableView.getItems().addAll(listarClientes);

        } catch (Exception e) {
            System.out.println("Error al listar los clientes: " + e.getMessage());
        }
    }

    public void limpiarErrores() {
        txtDni.getStyleClass().remove("text-field-error");
        txtRuc.getStyleClass().remove("text-field-error");
        txtNombre.getStyleClass().remove("text-field-error");
        txtRecLegal.getStyleClass().remove("text-field-error");
    }

    public void clearForm() {
        // Limpiar campos de texto
        txtDni.setText("");
        txtRuc.setText("");
        txtNombre.setText("");
        txtRecLegal.setText("");

        // Restablecer ID del cliente a 0 (indica que no hay edición activa)
        idClienteCE = 0L;

        // Limpiar errores visuales en el formulario
        limpiarErrores();
    }

    @FXML
    public void cancelarAccion() {
        clearForm();
        limpiarErrores();
    }

    void validarCampos(List<ConstraintViolation<ClienteLibro>> violacionesOrdenadasPorPropiedad) {
        // Crear un LinkedHashMap para ordenar las violaciones
        LinkedHashMap<String, String> erroresOrdenados = new LinkedHashMap<>();

        // Recorrer las violaciones para asociar errores a los campos correspondientes
        for (ConstraintViolation<ClienteLibro> violacion : violacionesOrdenadasPorPropiedad) {
            String campo = violacion.getPropertyPath().toString();

            switch (campo) {
                case "dni":
                    erroresOrdenados.put("dni", violacion.getMessage());
                    txtDni.getStyleClass().add("text-field-error");
                    break;

                case "ruc":
                    erroresOrdenados.put("ruc", violacion.getMessage());
                    txtRuc.getStyleClass().add("text-field-error");
                    break;

                case "nombres":
                    erroresOrdenados.put("nombres", violacion.getMessage());
                    txtNombre.getStyleClass().add("text-field-error");
                    break;

                case "repLegal":
                    erroresOrdenados.put("repLegal", violacion.getMessage());
                    txtRecLegal.getStyleClass().add("text-field-error");
                    break;

                default:
                    System.out.println("Campo desconocido: " + campo);
                    break;
            }
        }

        // Mostrar el primer error en el mensaje de la etiqueta
        if (!erroresOrdenados.isEmpty()) {
            Map.Entry<String, String> primerError = erroresOrdenados.entrySet().iterator().next();
            lbnMsg.setText(primerError.getValue()); // Mostrar el mensaje del primer error
            lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
        }
    }

    @FXML
    public void validarFormulario() {
        // Crear una nueva instancia de ClienteLibro
        formulario = new ClienteLibro();

        // Asignar valores desde el formulario
        formulario.setDni(txtDni.getText());
        formulario.setRuc(txtRuc.getText());
        formulario.setNombres(txtNombre.getText());
        formulario.setRepLegal(txtRecLegal.getText());

        // Validar el formulario usando el validador
        Set<ConstraintViolation<ClienteLibro>> violaciones = validator.validate(formulario);

        // Ordenar las violaciones por el nombre de la propiedad
        List<ConstraintViolation<ClienteLibro>> violacionesOrdenadasPorPropiedad = violaciones.stream()
                .sorted((v1, v2) -> v1.getPropertyPath().toString().compareTo(v2.getPropertyPath().toString()))
                .collect(Collectors.toList());

        // Verificar si el formulario es válido
        if (violacionesOrdenadasPorPropiedad.isEmpty()) {
            // Datos válidos
            lbnMsg.setText("Formulario válido");
            lbnMsg.setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
            limpiarErrores();

            // Mostrar un mensaje de guardado o actualización
            double width = stage.getWidth() / 1.5;
            double height = stage.getHeight() / 2;
            if (idClienteCE != 0L && idClienteCE > 0L) {
                formulario.setIdCliente(idClienteCE); // Asegúrate de establecer el ID del cliente en el formulario
                clienteLibroService.update(idClienteCE, formulario);  // Pasar el id y el objeto cliente
                Toast.showToast(stage, "Se actualizó correctamente!!", 2000, width, height);
            } else {
                clienteLibroService.save(formulario); // Guardar un nuevo cliente
                Toast.showToast(stage, "Se guardó correctamente!!", 2000, width, height);
            }

            // Limpiar el formulario y recargar la tabla
            clearForm();
            listar();
        } else {
            // Mostrar errores en el formulario
            validarCampos(violacionesOrdenadasPorPropiedad);
        }
    }


    public void editForm(ClienteLibro cliente) {
        // Rellenar los campos de texto con los datos del cliente
        txtDni.setText(cliente.getDni());
        txtRuc.setText(cliente.getRuc());
        txtNombre.setText(cliente.getNombres());
        txtRecLegal.setText(cliente.getRepLegal());

        // Guardar el ID del cliente que se está editando
        idClienteCE = cliente.getIdCliente();

        // Limpiar posibles errores previos en el formulario
        limpiarErrores();
    }
}
