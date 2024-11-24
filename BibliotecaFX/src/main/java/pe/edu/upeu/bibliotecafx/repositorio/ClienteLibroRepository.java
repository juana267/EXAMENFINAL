package pe.edu.upeu.bibliotecafx.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import pe.edu.upeu.bibliotecafx.modelo.ClienteLibro;

import java.util.List;

@Repository
public interface ClienteLibroRepository extends JpaRepository<ClienteLibro, Long> {
    List<ClienteLibro> findByDni(String dni);
    List<ClienteLibro> findByRuc(String ruc);
}
