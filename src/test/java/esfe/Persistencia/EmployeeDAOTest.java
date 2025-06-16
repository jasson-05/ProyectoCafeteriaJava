package esfe.Persistencia;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import esfe.dominio.Employee;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas unitarias adicionales para la clase EmployeeDAO,
 * enfocándose en casos límite o específicos de las operaciones CRUD.
 *
 * NOTA IMPORTANTE: Dado que EmployeeDAO usa una lista 'static' en memoria,
 * las pruebas no son completamente aisladas. Cada ejecución de prueba
 * modificará los datos estáticos, lo que podría afectar a pruebas posteriores
 * dentro de la misma ejecución de la suite de tests. Para un aislamiento total
 * en pruebas unitarias, la lista en EmployeeDAO debería ser no estática
 * o debería haber un método para resetearla antes de cada prueba.
 */
class EmployeeDAOEdgeCasesTest {
    private EmployeeDAO employeeDAO; // Instancia de la clase EmployeeDAO que se va a probar.
    private static boolean setupDone = false; // Flag para controlar la inicialización de datos estáticos
    private static final Random RANDOM = new Random(); // Instancia de Random para reutilizar

    /**
     * Método que se ejecuta antes de cada método de prueba (@Test).
     * Su propósito es inicializar el entorno de prueba. Debido a que EmployeeDAO
     * usa datos estáticos, el reseteo completo de datos para cada prueba
     * requeriría una modificación en EmployeeDAO. Aquí, solo inicializamos
     * la instancia del DAO.
     */
    @BeforeEach
    void setUp() {
        employeeDAO = new EmployeeDAO();
        // Para que las pruebas sean verdaderamente aisladas con un DAO de datos estáticos,
        // necesitarías un método en EmployeeDAO como 'clearAllEmployeesForTesting()'
        // que limpiaría el ArrayList estático. Sin ese método, las pruebas pueden
        // interferir entre sí. Por ejemplo:
        // if (!setupDone) {
        //     EmployeeDAO.clearAllEmployeesForTesting(); // Asumiendo que existe este método
        //     setupDone = true;
        // }
    }

    /**
     * Prueba para verificar la eliminación de un empleado que no existe.
     * Se espera que el método delete retorne false.
     * @throws SQLException Si ocurre un error de SQL.
     */
    @Test
    void testDeleteNonExistentEmployee() throws SQLException {
        // Crear un empleado con un ID que sabemos que no existe en el ArrayList estático
        // (asumiendo que los IDs comienzan desde 1 y no serán tan grandes)
        Employee nonExistentEmployee = new Employee();
        nonExistentEmployee.setId(-999); // Un ID negativo o muy grande es seguro que no existe

        boolean result = employeeDAO.delete(nonExistentEmployee);

        assertFalse(result, "La eliminación de un empleado inexistente debería retornar false.");
    }

    /**
     * Prueba para verificar la búsqueda con un filtro vacío o nulo.
     * Se espera que retorne todos los empleados actualmente en la "base de datos".
     * @throws SQLException Si ocurre un error de SQL.
     */
    @Test
    void testSearchWithEmptyOrNullFilter() throws SQLException {
        // Primero, asegurémonos de que haya algunos empleados para probar
        // (El constructor de EmployeeDAO ya añade algunos datos de prueba)
        Employee newEmp1 = employeeDAO.create(new Employee("Buscar A", "Pos1", 100.0, "111", "a@a.com"));
        Employee newEmp2 = employeeDAO.create(new Employee("Buscar B", "Pos2", 200.0, "222", "b@b.com"));
        Employee newEmp3 = employeeDAO.create(new Employee("Otro Nombre", "Pos3", 300.0, "333", "c@c.com"));


        ArrayList<Employee> allEmployees = employeeDAO.search(""); // Búsqueda con filtro vacío
        assertNotNull(allEmployees, "La lista de empleados no debería ser nula.");
        assertTrue(allEmployees.size() >= 3 + 3, "La búsqueda con filtro vacío debería retornar al menos los empleados de ejemplo + los 3 nuevos."); // 3 de inicio + 3 creados aquí.

        ArrayList<Employee> allEmployeesNull = employeeDAO.search(null); // Búsqueda con filtro nulo
        assertNotNull(allEmployeesNull, "La lista de empleados no debería ser nula con filtro nulo.");
        assertEquals(allEmployees.size(), allEmployeesNull.size(), "La búsqueda con filtro nulo debería retornar la misma cantidad que con filtro vacío.");
    }

    /**
     * Prueba para verificar la búsqueda por una cadena que no coincide con ningún empleado.
     * Se espera que la lista de resultados esté vacía.
     * @throws SQLException Si ocurre un error de SQL.
     */
    @Test
    void testSearchNoMatch() throws SQLException {
        ArrayList<Employee> results = employeeDAO.search("XYZStringThatDoesNotExist123");
        assertNotNull(results, "La lista de resultados no debería ser nula.");
        assertTrue(results.isEmpty(), "La búsqueda sin coincidencias debería retornar una lista vacía.");
    }

    /**
     * Prueba para crear un empleado y luego buscarlo por su nombre completo,
     * verificando que se encuentre específicamente ese empleado.
     * @throws SQLException Si ocurre un error de SQL.
     */
    @Test
    void testCreateAndSearchExactMatch() throws SQLException {
        String uniqueName = "Empleado Exacto " + RANDOM.nextInt(100000);
        Employee empToCreate = new Employee(
                uniqueName,
                "Especialista",
                950.00,
                "5037" + (10000000 + RANDOM.nextInt(90000000)),
                "email.exacto@ejemplo.com"
        );
        Employee createdEmp = employeeDAO.create(empToCreate);
        assertNotNull(createdEmp, "El empleado creado no debe ser nulo.");

        ArrayList<Employee> searchResults = employeeDAO.search(uniqueName);
        assertNotNull(searchResults, "La lista de resultados no debe ser nula.");
        assertFalse(searchResults.isEmpty(), "La búsqueda por nombre exacto debería encontrar el empleado.");
        assertEquals(1, searchResults.stream().filter(e -> e.getId() == createdEmp.getId()).count(), "Debería encontrar exactamente el empleado creado.");
        assertEquals(createdEmp.getName(), searchResults.get(0).getName(), "El nombre del empleado encontrado debe coincidir.");
    }
}
