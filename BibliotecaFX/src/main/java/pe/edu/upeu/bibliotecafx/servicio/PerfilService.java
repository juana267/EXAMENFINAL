package pe.edu.upeu.bibliotecafx.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.bibliotecafx.dto.ComboBoxOption;
import pe.edu.upeu.bibliotecafx.modelo.Perfil;
import pe.edu.upeu.bibliotecafx.repositorio.PerfilRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PerfilService {
    @Autowired
    PerfilRepository repo;

    public Perfil save(Perfil to) {
        return repo.save(to);
    }

    public List<Perfil> list() {
        return repo.findAll();
    }

    public Perfil update(Perfil to, Long id) {
        try {
            Perfil toe = repo.findById(id).orElse(null);
            if (toe != null) {
                toe.setNombre(to.getNombre());
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

    public Perfil searchById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public List<ComboBoxOption> listarCombobox() {
        return repo.findAll().stream()
                .map(perfil -> new ComboBoxOption(String.valueOf(perfil.getIdPerfil()), perfil.getNombre()))
                .collect(Collectors.toList());
    }
}
