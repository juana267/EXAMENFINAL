package pe.edu.upeu.bibliotecafx.modelo;
import jakarta.persistence.*;
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
@Table(name = "upeu_cliente")
public class ClienteLibro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long idCliente;

    @NotNull(message = "El DNI no puede estar vacío")
    @Size(min = 8, max = 8, message = "El DNI debe tener 8 caracteres")
    @Column(name = "dni", nullable = false, length = 8)
    private String dni;

    @Size(max = 11, message = "El RUC no puede exceder los 11 caracteres")
    @Column(name = "ruc", length = 11)
    private String ruc;

    @NotNull(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 160, message = "El nombre debe tener entre 2 y 160 caracteres")
    @Column(name = "nombres", nullable = false, length = 160)
    private String nombres;

    @Size(max = 160, message = "El representante legal no puede exceder los 160 caracteres")
    @Column(name = "rep_legal", length = 160)
    private String repLegal;

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getRepLegal() {
        return repLegal;
    }

    public void setRepLegal(String repLegal) {
        this.repLegal = repLegal;
    }
}
