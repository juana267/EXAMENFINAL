package pe.edu.upeu.bibliotecafx.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Size;

@Entity
@Table(name = "upeu_estado_libro")
public class EstadoLibro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    private Long idEstado;

    @NotNull(message = "El estado no puede estar vacío")
    @Size(min = 2, max = 50, message = "El estado debe tener entre 2 y 50 caracteres")
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    public Long getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Long idEstado) {
        this.idEstado = idEstado;
    }

    public @NotNull(message = "El estado no puede estar vacío") @Size(min = 2, max = 50, message = "El estado debe tener entre 2 y 50 caracteres") String getNombre() {
        return nombre;
    }

    public void setNombre(@NotNull(message = "El estado no puede estar vacío") @Size(min = 2, max = 50, message = "El estado debe tener entre 2 y 50 caracteres") String nombre) {
        this.nombre = nombre;
    }
}
