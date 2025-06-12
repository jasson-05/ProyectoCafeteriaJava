package esfe.Persistencia;

import org.junit.jupiter.api.BeforeEach; // Anotación para indicar que el método se ejecuta antes de cada método de prueba.
import org.junit.jupiter.api.Test;       // Anotación para indicar que el método es un caso de prueba.
import esfe.dominio.Venta;                // Clase que representa la entidad de venta utilizada en las pruebas.

import java.sql.SQLException;             // Clase para manejar excepciones relacionadas con la base de datos SQL.
import java.time.LocalDateTime;           // Para manejar el tipo de dato fecha/hora.
import java.util.ArrayList;              // Clase para crear listas dinámicas de objetos.
import java.util.Random;                 // Clase para generar números aleatorios.

import static org.junit.jupiter.api.Assertions.*; // Importación estática de métodos de aserción de JUnit 5.


class VentaDAOTest {
    private VentaDAO ventaDAO; // Instancia de la clase VentaDAO que se va a probar.

    @BeforeEach
    void setUp() {
        // Método que se ejecuta antes de cada método de prueba (@Test).
        // Su propósito es inicializar el entorno de prueba, creando una nueva instancia de VentaDAO.
        ventaDAO = new VentaDAO();
    }

    /**
     * Helper method to create a Venta record in the database and assert its creation.
     * Método auxiliar para crear un registro de Venta en la base de datos y verificar su creación.
     *
     * @param venta The Venta object to create. El objeto Venta a crear.
     * @return The created Venta object, including the generated ID. El objeto Venta creado, incluyendo el ID generado.
     * @throws SQLException If a database access error occurs. Si ocurre un error de acceso a la base de datos.
     */
    private Venta create(Venta venta) throws SQLException {
        Venta res = ventaDAO.create(venta);

        // Assertions to verify successful creation.
        // Aserciones para verificar la creación exitosa.
        assertNotNull(res, "La venta creada no debería ser nula.");
        assertNotEquals(0, res.getIdVenta(), "El ID de la venta creada debería ser generado por la base de datos.");
        assertEquals(venta.getNomProducto(), res.getNomProducto(), "El ID de producto debe coincidir.");
        assertEquals(venta.getPrecioProducto(), res.getPrecioProducto(), 0.001, "El precio de producto debe coincidir."); // 0.001 es delta para float/double
        assertEquals(venta.getNombreCliente(), res.getNombreCliente(), "El nombre del cliente debe coincidir.");
        assertEquals(venta.getEstado().trim(), res.getEstado().trim(), "El estado de la venta debe coincidir."); // trim() para nchar
        assertEquals(venta.getFecha().toLocalDate(), res.getFecha().toLocalDate(), "La fecha de la venta debe coincidir (solo fecha).");
        assertEquals(venta.getFecha().getHour(), res.getFecha().getHour(), "La hora de la venta debe coincidir.");
        assertEquals(venta.getFecha().getMinute(), res.getFecha().getMinute(), "El minuto de la venta debe coincidir.");

        return res;
    }

    /**
     * Helper method to update a Venta record in the database and assert the update.
     * Método auxiliar para actualizar un registro de Venta en la base de datos y verificar la actualización.
     *
     * @param venta The Venta object with updated information. El objeto Venta con la información actualizada.
     * @throws SQLException If a database access error occurs. Si ocurre un error de acceso a la base de datos.
     */
    private void update(Venta venta) throws SQLException {
        // Modify attributes of the Venta object for the update simulation.
        // Modificar atributos del objeto Venta para la simulación de actualización.
        venta.setNombreCliente(venta.getNombreCliente() + " Actualizado");
        venta.setPrecioProducto(venta.getPrecioProducto() + 5.50);
        venta.setEstado("ENVIADO");
        venta.setNomProducto(venta.getNomProducto() + 10); // Change product ID/name

        boolean res = ventaDAO.update(venta);
        assertTrue(res, "La actualización de la venta debería ser exitosa.");

        // Verify that the changes were persisted correctly by fetching the updated record.
        // Verificar que los cambios se persistieron correctamente al obtener el registro actualizado.
        getById(venta.getIdVenta(), venta);
    }

    /**
     * Helper method to retrieve a Venta record by ID and assert its correctness.
     * Método auxiliar para obtener un registro de Venta por ID y verificar su corrección.
     *
     * @param idVenta The ID of the Venta to retrieve. El ID de la Venta a obtener.
     * @param expectedVenta The Venta object with expected values for comparison. El objeto Venta con valores esperados para comparación.
     * @throws SQLException If a database access error occurs. Si ocurre un error de acceso a la base de datos.
     */
    private void getById(int idVenta, Venta expectedVenta) throws SQLException {
        Venta res = ventaDAO.getById(idVenta);

        // Assertions to verify the retrieved Venta matches the expected one.
        // Aserciones para verificar que la Venta obtenida coincide con la esperada.
        assertNotNull(res, "La venta obtenida por ID no debería ser nula.");
        assertEquals(expectedVenta.getIdVenta(), res.getIdVenta(), "El ID de la venta obtenida debe ser igual al esperado.");
        assertEquals(expectedVenta.getNomProducto(), res.getNomProducto(), "El ID de producto debe ser igual al esperado.");
        assertEquals(expectedVenta.getPrecioProducto(), res.getPrecioProducto(), 0.001, "El precio de producto debe ser igual al esperado.");
        assertEquals(expectedVenta.getNombreCliente(), res.getNombreCliente(), "El nombre del cliente debe ser igual al esperado.");
        assertEquals(expectedVenta.getEstado().trim(), res.getEstado().trim(), "El estado de la venta debe ser igual al esperado.");
        assertEquals(expectedVenta.getFecha().toLocalDate(), res.getFecha().toLocalDate(), "La fecha de la venta debe ser igual a la esperada (solo fecha).");
        assertEquals(expectedVenta.getFecha().getHour(), res.getFecha().getHour(), "La hora de la venta debe ser igual a la esperada.");
        assertEquals(expectedVenta.getFecha().getMinute(), res.getFecha().getMinute(), "El minuto de la venta debe ser igual al esperado.");
    }

    /**
     * Helper method to search for sales by client name and assert the results.
     * Método auxiliar para buscar ventas por nombre de cliente y verificar los resultados.
     *
     * @param searchTerm The client name to search for. El nombre del cliente a buscar.
     * @param expectedVentaId The ID of the expected Venta to be found in the results. El ID de la Venta esperada que debe encontrarse en los resultados.
     * @throws SQLException If a database access error occurs. Si ocurre un error de acceso a la base de datos.
     */
    private void search(String searchTerm, int expectedVentaId) throws SQLException {
        ArrayList<Venta> ventas = ventaDAO.search(searchTerm);
        assertNotNull(ventas, "La lista de resultados de búsqueda no debería ser nula.");
        assertFalse(ventas.isEmpty(), "Debería encontrarse al menos una venta para el término de búsqueda '" + searchTerm + "'.");

        boolean foundExpected = false;
        for (Venta v : ventas) {
            assertTrue(v.getNombreCliente().toLowerCase().contains(searchTerm.toLowerCase()), "El nombre del cliente en los resultados debe contener el término de búsqueda.");
            if (v.getIdVenta() == expectedVentaId) {
                foundExpected = true;
            }
        }
        assertTrue(foundExpected, "La venta con ID " + expectedVentaId + " no fue encontrada en los resultados de búsqueda para '" + searchTerm + "'.");
    }

    /**
     * Helper method to retrieve all sales and assert the list is not null and not empty.
     * Método auxiliar para obtener todas las ventas y verificar que la lista no sea nula y no esté vacía.
     *
     * @throws SQLException If a database access error occurs. Si ocurre un error de acceso a la base de datos.
     */
    private void getAll() throws SQLException {
        ArrayList<Venta> allVentas = ventaDAO.getAll();
        assertNotNull(allVentas, "La lista de todas las ventas no debería ser nula.");
        assertFalse(allVentas.isEmpty(), "La lista de todas las ventas no debería estar vacía después de las operaciones de prueba.");
        System.out.println("Total de ventas en la DB: " + allVentas.size());
    }

    /**
     * Helper method to delete a Venta record by ID and assert its deletion.
     * Método auxiliar para eliminar un registro de Venta por ID y verificar su eliminación.
     *
     * @param idVenta The ID of the Venta to delete. El ID de la Venta a eliminar.
     * @throws SQLException If a database access error occurs. Si ocurre un error de acceso a la base de datos.
     */
    private void delete(int idVenta) throws SQLException {
        boolean res = ventaDAO.delete(idVenta);
        assertTrue(res, "La eliminación de la venta con ID " + idVenta + " debería ser exitosa.");

        // Verify that the Venta no longer exists in the database.
        // Verificar que la Venta ya no existe en la base de datos.
        Venta res2 = ventaDAO.getById(idVenta);
        assertNull(res2, "La venta con ID " + idVenta + " debería haber sido eliminada y no encontrada por ID.");
    }

    /**
     * Main test method that orchestrates a full CRUD (Create, Read, Update, Delete) cycle for Venta.
     * Método de prueba principal que orquesta un ciclo completo CRUD (Crear, Leer, Actualizar, Eliminar) para Venta.
     *
     * @throws SQLException If any database operation fails. Si alguna operación de base de datos falla.
     */
    @Test
    void testVentaDAO() throws SQLException {
        Random random = new Random();
        int randomSuffix = random.nextInt(100000); // Para asegurar unicidad en datos de prueba

        // 1. Test Create
        // Crear una nueva venta con datos de prueba
        Venta newVenta = new Venta(
                101 + randomSuffix, // nomProduct
                50.75,              // precioProduct
                "Cliente Test " + randomSuffix, // nombreClient
                "PENDIENTE",        // estado
                LocalDateTime.now().minusHours(random.nextInt(24)) // fecha
        );
        Venta createdVenta = create(newVenta); // Call helper method and get the Venta with generated ID

        // 2. Test GetById
        // Obtener la venta recién creada por su ID y verificar que coincide
        getById(createdVenta.getIdVenta(), createdVenta);

        // 3. Test Update
        // Actualizar la venta y verificar los cambios
        update(createdVenta); // 'createdVenta' object now holds updated values

        // 4. Test Search
        // Buscar ventas por parte del nombre del cliente y verificar que la venta actualizada está en los resultados
        search("Cliente Test " + randomSuffix, createdVenta.getIdVenta()); // Search by original name part

        // 5. Test GetAll
        // Obtener todas las ventas y verificar que la lista no esté vacía
        getAll();

        // 6. Test Delete
        // Eliminar la venta creada y verificar que ya no existe
        delete(createdVenta.getIdVenta());

        // Opcional: Verificar que el search no devuelve la venta eliminada
        ArrayList<Venta> remainingVentas = ventaDAO.search("Cliente Test " + randomSuffix);
        boolean foundDeleted = false;
        for (Venta v : remainingVentas) {
            if (v.getIdVenta() == createdVenta.getIdVenta()) {
                foundDeleted = true;
                break;
            }
        }
        assertFalse(foundDeleted, "La venta eliminada no debería aparecer en los resultados de búsqueda.");

        // Opcional: Verificar que el getById retorna null para la venta eliminada
        assertNull(ventaDAO.getById(createdVenta.getIdVenta()), "getById debería retornar null para una venta eliminada.");
    }

    /**
     * Another specific test case for creating a sale.
     * Otro caso de prueba específico para crear una venta.
     *
     * @throws SQLException If a database access error occurs. Si ocurre un error de acceso a la base de datos.
     */
    @Test
    void createSpecificVenta() throws SQLException {
        Venta venta = new Venta(
                5001,
                99.99,
                "Cliente Específico",
                "PENDIENTE",
                LocalDateTime.of(2025, 7, 15, 10, 30)
        );
        Venta res = ventaDAO.create(venta);
        assertNotEquals(res, null); // Asegura que el objeto retornado no sea nulo
        assertEquals(5001, res.getNomProducto());
        assertEquals("Cliente Específico", res.getNombreCliente());
    }
}
