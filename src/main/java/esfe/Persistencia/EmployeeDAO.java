package esfe.Persistencia;

import esfe.dominio.Employee;

import java.sql.SQLException; // Aunque no se usa una DB real, se mantiene por si se expande a JDBC
import java.util.ArrayList;
import java.util.Iterator; // Para eliminar de forma segura

/**
 * Clase de Acceso a Datos (DAO) para la entidad Employee.
 * Simula la persistencia de datos en memoria usando un ArrayList.
 * En una aplicación real, esto interactuaría con una base de datos (SQL, NoSQL, etc.).
 */
public class EmployeeDAO {
    // Almacén de datos en memoria. En una aplicación real, esto sería una conexión a la DB.
    private static ArrayList<Employee> employees = new ArrayList<>();
    private static int nextId = 1; // Para simular IDs autoincrementales

    /**
     * Constructor. Inicializa algunos datos de ejemplo si la lista está vacía.
     */
    public EmployeeDAO() {
        if (employees.isEmpty()) {
            // Datos de prueba para que la tabla no aparezca vacía al inicio
            employees.add(new Employee(nextId++, "Juan Pérez", "Cajero", 800.00, "7890-1234", "juan.perez@example.com"));
            employees.add(new Employee(nextId++, "Ana García", "Barista", 750.00, "6543-2109", "ana.garcia@example.com"));
            employees.add(new Employee(nextId++, "Pedro López", "Gerente", 1200.00, "7123-4567", "pedro.lopez@example.com"));
        }
    }

    /**
     * Crea un nuevo empleado en el sistema.
     * @param employee El objeto Employee a crear.
     * @return El objeto Employee creado con su ID asignado, o null si falla.
     * @throws SQLException Si ocurre un error de SQL (simulado para compatibilidad).
     */
    public Employee create(Employee employee) throws SQLException {
        if (employee == null) {
            throw new IllegalArgumentException("El objeto Employee no puede ser nulo.");
        }
        employee.setId(nextId++); // Asigna un ID único simulado
        employees.add(employee);
        System.out.println("Empleado creado: " + employee); // Para depuración
        return employee;
    }

    /**
     * Busca empleados que coincidan con un filtro de nombre.
     * @param filter El nombre o parte del nombre a buscar. Una cadena vacía o nula retorna todos.
     * @return Una lista de objetos Employee que coinciden con el filtro.
     * @throws SQLException Si ocurre un error de SQL (simulado para compatibilidad).
     */
    public ArrayList<Employee> search(String filter) throws SQLException {
        ArrayList<Employee> results = new ArrayList<>();
        if (filter == null || filter.trim().isEmpty()) {
            return new ArrayList<>(employees); // Retorna una copia de todos los empleados
        }

        String lowerCaseFilter = filter.toLowerCase();
        for (Employee emp : employees) {
            if (emp.getName().toLowerCase().contains(lowerCaseFilter) ||
                    emp.getPosition().toLowerCase().contains(lowerCaseFilter)) { // También permite buscar por posición
                results.add(emp);
            }
        }
        return results;
    }

    /**
     * Actualiza la información de un empleado existente.
     * @param employee El objeto Employee con la información actualizada y un ID válido.
     * @return true si la actualización fue exitosa, false en caso contrario.
     * @throws SQLException Si ocurre un error de SQL (simulado para compatibilidad).
     */
    public boolean update(Employee employee) throws SQLException {
        if (employee == null || employee.getId() == 0) {
            throw new IllegalArgumentException("El objeto Employee y su ID no pueden ser nulos o cero para la actualización.");
        }
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId() == employee.getId()) {
                employees.set(i, employee); // Reemplaza el objeto existente
                System.out.println("Empleado actualizado: " + employee); // Para depuración
                return true;
            }
        }
        return false; // Empleado no encontrado
    }

    /**
     * Elimina un empleado del sistema.
     * @param employee El objeto Employee con el ID del empleado a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     * @throws SQLException Si ocurre un error de SQL (simulado para compatibilidad).
     */
    public boolean delete(Employee employee) throws SQLException {
        if (employee == null || employee.getId() == 0) {
            throw new IllegalArgumentException("El objeto Employee y su ID no pueden ser nulos o cero para la eliminación.");
        }
        // Usar Iterator para evitar ConcurrentModificationException al eliminar durante la iteración
        Iterator<Employee> iterator = employees.iterator();
        while (iterator.hasNext()) {
            Employee emp = iterator.next();
            if (emp.getId() == employee.getId()) {
                iterator.remove();
                System.out.println("Empleado eliminado con ID: " + employee.getId()); // Para depuración
                return true;
            }
        }
        return false; // Empleado no encontrado
    }
}
