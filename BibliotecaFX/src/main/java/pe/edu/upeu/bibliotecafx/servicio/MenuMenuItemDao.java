package pe.edu.upeu.bibliotecafx.servicio;

import org.springframework.stereotype.Service;
import pe.edu.upeu.bibliotecafx.dto.MenuMenuItenTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class MenuMenuItemDao implements MenuMenuItenDaoI{

    @Override
    public List<MenuMenuItenTO> listaAccesos(String perfil, Properties idioma) {
        List<MenuMenuItenTO> lista = new ArrayList<>();
        lista.add(new MenuMenuItenTO(idioma.getProperty("menu.nombre.archivo"), "", "mifile"));
        lista.add(new MenuMenuItenTO(idioma.getProperty("menu.nombre.archivo"), "Salir", "misalir"));

        lista.add(new MenuMenuItenTO("Clientes", "Reg. Clientes", "micliente"));

        lista.add(new MenuMenuItenTO("Libro", "Reg. Libro", "mireglibro"));


        lista.add(new MenuMenuItenTO("Venta", "Reg. Boleta", "miventaboleta"));
        lista.add(new MenuMenuItenTO("Venta", "Reg. Factura", "miventafactura"));

        lista.add(new MenuMenuItenTO("Usuarios", "Con. Usuarios", "miusuarios"));




        List<MenuMenuItenTO> accesoReal = new ArrayList<>();
        switch (perfil) {
            case "Administrador":
                accesoReal.add(lista.get(0));
                accesoReal.add(lista.get(1));
                accesoReal.add(lista.get(2));
                accesoReal.add(lista.get(3));
                accesoReal.add(lista.get(4));
                break;
            case "Root":
                accesoReal = lista;
                break;
            case "Reporte":
                accesoReal.add(lista.get(0));
                accesoReal.add(lista.get(5));
                accesoReal.add(lista.get(6));
                break;
            default:
                throw new AssertionError();
        }
        return accesoReal;
    }

}
