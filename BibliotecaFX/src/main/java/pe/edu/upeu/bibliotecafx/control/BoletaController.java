package pe.edu.upeu.bibliotecafx.control;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.edu.upeu.bibliotecafx.componente.*;
import pe.edu.upeu.bibliotecafx.dto.ModeloDataAutocomplet;
import pe.edu.upeu.bibliotecafx.dto.SessionManager;
import pe.edu.upeu.bibliotecafx.modelo.VentCarritoBoleta;
import pe.edu.upeu.bibliotecafx.modelo.VentaBoleta;
import pe.edu.upeu.bibliotecafx.modelo.VentaBoletaDetalle;
import pe.edu.upeu.bibliotecafx.servicio.*;

        import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
        import java.util.function.Consumer;

@Component
public class BoletaController {
    @FXML
    TextField autocompLibro;
    @FXML
    TextField nombreLibro, codigoLibro, stockLibro, cantidadLibro, precio, preTLibro, txtBaseImp, txtIgv, txtDescuento, txtImporteT;
    @FXML
    Button btnRegVenta, btnRegCarrito, btnFormClienteLibro;
    @FXML
    TextField autocompClienteLibro;
    @FXML
    TextField razonSocial;
    @FXML
    TextField dni;
    @FXML
    TableView<VentCarritoBoleta> tableView;
    AutoCompleteTextField actf;
    ModeloDataAutocomplet lastLibro;
    AutoCompleteTextField actfC;
    ModeloDataAutocomplet lastClienteLibro;
    @Autowired
    LibroService libroService;
    @Autowired
    ClienteLibroService cs;
    @Autowired
    VentCarritoBoletaService ventCarritoBoletaService;
    @Autowired
    UsuarioService daoU;
    @Autowired
    VentaBoletaService daoV;
    @Autowired
    VentaBoletaDetalleService daoVD;
    Stage stage;
    @FXML
    private AnchorPane miContenedor;
    private JasperPrint jasperPrint;
    private final SortedSet<ModeloDataAutocomplet> entries = new TreeSet<>((ModeloDataAutocomplet o1, ModeloDataAutocomplet o2) -> o1.toString().compareTo(o2.toString()));
    private final SortedSet<ModeloDataAutocomplet> entriesC = new TreeSet<>((ModeloDataAutocomplet o1, ModeloDataAutocomplet o2) -> o1.toString().compareTo(o2.toString()));

    @FXML
    public void initialize() {

        Platform.runLater(() -> {
            stage = (Stage) miContenedor.getScene().getWindow();
            System.out.println("El título del stage es: " + stage.getTitle());
        });

        listarClienteLibro(); // Pasa un filtro vacío
        actfC = new AutoCompleteTextField<>(entriesC, autocompClienteLibro);
        autocompClienteLibro.setOnKeyReleased(e -> {
            lastClienteLibro = (ModeloDataAutocomplet) actfC.getLastSelectedObject();
            if (lastClienteLibro != null) {
                System.out.println(lastClienteLibro.getNameDysplay());
                razonSocial.setText(lastClienteLibro.getNameDysplay());
                dni.setText(lastClienteLibro.getIdx());
                listar();
            }
        });

        listarLibro();
        actf = new AutoCompleteTextField<>(entries, autocompLibro);
        autocompLibro.setOnKeyReleased(e -> {
            lastLibro = (ModeloDataAutocomplet) actf.getLastSelectedObject();
            if (lastLibro != null) {
                System.out.println(lastLibro.getNameDysplay());
                nombreLibro.setText(lastLibro.getNameDysplay());
                codigoLibro.setText(lastLibro.getIdx());
                String[] dato = lastLibro.getOtherData().split(":");
                precio.setText(dato[0]);
                stockLibro.setText(dato[1]);
            }
        });

        personalizarTabla();
        btnRegCarrito.setDisable(true);
    }

    public void listarLibro() {
        entries.addAll(libroService.listAutoCompletLibro());
    }

    public void listarClienteLibro() {
        entriesC.addAll(cs.listAutoCompletClienteDni());
    }

    public void personalizarTabla() {
        // Crear instancia de la clase genérica TableViewHelper
        TableViewHelper<VentCarritoBoleta> tableViewHelper = new TableViewHelper<>();
        // Definir las columnas dinámicamente en un mapa (nombre visible -> campo del modelo)
        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        columns.put("ID Libro", new ColumnInfo("libro.idLibro", 100.0)); // Columna visible "Columna 1" mapea al campo "campo1"
        columns.put("Título", new ColumnInfo("nombreLibro", 300.0)); // Columna visible "Columna 1" mapea al campo "campo1"
        columns.put("Cantidad", new ColumnInfo("cantidad", 60.0)); // Columna visible "Columna 2" mapea al campo "campo2"
        columns.put("P.Unitario", new ColumnInfo("punitario", 100.0)); // Columna visible "Columna 2" mapea al campo "campo2"
        columns.put("P.Total", new ColumnInfo("ptotal", 100.0)); // Columna visible "Columna 2" mapea al campo "campo2"
        // Definir las acciones de actualizar y eliminar
        Consumer<VentCarritoBoleta> updateAction = (VentCarritoBoleta VentCarritoBoleta) -> {
            System.out.println("Actualizar: " + VentCarritoBoleta);
        };
        Consumer<VentCarritoBoleta> deleteAction = (VentCarritoBoleta VentCarritoBoleta) -> {
            deleteReg(VentCarritoBoleta);
        };
        // Usar el helper para agregar las columnas en el orden correcto
        tableViewHelper.addColumnsInOrderWithSize(tableView, columns, updateAction, deleteAction);
        // Agregar botones de eliminar y modificar
        tableView.setTableMenuButtonVisible(true);
    }

    public void listar() {
        tableView.getItems().clear();
        List<VentCarritoBoleta> lista = ventCarritoBoletaService. listaCarritoCliente(dni.getText());
        double impoTotal = 0, igv = 0;
        for (VentCarritoBoleta dato : lista) {
            impoTotal += Double.parseDouble(String.valueOf(dato.getPtotal()));
        }
        txtImporteT.setText(String.valueOf(impoTotal));
        double pv = impoTotal / 1.18;
        txtBaseImp.setText(String.valueOf(Math.round(pv * 100.0) / 100.0));
        txtIgv.setText(String.valueOf(Math.round((pv * 0.18) * 100.0) / 100.0));
        tableView.getItems().addAll(lista);
    }

    public void editVenCarrito(VentCarritoBoleta obj) {
        System.out.println(obj.getDni());
    }

    public void deleteReg(VentCarritoBoleta obj) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("Confirmar acción");
        alert.setContentText("¿Estás seguro de que deseas eliminar este elemento?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ventCarritoBoletaService.delete(obj.getIdCarrito());
            Stage stage = StageManager.getPrimaryStage();
            double with = stage.getMaxWidth() / 2;
            Toast.showToast(stage, "¡Acción completada!", 2000, with, 50);
            listar();
        } else {
            System.out.println("Acción cancelada");
        }
    }

    @FXML
    private void calcularPT() {
        if (!cantidadLibro.getText().equals("")) {
            double punit = Double.parseDouble(precio.getText());
            double cantidad = Double.parseDouble(cantidadLibro.getText());
            double dato = punit * cantidad;


            // Imprime los valores para depuración
            System.out.println("Precio Unitario: " + punit);
            System.out.println("Cantidad: " + cantidad);
            System.out.println("Precio Total Calculado: " + dato);

            preTLibro.setText(String.valueOf(dato));  // Asignar el valor calculado a preTLibro
            if (cantidad > 0.0) {
                btnRegCarrito.setDisable(false);
            } else {
                btnRegCarrito.setDisable(true);
            }
        } else {
            btnRegCarrito.setDisable(true);
        }
    }


    @FXML
    private void registarPCarrito() {
        try {
            VentCarritoBoleta ss = VentCarritoBoleta.builder()
                    .dni(dni.getText())
                    .libro(libroService.searchById(Long.parseLong(codigoLibro.getText())))
                    .nombreLibro(nombreLibro.getText())
                    .cantidad(Double.parseDouble(cantidadLibro.getText()))
                    .punitario(Double.parseDouble(precio.getText()))
                    .ptotal(Double.parseDouble(preTLibro.getText()))
                    .estado(1)
                    .usuario(daoU.searchById(SessionManager.getInstance().getUserId()))
                    .build();
            ventCarritoBoletaService.save(ss);
            listar();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void registrarVenta(){
        Locale locale = new Locale("es", "es-PE");
        LocalDateTime localDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss", locale);
        String fechaFormateada = localDate.format(formatter);
        VentaBoleta.VentaBoletaBuilder builder = VentaBoleta.builder();
        builder.clienteLibro(cs.searchById(dni.getText()));
        builder.precioBase(Double.parseDouble(txtBaseImp.getText()));
        builder.igv(Double.parseDouble(txtIgv.getText()));
        builder.precioTotal(Double.parseDouble(txtImporteT.getText()));
        builder.usuario(daoU.searchById(SessionManager.getInstance().getUserId()));
        builder.serie("V");
        builder.tipoDoc("Boleta");
        builder.fechaGener(localDate.parse(fechaFormateada, formatter));
        builder.numDoc("00");
        VentaBoleta to= builder
                .build();
        VentaBoleta idX = daoV.save(to);
        List<VentCarritoBoleta> dd = ventCarritoBoletaService.listaCarritoCliente(dni.getText());
        if (idX.getIdVenta() != 0) {
            for (VentCarritoBoleta car : dd) {
                VentaBoletaDetalle vd = VentaBoletaDetalle.builder()
                        .venta(idX)
                        .libro(libroService.searchById(car.libro.getIdLibro()))
                        .cantidad(car.getCantidad())
                        .descuento(0.0)
                        .pu(car.getPunitario())
                        .subtotal(car.getPtotal())
                        .build();
                daoVD.save(vd);
            }
        }
        ventCarritoBoletaService.deleteCarAll(dni.getText());
        listar();
        try {
            jasperPrint= daoV.runReport(Long.parseLong(String.valueOf(idX.getIdVenta())));
            Platform.runLater(() -> {
                ReportAlert reportAlert=new ReportAlert(jasperPrint);
                reportAlert.show();
                //ReportDialog reportDialog = new ReportDialog(jasperPrint);
                //reportDialog.show();
            });
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}