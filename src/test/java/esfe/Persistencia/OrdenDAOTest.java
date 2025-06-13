package esfe.Persistencia;

import esfe.dominio.orden;
import esfe.Persistencia.OrdenDAO;

import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrdenDAOTest {

    private static OrdenDAO ordenDAO;
    private static orden testOrden;

    @BeforeAll
    public static void setUp() {
        ordenDAO = new OrdenDAO();
        testOrden = new orden(0, 1, 99.99, LocalDateTime.now()); // Asegúrate de que UserId 1 existe en tu base de datos
    }

    @Test
    @Order(1)
    public void testCreateOrden() throws SQLException {
        orden creada = ordenDAO.create(testOrden);
        assertNotNull(creada);
        assertTrue(creada.getId() > 0);
        testOrden.setId(creada.getId()); // Guardar ID para las siguientes pruebas
    }

    @Test
    @Order(2)
    public void testGetById() throws SQLException {
        orden obtenida = ordenDAO.getById(testOrden.getId());
        assertNotNull(obtenida);
        assertEquals(testOrden.getUserId(), obtenida.getUserId());
        assertEquals(testOrden.getTotalAmount(), obtenida.getTotalAmount());
    }

    @Test
    @Order(3)
    public void testUpdateOrden() throws SQLException {
        testOrden.setTotalAmount(149.99);
        boolean actualizado = ordenDAO.update(testOrden);
        assertTrue(actualizado);

        orden actualizada = ordenDAO.getById(testOrden.getId());
        assertEquals(149.99, actualizada.getTotalAmount());
    }

    @Test
    @Order(4)
    public void testGetAll() throws SQLException {
        List<orden> todas = ordenDAO.getAll();
        assertNotNull(todas);
        assertFalse(todas.isEmpty());
    }

    @Test
    @Order(5)
    public void testDeleteOrden() throws SQLException {
        boolean eliminado = ordenDAO.delete(testOrden.getId());
        assertTrue(eliminado);

        orden eliminada = ordenDAO.getById(testOrden.getId());
        assertNull(eliminada);
    }
}
