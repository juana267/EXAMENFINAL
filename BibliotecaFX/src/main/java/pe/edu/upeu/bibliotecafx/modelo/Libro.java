package pe.edu.upeu.bibliotecafx.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "upeu_libro")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_libro")
    private Long idLibro;

    @NotNull(message = "El título no puede estar vacío")
    @Size(min = 2, max = 200, message = "El título debe tener entre 2 y 200 caracteres")
    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @NotNull(message = "El autor no puede estar vacío")
    @Column(name = "autor", nullable = false)
    private String autor;

    @NotNull(message = "El ISBN no puede estar vacío")
    @Size(min = 2, max = 5, message = "El ISBN debe tener entre 2 y 5 caracteres")
    @Column(name = "isbn", nullable = false, unique = true, length = 13)
    private String isbn;

    @PositiveOrZero(message = "El número de copias debe ser positivo o cero")
    @Column(name = "copias_disponibles", nullable = false)
    private Integer copiasDisponibles;

    @Positive(message = "El número de páginas debe ser positivo")
    @Column(name = "num_paginas", nullable = false)
    private Integer numPaginas;

    @Positive(message = "El año de publicación debe ser positivo")
    @Column(name = "anio_publicacion", nullable = false)
    private Integer anioPublicacion;

    @NotNull(message = "El precio no puede estar vacío")
    @Positive(message = "El precio debe ser positivo")
    @Column(name = "precio", nullable = false)
    private Double precio;


    @ManyToOne
    @JoinColumn(name = "id_categoria", referencedColumnName = "id_categoria",
            nullable = false, foreignKey = @ForeignKey(name = "FK_CATEGORIA_LIBRO"))
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "id_estado", referencedColumnName = "id_estado",
            nullable = false, foreignKey = @ForeignKey(name = "FK_ESTADO_LIBRO"))
    private EstadoLibro estadoLibro;

    @ManyToOne
    @JoinColumn(name = "id_editorial", referencedColumnName = "id_editorial",
            nullable = false, foreignKey = @ForeignKey(name = "FK_ESTADO_LIBRO"))
    private Editorial editorial;

    @ManyToOne
    @JoinColumn(name = "id_idioma", referencedColumnName = "id_idioma",
            nullable = false, foreignKey = @ForeignKey(name = "FK_ESTADO_LIBRO"))
    private Idioma idioma;

    public Long getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(Long idLibro) {
        this.idLibro = idLibro;
    }

    public @NotNull(message = "El título no puede estar vacío") @Size(min = 2, max = 200, message = "El título debe tener entre 2 y 200 caracteres") String getTitulo() {
        return titulo;
    }

    public void setTitulo(@NotNull(message = "El título no puede estar vacío") @Size(min = 2, max = 200, message = "El título debe tener entre 2 y 200 caracteres") String titulo) {
        this.titulo = titulo;
    }

    public @NotNull(message = "El autor no puede estar vacío") String getAutor() {
        return autor;
    }

    public void setAutor(@NotNull(message = "El autor no puede estar vacío") String autor) {
        this.autor = autor;
    }

    public @PositiveOrZero(message = "El número de copias debe ser positivo o cero") Integer getCopiasDisponibles() {
        return copiasDisponibles;
    }

    public void setCopiasDisponibles(@PositiveOrZero(message = "El número de copias debe ser positivo o cero") Integer copiasDisponibles) {
        this.copiasDisponibles = copiasDisponibles;
    }

    public @NotNull(message = "El ISBN no puede estar vacío") @Size(min = 2, max = 5, message = "El ISBN debe tener entre 2 y 5 caracteres") String getIsbn() {
        return isbn;
    }

    public void setIsbn(@NotNull(message = "El ISBN no puede estar vacío") @Size(min = 2, max = 5, message = "El ISBN debe tener entre 2 y 5 caracteres") String isbn) {
        this.isbn = isbn;
    }

    public @Positive(message = "El número de páginas debe ser positivo") Integer getNumPaginas() {
        return numPaginas;
    }

    public void setNumPaginas(@Positive(message = "El número de páginas debe ser positivo") Integer numPaginas) {
        this.numPaginas = numPaginas;
    }

    public @Positive(message = "El año de publicación debe ser positivo") Integer getAnioPublicacion() {
        return anioPublicacion;
    }

    public void setAnioPublicacion(@Positive(message = "El año de publicación debe ser positivo") Integer anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    public @NotNull(message = "El precio no puede estar vacío") @Positive(message = "El precio debe ser positivo") Double getPrecio() {
        return precio;
    }

    public void setPrecio(@NotNull(message = "El precio no puede estar vacío") @Positive(message = "El precio debe ser positivo") Double precio) {
        this.precio = precio;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public EstadoLibro getEstadoLibro() {
        return estadoLibro;
    }

    public void setEstadoLibro(EstadoLibro estadoLibro) {
        this.estadoLibro = estadoLibro;
    }

    public Editorial getEditorial() {
        return editorial;
    }

    public void setEditorial(Editorial editorial) {
        this.editorial = editorial;
    }

    public Idioma getIdioma() {
        return idioma;
    }

    public void setIdioma(Idioma idioma) {
        this.idioma = idioma;
    }
}

