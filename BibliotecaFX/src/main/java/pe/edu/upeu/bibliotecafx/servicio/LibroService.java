package pe.edu.upeu.bibliotecafx.servicio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.bibliotecafx.dto.ModeloDataAutocomplet;
import pe.edu.upeu.bibliotecafx.modelo.Libro;
import pe.edu.upeu.bibliotecafx.repositorio.LibroRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class LibroService {

    @Autowired
    LibroRepository repo;

    private Logger logger = LoggerFactory.getLogger(LibroService.class);


    public Libro save(Libro to){
        return repo.save(to);
    }
    public List<Libro> list(){
        return repo.findAll();
    }
    public Libro update(Libro to, Long id){
        try {
            Libro toe=repo.findById(id).get();
            if(toe!=null){
                toe.setTitulo(to.getTitulo());
            }
            return repo.save(toe);
        }catch (Exception e){
            System.out.println("Error: "+ e.getMessage());
        }
        return null;
    }

    public Libro update(Libro to){
        return repo.save(to);
    }
    public void delete(Long id){
        repo.deleteById(id);
    }
    public Libro searchById(Long id){
        return repo.findById(id).get();
    }

    public List<ModeloDataAutocomplet> listAutoCompletLibro() {
        List<ModeloDataAutocomplet> listarProducto = new ArrayList<>();
        try {
            for (Libro libro : repo.findAll()) {
                ModeloDataAutocomplet data = new ModeloDataAutocomplet();
                data.setIdx(String.valueOf(libro.getIdLibro()));
                data.setNameDysplay(libro.getTitulo());
                data.setOtherData(libro.getPrecio() + ":" + libro.getCopiasDisponibles());
                listarProducto.add(data);
            }
        } catch (Exception e) {
            logger.error("Error al realizar la busqueda", e);
        }
        return listarProducto;
    }

}

