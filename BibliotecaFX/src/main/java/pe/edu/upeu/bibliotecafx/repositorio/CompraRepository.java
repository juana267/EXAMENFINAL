package pe.edu.upeu.bibliotecafx.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.bibliotecafx.modelo.Compra;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
}
