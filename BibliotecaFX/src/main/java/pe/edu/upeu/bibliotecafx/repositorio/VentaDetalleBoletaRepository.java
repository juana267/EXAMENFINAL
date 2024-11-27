
package pe.edu.upeu.bibliotecafx.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.bibliotecafx.modelo.VentaBoletaDetalle;

@Repository
public interface  VentaDetalleBoletaRepository extends JpaRepository<VentaBoletaDetalle, Long> {
}