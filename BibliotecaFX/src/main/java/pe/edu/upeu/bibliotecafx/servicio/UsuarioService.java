package pe.edu.upeu.bibliotecafx.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pe.edu.upeu.bibliotecafx.modelo.Usuario;
import pe.edu.upeu.bibliotecafx.repositorio.UsuarioRepository;

import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    UsuarioRepository repo;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public Usuario save(Usuario to) {
        return repo.save(to);
    }

    public List<Usuario> list() {
        return repo.findAll();
    }

    public Usuario update(Usuario to, Long id) {
        try {
            Usuario toe = repo.findById(id).orElse(null);
            if (toe != null) {
                toe.setIdPerfil(to.getIdPerfil());
                return repo.save(toe);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }


    public void delete(Long id) {
        repo.deleteById(id);
    }

    public Usuario searchById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Usuario loginUsuario(String user, String clave) {
        // Buscar el usuario por nombre de usuario
        Usuario usuario = repo.findByUser(user);

        if (usuario != null) {
            // Comparar la contraseña ingresada con la almacenada en la base de datos
            boolean contrasenaCorrecta = passwordEncoder.matches(clave, usuario.getClave());
            if (contrasenaCorrecta) {
                return usuario;
            } else {
                return null; // La contraseña es incorrecta
            }
        } else {
            return null; // El usuario no existe
        }
    }


            // Método para verificar si un correo es único
        public boolean isCorreoUnico(String correo) {
            return repo.findByCorreo(correo).isEmpty();
        }

}
