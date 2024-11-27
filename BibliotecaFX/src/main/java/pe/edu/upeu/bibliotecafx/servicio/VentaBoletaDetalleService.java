package pe.edu.upeu.bibliotecafx.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.bibliotecafx.modelo.VentaBoletaDetalle;
import pe.edu.upeu.bibliotecafx.repositorio.VentaDetalleBoletaRepository;

import java.util.List;

@Service
public class VentaBoletaDetalleService {
    @Autowired
    VentaDetalleBoletaRepository repo;

    public VentaBoletaDetalle save(VentaBoletaDetalle to) {
        return repo.save(to);
    }

    public List<VentaBoletaDetalle> list() {
        return repo.findAll();
    }

    public VentaBoletaDetalle update(VentaBoletaDetalle to, Long id) {
        try {
            VentaBoletaDetalle toe = repo.findById(id).orElse(null);
            if (toe != null) {
                toe.setCantidad(to.getCantidad());
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

    public VentaBoletaDetalle searchById(Long id) {
        return repo.findById(id).orElse(null);
    }
}