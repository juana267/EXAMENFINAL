package pe.edu.upeu.bibliotecafx.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.bibliotecafx.modelo.Idioma;

@Repository
public interface IdiomaRepository extends JpaRepository<Idioma, Long> {
}
