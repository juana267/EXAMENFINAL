package pe.edu.upeu.bibliotecafx.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.bibliotecafx.dto.ComboBoxOption;
import pe.edu.upeu.bibliotecafx.modelo.EstadoLibro;
import pe.edu.upeu.bibliotecafx.repositorio.EstadoLibroRepository;

import java.util.ArrayList;
import java.util.List;
@Service
public class EstadoLibroService {

        @Autowired
        EstadoLibroRepository repo;

        //C
        public EstadoLibro save(EstadoLibro to){
            return repo.save(to);
        }

        //R
        public List<EstadoLibro> list(){
            return repo.findAll();
        }
        //U
        public EstadoLibro update(EstadoLibro to, Long id){
            try {
                EstadoLibro toe=repo.findById(id).get();
                if(toe!=null){
                    toe.setNombre(to.getNombre());
                }
                return repo.save(toe);
            }catch (Exception e){
                System.out.println("Error: "+ e.getMessage());
            }
            return null;
        }

        public EstadoLibro update(EstadoLibro to){
            return repo.save(to);
        }

        //D
        public void delete(Long id){
            repo.deleteById(id);
        }
        //B
        public EstadoLibro searchById(Long id){
            return repo.findById(id).orElse(null);
        }


        public List<ComboBoxOption> listarCombobox(){
            List<ComboBoxOption> listar=new ArrayList<>();
            ComboBoxOption cb;
            for(EstadoLibro cate : repo.findAll()) {
                cb=new ComboBoxOption();
                cb.setKey(String.valueOf(cate.getIdEstado()));
                cb.setValue(cate.getNombre());
                listar.add(cb);
            }
            return listar;
        }


    }

