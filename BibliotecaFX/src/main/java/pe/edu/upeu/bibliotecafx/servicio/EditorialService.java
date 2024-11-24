package pe.edu.upeu.bibliotecafx.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.bibliotecafx.dto.ComboBoxOption;
import pe.edu.upeu.bibliotecafx.modelo.Editorial;
import pe.edu.upeu.bibliotecafx.repositorio.EditorialRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class EditorialService {

    @Autowired
    EditorialRepository repo;

    //C
    public Editorial save(Editorial to){
        return repo.save(to);
    }

    //R
    public List<Editorial> list(){
        return repo.findAll();
    }
    //U
    public Editorial update(Editorial to, Long id){
        try {
            Editorial toe=repo.findById(id).get();
            if(toe!=null){
                toe.setNombre(to.getNombre());
            }
            return repo.save(toe);
        }catch (Exception e){
            System.out.println("Error: "+ e.getMessage());
        }
        return null;
    }

    public Editorial update(Editorial to){
        return repo.save(to);
    }

    //D
    public void delete(Long id){
        repo.deleteById(id);
    }
    //B
    public Editorial searchById(Long id){
        return repo.findById(id).orElse(null);
    }


    public List<ComboBoxOption> listarCombobox(){
        List<ComboBoxOption> listar=new ArrayList<>();
        ComboBoxOption cb;
        for(Editorial cate : repo.findAll()) {
            cb=new ComboBoxOption();
            cb.setKey(String.valueOf(cate.getIdEditorial()));
            cb.setValue(cate.getNombre());
            listar.add(cb);
        }
        return listar;
    }


}

