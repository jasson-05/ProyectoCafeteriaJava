package esfe.Persistencia;

import esfe.dominio.Menu;
import esfe.Persistencia.MenuDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MenuDAOTest {
    private MenuDAO menuDAO;

    @BeforeEach
    void setUp() {
        menuDAO = new MenuDAO();
    }

    private Menu create(Menu menu) throws SQLException {
        Menu res = menuDAO.create(menu);

        assertNotNull(res, "El menú creado no debería ser nulo.");
        assertEquals(menu.getNompro(), res.getNompro(), "El nombre del producto debe coincidir.");
        assertEquals(menu.getPrecio(), res.getPrecio(), 0.001, "El precio del producto debe coincidir.");
        assertEquals(menu.getCategoria(), res.getCategoria(), "La categoría debe coincidir.");
        assertEquals(menu.getTipo(), res.getTipo(), "El tipo debe coincidir.");

        return res;
    }

    private void update(Menu menu) throws SQLException {
        menu.setNompro(menu.getNompro() + "_mod");
        menu.setPrecio(menu.getPrecio() + 1.0f);
        menu.setCategoria("CategoriaMod");
        menu.setTipo("TipoMod");

        boolean res = menuDAO.update(menu);

        assertTrue(res, "La actualización del menú debería ser exitosa.");

        getById(menu);
    }

    private void getById(Menu menu) throws SQLException {
        Menu res = menuDAO.getById(menu.getCodpro());

        assertNotNull(res, "El menú obtenido no debería ser nulo.");
        assertEquals(menu.getCodpro(), res.getCodpro(), "El ID del producto debe coincidir.");
        assertEquals(menu.getNompro(), res.getNompro(), "El nombre del producto debe coincidir.");
        assertEquals(menu.getPrecio(), res.getPrecio(), 0.001, "El precio debe coincidir.");
        assertEquals(menu.getCategoria(), res.getCategoria(), "La categoría debe coincidir.");
        assertEquals(menu.getTipo(), res.getTipo(), "El tipo debe coincidir.");
    }

    private void search(Menu menu) throws SQLException {
        ArrayList<Menu> menus = menuDAO.search(menu.getNompro());
        boolean found = false;

        for (Menu m : menus) {
            if (m.getNompro().contains(menu.getNompro())) {
                found = true;
            } else {
                found = false;
                break;
            }
        }

        assertTrue(found, "No se encontró un menú que coincida con el nombre: " + menu.getNompro());
    }

    private void delete(Menu menu) throws SQLException {
        boolean res = menuDAO.delete(menu);
        assertTrue(res, "La eliminación del menú debería ser exitosa.");

        Menu deleted = menuDAO.getById(menu.getCodpro());
        assertNull(deleted, "El menú debería haber sido eliminado.");
    }

    @Test
    void testMenuDAO() throws SQLException {
        Random random = new Random();
        int num = random.nextInt(1000) + 1;
        String nombre = "ProductoTest" + num;
        float precio = 5.99f + random.nextFloat();
        String categoria = "Postre";
        String tipo = "Fría";

        Menu menu = new Menu(0, nombre, precio, categoria, tipo);

        Menu testMenu = create(menu);
        update(testMenu);
        search(testMenu);
        delete(testMenu);
    }

    @Test
    void createMenu() throws SQLException {
        Menu menu = new Menu(0, "cafe", 4.50f, "bebida", "Rápida");
        Menu res = menuDAO.create(menu);
        assertNotNull(res, "El menú no debería ser nulo al ser creado.");
    }
}