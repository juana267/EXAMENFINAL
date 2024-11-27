package pe.edu.upeu.bibliotecafx.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.bibliotecafx.modelo.VentCarritoBoleta;


import java.util.List;

@Repository

public interface VentCarritoBoletaRepository extends JpaRepository<VentCarritoBoleta, Long> {
    @Query(value = "SELECT c.* FROM upeu_vent_carrito_boleta c WHERE c.dni=:dni", nativeQuery = true)
    List<VentCarritoBoleta> listaCarritoCliente(@Param("dni") String dniruc);

    void deleteByDni(String dniruc);
}