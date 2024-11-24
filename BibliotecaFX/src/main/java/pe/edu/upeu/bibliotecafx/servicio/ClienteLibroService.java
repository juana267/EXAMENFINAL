package pe.edu.upeu.bibliotecafx.servicio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.bibliotecafx.dto.ModeloDataAutocomplet;
import pe.edu.upeu.bibliotecafx.modelo.ClienteLibro;
import pe.edu.upeu.bibliotecafx.repositorio.ClienteLibroRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClienteLibroService {
    @Autowired
    private ClienteLibroRepository repo;
    private Logger logger = LoggerFactory.getLogger(ClienteLibroService.class);

    // Crear un nuevo cliente
    public ClienteLibro save(ClienteLibro cliente) {
        try {
            return repo.save(cliente);
        } catch (Exception e) {
            logger.error("Error al crear cliente", e);
        }
        return null;
    }

    // Listar todos los clientes
    public List<ClienteLibro> list() {
        try {
            return repo.findAll();
        } catch (Exception e) {
            logger.error("Error al listar clientes", e);
        }
        return new ArrayList<>();
    }

    // Actualizar cliente existente por ID
    public ClienteLibro update(Long id, ClienteLibro cliente) {  // Aquí se recibe el ID
        try {
            ClienteLibro existingCliente = repo.findById(id).orElse(null);  // Usar el ID pasado como parámetro
            if (existingCliente != null) {
                existingCliente.setDni(cliente.getDni());
                existingCliente.setRuc(cliente.getRuc());
                existingCliente.setNombres(cliente.getNombres());
                existingCliente.setRepLegal(cliente.getRepLegal());
                return repo.save(existingCliente);
            }
        } catch (Exception e) {
            logger.error("Error al actualizar cliente", e);
        }
        return null;
    }
    // Eliminar cliente por ID
    public boolean delete(Long id) {
        try {
            repo.deleteById(id);
            return true;
        } catch (Exception e) {
            logger.error("Error al eliminar cliente", e);
        }
        return false;
    }

    // Buscar cliente por ID
    public ClienteLibro searchById(Long id) {
        try {
            return repo.findById(id).orElse(null);
        } catch (Exception e) {
            logger.error("Error al buscar cliente por ID", e);
        }
        return null;
    }

    // Autocompletado para DNI y RUC
    public List<ModeloDataAutocomplet> listAutoCompletClienteLibro(String query) {
        List<ModeloDataAutocomplet> listarClienteLibro = new ArrayList<>();
        try {
            if (query.length() == 8) {  // Caso para buscar por DNI
                List<ClienteLibro> clientes = repo.findByDni(query);
                for (ClienteLibro cliente : clientes) {
                    ModeloDataAutocomplet data = new ModeloDataAutocomplet();
                    data.setIdx(String.valueOf(cliente.getIdCliente()));
                    data.setNameDysplay(cliente.getDni());
                    data.setOtherData(cliente.getNombres()+":"+cliente.getRepLegal());
                    listarClienteLibro.add(data);
                }
            } else if (query.length() == 11) {  // Caso para buscar por RUC
                List<ClienteLibro> clientes = repo.findByRuc(query);
                for (ClienteLibro cliente : clientes) {
                    ModeloDataAutocomplet data = new ModeloDataAutocomplet();
                    data.setIdx(String.valueOf(cliente.getIdCliente()));
                    data.setNameDysplay(cliente.getRuc());
                    data.setOtherData(cliente.getNombres()+":"+cliente.getRepLegal());
                    listarClienteLibro.add(data);
                }
            }
        } catch (Exception e) {
            logger.error("Error al realizar la busqueda", e);
        }
        return listarClienteLibro;
    }
}
