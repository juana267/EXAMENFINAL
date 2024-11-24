package pe.edu.upeu.bibliotecafx.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "upeu_editorial")
public class Editorial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_editorial")
    private Long idEditorial;

    @NotNull(message = "El nombre de la Editorial no puede estar vacío")
    @Size(min = 2, max = 100, message = "El nombre de la Editorial debe tener entre 2 y 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    public Long getIdEditorial() {
        return idEditorial;
    }

    public void setIdEditorial(Long idEditorial) {
        this.idEditorial = idEditorial;
    }

    public @NotNull(message = "El nombre de la Editorial no puede estar vacío") @Size(min = 2, max = 100, message = "El nombre de la Editorial debe tener entre 2 y 100 caracteres") String getNombre() {
        return nombre;
    }

    public void setNombre(@NotNull(message = "El nombre de la Editorial no puede estar vacío") @Size(min = 2, max = 100, message = "El nombre de la Editorial debe tener entre 2 y 100 caracteres") String nombre) {
        this.nombre = nombre;
    }
}
