package pe.edu.upeu.bibliotecafx.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.bibliotecafx.modelo.Usuario;

import java.util.Optional;

@Repository
public interface UsuarioRepository  extends JpaRepository<Usuario, Long> {
    @Query(value = "SELECT u.* FROM upeu_usuario u WHERE u.user=:userx ", nativeQuery = true)
    Usuario buscarUsuario(@Param("userx") String userx);

    @Query(value = "SELECT u.* FROM upeu_usuario u WHERE u.user=:user and u.clave=:clave", nativeQuery = true)
    Usuario loginUsuario(@Param("user") String user, @Param("clave") String clave);


    // Método para buscar al usuario por nombre de usuario
    @Query("SELECT u FROM Usuario u WHERE u.user = ?1")
    Usuario findByUser(String user);

        // Método para buscar un usuario por correo
        Optional<Usuario> findByCorreo(String correo);
    }