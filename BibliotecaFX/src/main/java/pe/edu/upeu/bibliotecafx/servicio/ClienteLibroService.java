package pe.edu.upeu.bibliotecafx.servicio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.bibliotecafx.dto.ModeloDataAutocomplet;
import pe.edu.upeu.bibliotecafx.modelo.ClienteLibro;
import pe.edu.upeu.bibliotecafx.repositorio.ClienteLibroRepository;
import java.util.Optional;


import java.util.ArrayList;
import java.util.List;

@Service
public class ClienteLibroService {

    private String dni;

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



    public ClienteLibro searchById(String dni) {
        // Buscar la lista de clientes con el mismo DNI
        List<ClienteLibro> clientes = repo.findByDni(dni);

        // Si la lista está vacía, lanzar una excepción
        if (clientes.isEmpty()) {
            throw new RuntimeException("Cliente no encontrado con DNI: " + dni);
        }

        // Si la lista contiene elementos, devolver el primer cliente encontrado
        return clientes.get(0);
    }


    public List<ModeloDataAutocomplet> listAutoCompletClienteDni() {
        List<ModeloDataAutocomplet> listarclientes = new ArrayList<>();
        try {
            for (ClienteLibro cliente : repo.findAll()) {
                ModeloDataAutocomplet data = new ModeloDataAutocomplet();
                data.setIdx(String.valueOf(cliente.getDni()));
                data.setNameDysplay(cliente.getNombres());
                data.setOtherData(cliente.getRepLegal());
                listarclientes.add(data);
            }
        } catch (Exception e) {
            logger.error("Error durante la operación", e);
        }
        return listarclientes;
    }

}
