package pe.edu.upeu.bibliotecafx.confi;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "pe.edu.upeu.bibliotecafx")  // Asegúrate de que esté buscando en el paquete correcto
public class AppConfig {
}

