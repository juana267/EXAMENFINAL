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
import pe.edu.upeu.bibliotecafx.modelo.Libro;
import pe.edu.upeu.bibliotecafx.servicio.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class LibroController {

    @FXML
    private AnchorPane miContenedor;
    @FXML
    private TextField txtTitulo, txtAutor, txtIsbn, txtCopiasD, txtNPaginas, txtAñoPublicacion, txtPrecio;
    @FXML
    private ComboBox<ComboBoxOption> cbxCategoria, cbxEditorial, cbxIdioma, cbxEstadoLibro;
    @FXML
    private TableView<Libro> tableView;
    @FXML
    private Label lbnMsg;

    private Stage stage;

    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private EditorialService editorialService;
    @Autowired
    private IdiomaService idiomaService;
    @Autowired
    private EstadoLibroService estadoLibroService;

    @Autowired
    private LibroService libroService;

    private Validator validator;
    private ObservableList<Libro> listarLibros;
    private Libro formulario;
    private Long idLibroCE = 0L;

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

        cbxIdioma.setTooltip(new Tooltip());
        cbxIdioma.getItems().addAll(idiomaService.listarCombobox());
        cbxIdioma.setOnAction(event -> {
            ComboBoxOption selectedProduct = cbxIdioma.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                String selectedId = selectedProduct.getKey(); // Obtener el ID
                System.out.println("ID del producto seleccionado: " + selectedId);
            }
        });
        new ComboBoxAutoComplete<>(cbxIdioma);

        cbxCategoria.setTooltip(new Tooltip());
        cbxCategoria.getItems().addAll(categoriaService.listarCombobox());
        cbxCategoria.setOnAction(event -> {
            ComboBoxOption selectedProduct = cbxCategoria.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                String selectedId = selectedProduct.getKey(); // Obtener el ID
                System.out.println("ID del producto seleccionado: " + selectedId);
            }
        });
        new ComboBoxAutoComplete<>(cbxCategoria);

        cbxEditorial.setTooltip(new Tooltip());
        cbxEditorial.getItems().addAll(editorialService.listarCombobox());
        cbxEditorial.setOnAction(event -> {
            ComboBoxOption selectedProduct = cbxEditorial.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                String selectedId = selectedProduct.getKey(); // Obtener el ID
                System.out.println("ID del producto seleccionado: " + selectedId);
            }
        });
        new ComboBoxAutoComplete<>(cbxEditorial);

        cbxEstadoLibro.setTooltip(new Tooltip());
        cbxEstadoLibro.getItems().addAll(estadoLibroService.listarCombobox());
        cbxEstadoLibro.setOnAction(event -> {
            ComboBoxOption selectedProduct = cbxEstadoLibro.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                String selectedId = selectedProduct.getKey(); // Obtener el ID
                System.out.println("ID del producto seleccionado: " + selectedId);
            }
        });
        new ComboBoxAutoComplete<>(cbxEstadoLibro);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        // Crear instancia de la clase genérica TableViewHelper
        TableViewHelper<Libro> tableViewHelper = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        // Configurar columnas para la tabla de libros
        columns.put("ID Libro", new ColumnInfo("idLibro", 60.0)); // Columna ID del libro
        columns.put("Título", new ColumnInfo("titulo", 200.0)); // Columna Título
        columns.put("Autor", new ColumnInfo("autor", 150.0)); // Columna Autor
        columns.put("ISBN", new ColumnInfo("isbn", 150.0)); // Columna ISBN
        columns.put("Copias Disponibles", new ColumnInfo("copiasDisponibles", 150.0)); // Columna Copias Disponibles
        columns.put("N° Páginas", new ColumnInfo("numeroPaginas", 100.0)); // Columna Número de Páginas
        columns.put("Año Publicación", new ColumnInfo("anioPublicacion", 150.0)); // Columna Año de Publicación
        columns.put("Categoría", new ColumnInfo("categoria.nombre", 200.0)); // Columna Categoría (mapeada al nombre)
        columns.put("Editorial", new ColumnInfo("editorial.nombre", 200.0)); // Columna Editorial (mapeada al nombre)
        columns.put("Idioma", new ColumnInfo("idioma.nombre", 150.0)); // Columna Idioma (mapeada al nombre)
        columns.put("Precio", new ColumnInfo("precio", 150.0)); // Columna Idioma (mapeada al nombre)
        columns.put("Estado", new ColumnInfo("estadoLibro.nombre", 150.0)); // Columna Estado del Libro (mapeada al nombre)


        Consumer<Libro> updateAction = (Libro libro) -> {
            System.out.println("Actualizar: " + libro);
            editForm(libro);
        };
        Consumer<Libro> deleteAction = (Libro producto) -> {System.out.println("Actualizar: " + producto);
            libroService.delete(producto.getIdLibro()); /*deletePerson(usuario);*/
            double with=stage.getWidth()/1.5;
            double h=stage.getHeight()/2;
            Toast.showToast(stage, "Se eliminó correctamente!!", 2000, with, h);
            listar();
        };

        tableViewHelper.addColumnsInOrderWithSize(tableView, columns,updateAction, deleteAction );

        tableView.setTableMenuButtonVisible(true);
        listar();

    }

    public void listar() {
        try {
            // Limpiar los ítems actuales en la tabla
            tableView.getItems().clear();

            // Obtener la lista de libros desde el servicio
            listarLibros = FXCollections.observableArrayList(libroService.list());

            // Agregar los libros obtenidos a la tabla
            tableView.getItems().addAll(listarLibros);

        } catch (Exception e) {
            System.out.println("Error al listar los libros: " + e.getMessage());
        }
    }


    public void limpiarErrores() {
        txtTitulo.getStyleClass().remove("text-field-error");
        txtAutor.getStyleClass().remove("text-field-error");
        txtIsbn.getStyleClass().remove("text-field-error");
        txtCopiasD.getStyleClass().remove("text-field-error");
        txtNPaginas.getStyleClass().remove("text-field-error");
        txtAñoPublicacion.getStyleClass().remove("text-field-error");
        txtPrecio.getStyleClass().remove("text-field-error");
        cbxCategoria.getStyleClass().remove("text-field-error");
        cbxEditorial.getStyleClass().remove("text-field-error");
        cbxIdioma.getStyleClass().remove("text-field-error");
        cbxEstadoLibro.getStyleClass().remove("text-field-error");
    }


    public void clearForm() {
        // Limpiar campos de texto
        txtTitulo.setText("");
        txtAutor.setText("");
        txtIsbn.setText("");
        txtCopiasD.setText("");
        txtNPaginas.setText("");
        txtAñoPublicacion.setText("");
        txtPrecio.setText("");

        // Restablecer selección en los ComboBox
        cbxCategoria.getSelectionModel().select(null);
        cbxEditorial.getSelectionModel().select(null);
        cbxIdioma.getSelectionModel().select(null);
        cbxEstadoLibro.getSelectionModel().select(null);

        // Restablecer ID del libro a 0 (indica que no hay edición activa)
        idLibroCE = 0L;

        // Limpiar errores visuales en el formulario
        limpiarErrores();
    }


    @FXML
    public void cancelarAccion(){
        clearForm();
        limpiarErrores();
    }

    void validarCampos(List<ConstraintViolation<Libro>> violacionesOrdenadasPorPropiedad) {
        // Crear un LinkedHashMap para ordenar las violaciones
        LinkedHashMap<String, String> erroresOrdenados = new LinkedHashMap<>();

        // Recorrer las violaciones para asociar errores a los campos correspondientes
        for (ConstraintViolation<Libro> violacion : violacionesOrdenadasPorPropiedad) {
            String campo = violacion.getPropertyPath().toString();

            switch (campo) {
                case "titulo":
                    erroresOrdenados.put("titulo", violacion.getMessage());
                    txtTitulo.getStyleClass().add("text-field-error");
                    break;

                case "autor":
                    erroresOrdenados.put("autor", violacion.getMessage());
                    txtAutor.getStyleClass().add("text-field-error");
                    break;

                case "isbn":
                    erroresOrdenados.put("isbn", violacion.getMessage());
                    txtIsbn.getStyleClass().add("text-field-error");
                    break;

                case "copiasDisponibles":
                    erroresOrdenados.put("copiasDisponibles", violacion.getMessage());
                    txtCopiasD.getStyleClass().add("text-field-error");
                    break;

                case "numeroPaginas":
                    erroresOrdenados.put("numeroPaginas", violacion.getMessage());
                    txtNPaginas.getStyleClass().add("text-field-error");
                    break;

                case "anioPublicacion":
                    erroresOrdenados.put("anioPublicacion", violacion.getMessage());
                    txtAñoPublicacion.getStyleClass().add("text-field-error");
                    break;

                case "precio":
                    erroresOrdenados.put("precio", violacion.getMessage());
                    txtPrecio.getStyleClass().add("text-field-error");
                    break;

                case "categoria":
                    erroresOrdenados.put("categoria", violacion.getMessage());
                    cbxCategoria.getStyleClass().add("text-field-error");
                    break;

                case "editorial":
                    erroresOrdenados.put("editorial", violacion.getMessage());
                    cbxEditorial.getStyleClass().add("text-field-error");
                    break;

                case "idioma":
                    erroresOrdenados.put("idioma", violacion.getMessage());
                    cbxIdioma.getStyleClass().add("text-field-error");
                    break;

                case "estadoLibro":
                    erroresOrdenados.put("estadoLibro", violacion.getMessage());
                    cbxEstadoLibro.getStyleClass().add("text-field-error");
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
        // Crear una nueva instancia de Libro
        formulario = new Libro();

        // Asignar valores desde el formulario
        formulario.setTitulo(txtTitulo.getText());
        formulario.setAutor(txtAutor.getText());
        formulario.setIsbn(txtIsbn.getText());
        formulario.setCopiasDisponibles(Integer.parseInt(txtCopiasD.getText().isEmpty() ? "0" : txtCopiasD.getText()));
        formulario.setNumPaginas(Integer.parseInt(txtNPaginas.getText().isEmpty() ? "0" : txtNPaginas.getText()));
        formulario.setAnioPublicacion(Integer.parseInt(txtAñoPublicacion.getText().isEmpty() ? "0" : txtAñoPublicacion.getText()));
        formulario.setPrecio(Double.parseDouble(txtPrecio.getText()==""?"0":txtPrecio.getText()));

        // Obtener las selecciones de los ComboBox
        String idxCategoria = cbxCategoria.getSelectionModel().getSelectedItem() == null ? "0" : cbxCategoria.getSelectionModel().getSelectedItem().getKey();
        formulario.setCategoria(categoriaService.searchById(Long.parseLong(idxCategoria)));

        String idxEditorial = cbxEditorial.getSelectionModel().getSelectedItem() == null ? "0" : cbxEditorial.getSelectionModel().getSelectedItem().getKey();
        formulario.setEditorial(editorialService.searchById(Long.parseLong(idxEditorial)));

        String idxIdioma = cbxIdioma.getSelectionModel().getSelectedItem() == null ? "0" : cbxIdioma.getSelectionModel().getSelectedItem().getKey();
        formulario.setIdioma(idiomaService.searchById(Long.parseLong(idxIdioma)));

        String idxEstadoLibro = cbxEstadoLibro.getSelectionModel().getSelectedItem() == null ? "0" : cbxEstadoLibro.getSelectionModel().getSelectedItem().getKey();
        formulario.setEstadoLibro(estadoLibroService.searchById(Long.parseLong(idxEstadoLibro)));

        // Validar el formulario usando el validador
        Set<ConstraintViolation<Libro>> violaciones = validator.validate(formulario);

        // Ordenar las violaciones por el nombre de la propiedad
        List<ConstraintViolation<Libro>> violacionesOrdenadasPorPropiedad = violaciones.stream()
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
            if (idLibroCE != 0L && idLibroCE > 0L) {
                formulario.setIdLibro(idLibroCE); // Actualizar libro existente
                libroService.update(formulario);
                Toast.showToast(stage, "Se actualizó correctamente!!", 2000, width, height);
            } else {
                libroService.save(formulario); // Guardar un nuevo libro
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

    public void editForm(Libro libro) {
        // Rellenar los campos de texto con los datos del libro
        txtTitulo.setText(libro.getTitulo());
        txtAutor.setText(libro.getAutor());
        txtIsbn.setText(libro.getIsbn());
        txtCopiasD.setText(String.valueOf(libro.getCopiasDisponibles()));
        txtNPaginas.setText(String.valueOf(libro.getNumPaginas()));
        txtAñoPublicacion.setText(String.valueOf(libro.getAnioPublicacion()));
        txtPrecio.setText(String.valueOf(libro.getAnioPublicacion()));

        // Seleccionar el ítem en cbxCategoria según el ID de Categoría
        cbxCategoria.getSelectionModel().select(
                cbxCategoria.getItems().stream()
                        .filter(categoria -> Long.parseLong(categoria.getKey()) == libro.getCategoria().getIdCategoria())
                        .findFirst()
                        .orElse(null)
        );

        // Seleccionar el ítem en cbxEditorial según el ID de Editorial
        cbxEditorial.getSelectionModel().select(
                cbxEditorial.getItems().stream()
                        .filter(editorial -> Long.parseLong(editorial.getKey()) == libro.getEditorial().getIdEditorial())
                        .findFirst()
                        .orElse(null)
        );

        // Seleccionar el ítem en cbxIdioma según el ID de Idioma
        cbxIdioma.getSelectionModel().select(
                cbxIdioma.getItems().stream()
                        .filter(idioma -> Long.parseLong(idioma.getKey()) == libro.getIdioma().getIdIdioma())
                        .findFirst()
                        .orElse(null)
        );

        // Seleccionar el ítem en cbxEstadoLibro según el ID de Estado del Libro
        cbxEstadoLibro.getSelectionModel().select(
                cbxEstadoLibro.getItems().stream()
                        .filter(estado -> Long.parseLong(estado.getKey()) == libro.getEstadoLibro().getIdEstado())
                        .findFirst()
                        .orElse(null)
        );

        // Guardar el ID del libro que se está editando
        idLibroCE = libro.getIdLibro();

        // Limpiar posibles errores previos en el formulario
        limpiarErrores();
    }




}
