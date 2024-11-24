package pe.edu.upeu.bibliotecafx.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Size;


@Entity
@Table(name = "upeu_idioma")
public class Idioma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_idioma")
    private Long idIdioma;

    @NotNull(message = "El nombre de la idioma no puede estar vacío")
    @Size(min = 2, max = 100, message = "El nombre del idioma debe tener entre 2 y 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    public Long getIdIdioma() {
        return idIdioma;
    }

    public void setIdIdioma(Long idIdioma) {
        this.idIdioma = idIdioma;
    }

    public @NotNull(message = "El nombre de la idioma no puede estar vacío") @Size(min = 2, max = 100, message = "El nombre del idioma debe tener entre 2 y 100 caracteres") String getNombre() {
        return nombre;
    }

    public void setNombre(@NotNull(message = "El nombre de la idioma no puede estar vacío") @Size(min = 2, max = 100, message = "El nombre del idioma debe tener entre 2 y 100 caracteres") String nombre) {
        this.nombre = nombre;
    }
}
