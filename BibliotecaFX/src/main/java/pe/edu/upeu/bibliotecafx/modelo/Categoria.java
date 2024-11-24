package pe.edu.upeu.bibliotecafx.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Size;


@Entity
@Table(name = "upeu_categoria")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Long idCategoria;

    @NotNull(message = "El nombre de la categoría no puede estar vacío")
    @Size(min = 2, max = 100, message = "El nombre de la categoría debe tener entre 2 y 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public @NotNull(message = "El nombre de la categoría no puede estar vacío") @Size(min = 2, max = 100, message = "El nombre de la categoría debe tener entre 2 y 100 caracteres") String getNombre() {
        return nombre;
    }

    public void setNombre(@NotNull(message = "El nombre de la categoría no puede estar vacío") @Size(min = 2, max = 100, message = "El nombre de la categoría debe tener entre 2 y 100 caracteres") String nombre) {
        this.nombre = nombre;
    }
}
