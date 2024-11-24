package pe.edu.upeu.bibliotecafx.servicio;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.bibliotecafx.modelo.VentaBoleta;
import pe.edu.upeu.bibliotecafx.repositorio.VentaBoletaRepository;

import javax.sql.DataSource;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@Service
public class VentaBoletaService {

        @Autowired
        VentaBoletaRepository repo;

        @Autowired
        DataSource dataSource;

        public VentaBoleta save(VentaBoleta to) {
            return repo.save(to);
        }

        public List<VentaBoleta> list() {
            return repo.findAll();
        }

        public VentaBoleta update(VentaBoleta to, Long id) {
            try {
                VentaBoleta toe = repo.findById(id).orElse(null);
                if (toe != null) {
                    toe.setSerie(to.getSerie());
                    return repo.save(toe);
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            return null;
        }

        public void delete(Long id) {
            repo.deleteById(id);
        }

        public VentaBoleta searchById(Long id) {
            return repo.findById(id).orElse(null);
        }

        public File getFile(String filex) {
            File newFolder = new File("jasper");
            String ruta = newFolder.getAbsolutePath();
            //CAMINO = Paths.get(ruta+"/"+"reporte1.jrxml");
            Path CAMINO = Paths.get(ruta + "/" + filex);
            System.out.println("Llegasss Ruta 2:" + CAMINO.toAbsolutePath().toFile());
            return CAMINO.toFile();
        }
        public JasperPrint runReport(Long idv) throws JRException, SQLException {
            // Verificar si la venta existe
            if (!repo.existsById(idv)) {
                throw new IllegalArgumentException("La venta con id " + idv + " no existe");
            }
            HashMap<String, Object> param = new HashMap<>();
            // Obtener ruta de la imagen
            String imgen = getFile("logoupeu.png").getAbsolutePath();
            String urljasper=getFile("detallev.jasper").getAbsolutePath();
            // Agregar parámetros
            param.put("idventa", idv);
            param.put("imagenurl", imgen);
            param.put("urljasper", urljasper);
            // Cargar el diseño del informe
            JasperDesign jdesign = JRXmlLoader.load(getFile("comprobante.jrxml"));
            JasperReport jreport = JasperCompileManager.compileReport(jdesign);
            // Llenar el informe
            return JasperFillManager.fillReport(jreport, param, dataSource.getConnection());
        }

    }
