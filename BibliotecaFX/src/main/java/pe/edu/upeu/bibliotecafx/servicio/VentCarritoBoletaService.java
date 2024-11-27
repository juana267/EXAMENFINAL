package pe.edu.upeu.bibliotecafx.servicio;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.bibliotecafx.modelo.VentCarritoBoleta;
import pe.edu.upeu.bibliotecafx.repositorio.VentCarritoBoletaRepository;

import java.util.List;

@Service

public class VentCarritoBoletaService {


    @Autowired
    VentCarritoBoletaRepository repo;

    public VentCarritoBoleta save(VentCarritoBoleta to) {
        return repo.save(to);
    }

    public List<VentCarritoBoleta> list() {
        return repo.findAll();
    }

    public VentCarritoBoleta update(VentCarritoBoleta to, Long id) {
        try {
            VentCarritoBoleta toe = repo.findById(id).orElse(null);
            if (toe != null) {
                toe.setEstado(to.getEstado());
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

    public VentCarritoBoleta searchById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public List<VentCarritoBoleta> listaCarritoCliente(String dni) {
        return repo.listaCarritoCliente(dni);
    }

    @Transactional
    public void deleteCarAll(String dni) {
        this.repo.deleteByDni(dni);
    }

}