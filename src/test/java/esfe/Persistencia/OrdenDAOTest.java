package esfe.Persistencia;

import esfe.dominio.orden;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

@Disabled
class OrdenDAOTest {

    private OrdenDAO orderDAO;

    @BeforeEach
    void setUp() {
        orderDAO = new OrdenDAO();
    }

    @Test
    void testCRUD() throws SQLException {
        // Crear
        orden order = new orden(0, 1, 12.0, LocalDateTime.now());
        orden created = orderDAO.create(order);
        assertNotNull(created);
        assertTrue(created.getId() > 0);

        // Obtener
        orden fetched = orderDAO.getById(created.getId());
        assertNotNull(fetched);
        assertEquals(created.getId(), fetched.getId());

        // Actualizar
        fetched.setTotalAmount(79.0);
        boolean updated = orderDAO.update(fetched);
        assertTrue(updated);
        assertEquals(79.0, orderDAO.getById(fetched.getId()).getTotalAmount());

        // Listar todos
        List<orden> all = orderDAO.getAll();
        assertFalse(all.isEmpty());

        // Eliminar
        boolean deleted = orderDAO.delete(fetched.getId());
        assertTrue(deleted);
        assertNull(orderDAO.getById(fetched.getId()));
    }
}
