package pe.edu.upeu.bibliotecafx.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "upeu_usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @NotNull(message = "El nombre de usuario no puede estar vacío")
    @Size(min = 2, max = 20, message = "El nombre de usuario debe tener entre 2 y 20 caracteres")
    @Column(name = "user", nullable = false, unique = true, length = 20)
    private String user;

    @NotNull(message = "La clave no puede estar vacía")
    @Size(min = 6, message = "La clave debe tener al menos 6 caracteres")
    @Column(name = "clave", nullable = false, length = 100)
    private String clave;

    @NotNull(message = "El estado no puede estar vacío")
    @Column(name = "estado", nullable = false, length = 10)
    private String estado;

    @NotNull(message = "El correo no puede estar vacío")
    @Email(message = "El correo debe ser una dirección de correo válida")
    @Column(name = "correo", nullable = false, unique = true, length = 100)
    private String correo;

    @ManyToOne
    @JoinColumn(
            name = "id_perfil",
            referencedColumnName = "id_perfil",
            nullable = true, // Permitir valores NULL
            foreignKey = @ForeignKey(name = "FK_PERFIL_USUARIO")
    )
    private Perfil idPerfil;

}
