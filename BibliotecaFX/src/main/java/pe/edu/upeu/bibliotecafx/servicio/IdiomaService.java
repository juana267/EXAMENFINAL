package pe.edu.upeu.bibliotecafx.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.bibliotecafx.dto.ComboBoxOption;
import pe.edu.upeu.bibliotecafx.modelo.Idioma;
import pe.edu.upeu.bibliotecafx.repositorio.IdiomaRepository;

import java.util.ArrayList;
import java.util.List;
@Service
public class IdiomaService {

    @Autowired
    IdiomaRepository repo;

    //C
    public Idioma save(Idioma to){
        return repo.save(to);
    }

    //R
    public List<Idioma> list(){
        return repo.findAll();
    }
    //U
    public Idioma update(Idioma to, Long id){
        try {
            Idioma toe=repo.findById(id).get();
            if(toe!=null){
                toe.setNombre(to.getNombre());
            }
            return repo.save(toe);
        }catch (Exception e){
            System.out.println("Error: "+ e.getMessage());
        }
        return null;
    }

    public Idioma update(Idioma to){
        return repo.save(to);
    }

    //D
    public void delete(Long id){
        repo.deleteById(id);
    }
    //B
    public Idioma searchById(Long id){
        return repo.findById(id).orElse(null);
    }


    public List<ComboBoxOption> listarCombobox(){
        List<ComboBoxOption> listar=new ArrayList<>();
        ComboBoxOption cb;
        for(Idioma cate : repo.findAll()) {
            cb=new ComboBoxOption();
            cb.setKey(String.valueOf(cate.getIdIdioma()));
            cb.setValue(cate.getNombre());
            listar.add(cb);
        }
        return listar;
    }


}

