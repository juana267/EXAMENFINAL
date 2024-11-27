package pe.edu.upeu.bibliotecafx.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "upeu_vent_carrito_boleta")
public class VentCarritoBoleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrito")
    public Long idCarrito;

    @Column(name = "dni", nullable = false, length = 12)
    public String dni;

    @ManyToOne
    @JoinColumn(name = "id_libro", nullable = false)
    public Libro libro;

    @Column(name = "nombre_libro", nullable = false, length = 120)
    public String nombreLibro;

    @Column(name = "cantidad", nullable = false)
    public Double cantidad;

    @Column(name = "punitario", nullable = false)
    public Double punitario;

    @Column(name = "ptotal", nullable = false)
    public Double ptotal;

    @Column(name = "estado", nullable = false)
    public int estado;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    public Usuario usuario;
}
