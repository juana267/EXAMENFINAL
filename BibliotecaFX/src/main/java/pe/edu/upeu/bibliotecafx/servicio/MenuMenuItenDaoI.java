package pe.edu.upeu.bibliotecafx.servicio;

import pe.edu.upeu.bibliotecafx.dto.MenuMenuItenTO;

import java.util.List;
import java.util.Properties;

public interface MenuMenuItenDaoI {

    public List<MenuMenuItenTO> listaAccesos(String perfil, Properties idioma);

}
