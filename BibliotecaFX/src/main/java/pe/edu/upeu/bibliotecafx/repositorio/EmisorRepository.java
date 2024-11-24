package pe.edu.upeu.bibliotecafx.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.bibliotecafx.modelo.Emisor;

@Repository
public interface EmisorRepository extends JpaRepository<Emisor, Long> {
}
