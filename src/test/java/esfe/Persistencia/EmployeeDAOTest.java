package esfe.Persistencia;

import org.junit.jupiter.api.BeforeEach; // Anotación para indicar que el método se ejecuta antes de cada método de prueba.
import org.junit.jupiter.api.Test;      // Anotación para indicar que el método es un caso de prueba.
import esfe.dominio.Employee;           // Clase que representa la entidad de empleado utilizada en las pruebas.

import java.sql.SQLException;           // Clase para manejar excepciones relacionadas con la base de datos SQL.
import java.util.ArrayList;             // Clase para crear listas dinámicas de objetos, utilizada en algunas pruebas.
import java.util.Random;                // Clase para generar números aleatorios, útil para crear datos de prueba.

import static org.junit.jupiter.api.Assertions.*; // Importación estática de métodos de aserción de JUnit 5.

/**
 * Clase de pruebas unitarias para la clase EmployeeDAO.
 * Contiene métodos para verificar el correcto funcionamiento de las operaciones CRUD
 * (Crear, Leer, Actualizar, Eliminar) en la base de datos de empleados.
 */
class EmployeeDAOTest {
    private EmployeeDAO employeeDAO; // Instancia de la clase EmployeeDAO que se va a probar.

    /**
     * Método que se ejecuta antes de cada método de prueba (@Test).
     * Su propósito es inicializar el entorno de prueba, creando una nueva instancia
     * de EmployeeDAO para cada prueba, asegurando que las pruebas sean independientes.
     */
    @BeforeEach
    void setUp(){
        employeeDAO = new EmployeeDAO();
    }

    /**
     * Helper para crear un nuevo empleado y verificar su correcta inserción.
     * @param employee El objeto Employee a crear.
     * @return El objeto Employee creado, con el ID asignado por la base de datos.
     * @throws SQLException Si ocurre un error de SQL.
     */
    private Employee create(Employee employee) throws SQLException{
        // Llama al método 'create' del EmployeeDAO para persistir el empleado en la base de datos.
        Employee res = employeeDAO.create(employee);

        // Realiza aserciones para verificar que la creación del empleado fue exitosa
        // y que los datos del empleado retornado coinciden con los datos originales.
        assertNotNull(res, "El empleado creado no debería ser nulo.");
        assertTrue(res.getId() > 0, "El ID del empleado creado debería ser mayor que 0.");
        assertEquals(employee.getName(), res.getName(), "El nombre del empleado creado debe ser igual al original.");
        assertEquals(employee.getPosition(), res.getPosition(), "La posición del empleado creado debe ser igual a la original.");
        assertEquals(employee.getSalary(), res.getSalary(), 0.001, "El salario del empleado creado debe ser igual al original."); // Delta para comparar doubles
        assertEquals(employee.getPhoneNumber(), res.getPhoneNumber(), "El teléfono del empleado creado debe ser igual al original.");
        assertEquals(employee.getAddressEmployees(), res.getAddressEmployees(), "La dirección del empleado creado debe ser igual a la original.");

        // Retorna el objeto Employee creado.
        return res;
    }

    /**
     * Helper para actualizar un empleado y verificar su correcta modificación.
     * @param employee El objeto Employee a actualizar (debe tener un ID válido).
     * @throws SQLException Si ocurre un error de SQL.
     */
    private void update(Employee employee) throws SQLException{
        // Modifica los atributos del objeto Employee para simular una actualización.
        employee.setName(employee.getName() + " Updated");
        employee.setPosition("Senior " + employee.getPosition());
        employee.setSalary(employee.getSalary() * 1.10); // Aumentar salario 10%
        employee.setPhoneNumber("+" + (new Random().nextInt(900000000) + 100000000)); // Nuevo número aleatorio
        employee.setAddressEmployees("New Address " + (new Random().nextInt(100) + 1));

        // Llama al método 'update' del EmployeeDAO para actualizar el empleado.
        boolean res = employeeDAO.update(employee);

        // Realiza una aserción para verificar que la actualización fue exitosa.
        assertTrue(res, "La actualización del empleado debería ser exitosa.");

        // Llama al método 'getById' para verificar que los cambios se persistieron correctamente.
        getById(employee);
    }

    /**
     * Helper para obtener un empleado por ID y verificar que los datos coincidan.
     * @param employee El objeto Employee de referencia (con el ID y datos esperados).
     * @throws SQLException Si ocurre un error de SQL.
     */
    private void getById(Employee employee) throws SQLException {
        // Llama al método 'getById' del EmployeeDAO para obtener un empleado por su ID.
        Employee res = employeeDAO.getById(employee.getId());

        // Realiza aserciones para verificar que el empleado obtenido coincide
        // con el empleado original (o el empleado modificado en pruebas de actualización).
        assertNotNull(res, "El empleado obtenido por ID no debería ser nulo.");
        assertEquals(employee.getId(), res.getId(), "El ID del empleado obtenido debe ser igual al original.");
        assertEquals(employee.getName(), res.getName(), "El nombre del empleado obtenido debe ser igual al esperado.");
        assertEquals(employee.getPosition(), res.getPosition(), "La posición del empleado obtenido debe ser igual a la esperada.");
        assertEquals(employee.getSalary(), res.getSalary(), 0.001, "El salario del empleado obtenido debe ser igual al esperado.");
        assertEquals(employee.getPhoneNumber(), res.getPhoneNumber(), "El teléfono del empleado obtenido debe ser igual al esperado.");
        assertEquals(employee.getAddressEmployees(), res.getAddressEmployees(), "La dirección del empleado obtenido debe ser igual a la esperada.");
    }

    /**
     * Helper para buscar empleados por nombre y verificar los resultados.
     * @param employee El objeto Employee de referencia para la búsqueda de nombre.
     * @throws SQLException Si ocurre un error de SQL.
     */
    private void search(Employee employee) throws SQLException {
        // Llama al método 'search' del EmployeeDAO para buscar empleados por nombre.
        ArrayList<Employee> employees = employeeDAO.search(employee.getName().substring(0, employee.getName().length() / 2)); // Busca una parte del nombre
        boolean find = false; // Variable para rastrear si se encontró un empleado con el nombre buscado.

        // Itera sobre la lista de empleados devuelta por la búsqueda.
        for (Employee employeeItem : employees) {
            // Verifica si el nombre de cada empleado encontrado contiene la cadena de búsqueda.
            if (employeeItem.getName().contains(employee.getName().substring(0, employee.getName().length() / 2))) {
                find = true; // Si se encuentra una coincidencia, se establece 'find' a true.
                break; // Se encontró al menos uno, suficiente para esta prueba
            }
        }
        // Realiza una aserción para verificar que se encontró al menos un empleado con el nombre buscado.
        assertTrue(find, "No se encontraron empleados cuyo nombre contenga la cadena de búsqueda: " + employee.getName().substring(0, employee.getName().length() / 2));
    }

    /**
     * Helper para eliminar un empleado y verificar su correcta eliminación.
     * @param employee El objeto Employee a eliminar (debe tener un ID válido).
     * @throws SQLException Si ocurre un error de SQL.
     */
    private void delete(Employee employee) throws SQLException{
        // Llama al método 'delete' del EmployeeDAO para eliminar un empleado por su ID.
        boolean res = employeeDAO.delete(employee);

        // Realiza una aserción para verificar que la eliminación fue exitosa.
        assertTrue(res, "La eliminación del empleado debería ser exitosa.");

        // Intenta obtener el empleado por su ID después de la eliminación.
        Employee res2 = employeeDAO.getById(employee.getId());

        // Realiza una aserción para verificar que el empleado ya no existe en la base de datos.
        assertNull(res2, "El empleado debería haber sido eliminado y no encontrado por ID.");
    }

    /**
     * Prueba completa del ciclo de vida de un empleado: Crear, Actualizar, Buscar, Eliminar.
     * @throws SQLException Si ocurre un error de SQL durante la ejecución de las pruebas.
     */
    @Test
    void testEmployeeDAO() throws SQLException {
        Random random = new Random();
        int uniqueId = random.nextInt(100000); // Para asegurar nombres y teléfonos únicos en las pruebas

        // 1. Crear un nuevo empleado de prueba
        Employee newEmployee = new Employee(
                0, // El ID se generará automáticamente
                "Empleado Prueba " + uniqueId,
                "Desarrollador Junior",
                30000.00,
                "5037" + (10000000 + random.nextInt(90000000)), // Teléfono de 8 dígitos simulado
                "Calle Falsa 123, Ciudad " + uniqueId
        );
        Employee createdEmployee = create(newEmployee);

        // 2. Actualizar el empleado creado
        update(createdEmployee);

        // 3. Buscar el empleado actualizado por una parte de su nombre
        search(createdEmployee);

        // 4. Eliminar el empleado
        delete(createdEmployee);
    }

    /**
     * Prueba específica para la creación de un empleado.
     * @throws SQLException Si ocurre un error de SQL.
     */
    @Test
    void createEmployeeTest() throws SQLException {
        Random random = new Random();
        int uniqueId = random.nextInt(100000);

        Employee employee = new Employee(
                0,
                "Nuevo Empleado " + uniqueId,
                "Diseñador",
                35000.00,
                "5037" + (10000000 + random.nextInt(90000000)),
                "Avenida Siempre Viva 456"
        );
        Employee createdEmployee = employeeDAO.create(employee);
        assertNotNull(createdEmployee);
        assertTrue(createdEmployee.getId() > 0);
        assertEquals(employee.getName(), createdEmployee.getName());
    }
}
