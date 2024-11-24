package pe.edu.upeu.bibliotecafx.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.bibliotecafx.modelo.VentaBoleta;


@Repository

public interface VentaBoletaRepository extends JpaRepository<VentaBoleta, Long> {
}
