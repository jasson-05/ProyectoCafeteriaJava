package esfe.Persistencia;

import java.sql.PreparedStatement; // Clase para ejecutar consultas SQL preparadas, previniendo inyecciones SQL.
import java.sql.ResultSet;          // Interfaz para representar el resultado de un conjunto de resultados SQL.
import java.sql.SQLException;       // Clase para manejar errores relacionados con la base de datos SQL.
import java.util.ArrayList;         // Clase para crear listas dinámicas de objetos.

import esfe.dominio.Employee;      // Clase que representa la entidad de empleado en el dominio de la aplicación.

/**
 * Clase DAO (Data Access Object) para la entidad Employee.
 * Se encarga de las operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * de los empleados en la base de datos SQL Server.
 */
public class EmployeeDAO {
    private ConnectionManager conn; // Objeto para gestionar la conexión con la base de datos.
    private PreparedStatement ps;   // Objeto para ejecutar consultas SQL preparadas.
    private ResultSet rs;           // Objeto para almacenar el resultado de una consulta SQL.

    /**
     * Constructor de la clase EmployeeDAO.
     * Inicializa la instancia de ConnectionManager para gestionar la conexión a la base de datos.
     */
    public EmployeeDAO(){
        conn = ConnectionManager.getInstance();
    }

    /**
     * Crea un nuevo empleado en la base de datos.
     *
     * @param employee El objeto Employee que contiene la información del nuevo empleado a crear.
     * Se espera que el objeto Employee tenga los campos 'name', 'position', 'salary',
     * 'phoneNumber' y 'addressEmployees' correctamente establecidos. El campo 'id' será
     * generado automáticamente por la base de datos.
     * @return El objeto Employee recién creado, incluyendo el ID generado por la base de datos,
     * o null si ocurre algún error durante la creación.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos
     * durante la creación del empleado.
     */
    public Employee create(Employee employee) throws SQLException {
        Employee res = null;
        ResultSet generatedKeys = null; // Declarar ResultSet fuera del try para que el finally pueda accederlo

        try{
            // Preparar la sentencia SQL para la inserción de un nuevo empleado.
            // Se especifica que se retornen las claves generadas automáticamente.
            ps = conn.connect().prepareStatement(
                    "INSERT INTO Employees (name, position, salary, phoneNumber, addressemployess) " +
                            "VALUES (?, ?, ?, ?, ?)",
                    java.sql.Statement.RETURN_GENERATED_KEYS
            );
            // Establecer los valores de los parámetros en la sentencia preparada.
            ps.setString(1, employee.getName());
            ps.setString(2, employee.getPosition());
            ps.setDouble(3, employee.getSalary());
            ps.setString(4, employee.getPhoneNumber());
            ps.setString(5, employee.getAddressEmployees());

            // Ejecutar la sentencia de inserción y obtener el número de filas afectadas.
            int affectedRows = ps.executeUpdate();

            // Verificar si la inserción fue exitosa (al menos una fila afectada).
            if (affectedRows != 0) {
                // Obtener las claves generadas automáticamente por la base de datos (en este caso, el ID).
                generatedKeys = ps.getGeneratedKeys();
                // Mover el cursor al primer resultado (si existe).
                if (generatedKeys.next()) {
                    // Obtener el ID generado. Generalmente la primera columna contiene la clave primaria.
                    int idGenerado = generatedKeys.getInt(1);
                    // Recuperar el empleado completo utilizando el ID generado.
                    res = getById(idGenerado); // getById ya gestiona su propia conexión y recursos
                } else {
                    // Lanzar una excepción si la creación del empleado falló y no se obtuvo un ID.
                    throw new SQLException("Creating employee failed, no ID obtained.");
                }
            }
        }catch (SQLException ex){
            // Capturar cualquier excepción SQL que ocurra durante el proceso.
            throw new SQLException("Error al crear el empleado: " + ex.getMessage(), ex);
        } finally {
            // Bloque finally para asegurar que los recursos se liberen de forma segura.
            // Cerrar ResultSet de claves generadas
            try {
                if (generatedKeys != null) {
                    generatedKeys.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar ResultSet en create: " + e.getMessage());
            }

            // Cerrar PreparedStatement
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar PreparedStatement en create: " + e.getMessage());
            }
            conn.disconnect(); // Desconectar de la base de datos.
        }
        return res; // Retornar el empleado creado (con su ID asignado) o null si hubo un error.
    }

    /**
     * Actualiza la información de un empleado existente en la base de datos.
     *
     * @param employee El objeto Employee que contiene la información actualizada del empleado.
     * Se requiere que el objeto Employee tenga los campos 'id', 'name', 'position', 'salary',
     * 'phoneNumber' y 'addressEmployees' correctamente establecidos para realizar la actualización.
     * @return true si la actualización del empleado fue exitosa (al menos una fila afectada),
     * false en caso contrario.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos
     * durante la actualización del empleado.
     */
    public boolean update(Employee employee) throws SQLException{
        boolean res = false;
        try{
            // Preparar la sentencia SQL para actualizar la información de un empleado.
            ps = conn.connect().prepareStatement(
                    "UPDATE Employees " +
                            "SET name = ?, position = ?, salary = ?, phoneNumber = ?, addressemployess = ? " +
                            "WHERE id = ?"
            );

            // Establecer los valores de los parámetros en la sentencia preparada.
            ps.setString(1, employee.getName());
            ps.setString(2, employee.getPosition());
            ps.setDouble(3, employee.getSalary());
            ps.setString(4, employee.getPhoneNumber());
            ps.setString(5, employee.getAddressEmployees());
            ps.setInt(6, employee.getId());

            // Ejecutar la sentencia de actualización y verificar si se afectó alguna fila.
            if(ps.executeUpdate() > 0){
                res = true;
            }
        }catch (SQLException ex){
            // Capturar cualquier excepción SQL que ocurra durante el proceso.
            throw new SQLException("Error al modificar el empleado: " + ex.getMessage(), ex);
        } finally {
            // Bloque finally para asegurar que los recursos se liberen.
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar PreparedStatement en update: " + e.getMessage());
            }
            conn.disconnect(); // Desconectar de la base de datos.
        }
        return res;
    }

    /**
     * Elimina un empleado de la base de datos basándose en su ID.
     *
     * @param employee El objeto Employee que contiene el ID del empleado a eliminar.
     * Se requiere que el objeto Employee tenga el campo 'id' correctamente establecido.
     * @return true si la eliminación del empleado fue exitosa (al menos una fila afectada),
     * false en caso contrario.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos
     * durante la eliminación del empleado.
     */
    public boolean delete(Employee employee) throws SQLException{
        boolean res = false;
        try{
            // Preparar la sentencia SQL para eliminar un empleado por su ID.
            ps = conn.connect().prepareStatement(
                    "DELETE FROM Employees WHERE id = ?"
            );
            // Establecer el valor del parámetro en la sentencia preparada (el ID del empleado a eliminar).
            ps.setInt(1, employee.getId());

            // Ejecutar la sentencia de eliminación y verificar si se afectó alguna fila.
            if(ps.executeUpdate() > 0){
                res = true;
            }
        }catch (SQLException ex){
            // Capturar cualquier excepción SQL que ocurra durante el proceso.
            throw new SQLException("Error al eliminar el empleado: " + ex.getMessage(), ex);
        } finally {
            // Bloque finally para asegurar que los recursos se liberen.
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar PreparedStatement en delete: " + e.getMessage());
            }
            conn.disconnect(); // Desconectar de la base de datos.
        }
        return res;
    }

    /**
     * Busca empleados en la base de datos cuyo nombre contenga la cadena de búsqueda proporcionada.
     * La búsqueda se realiza de forma parcial, es decir, si el nombre del empleado contiene
     * la cadena de búsqueda (ignorando mayúsculas y minúsculas), será incluido en los resultados.
     *
     * @param name La cadena de texto a buscar dentro de los nombres de los empleados.
     * @return Un ArrayList de objetos Employee que coinciden con el criterio de búsqueda.
     * Retorna una lista vacía si no se encuentran empleados con el nombre especificado.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos
     * durante la búsqueda de empleados.
     */
    public ArrayList<Employee> search(String name) throws SQLException{
        ArrayList<Employee> records = new ArrayList<>();

        try {
            // Preparar la sentencia SQL para buscar empleados por nombre (usando LIKE para búsqueda parcial).
            ps = conn.connect().prepareStatement("SELECT id, name, position, salary, phoneNumber, addressemployess " +
                    "FROM Employees " +
                    "WHERE name LIKE ?");

            // Establecer el valor del parámetro en la sentencia preparada.
            // El '%' al inicio y al final permiten la búsqueda de la cadena 'name' en cualquier parte del nombre del empleado.
            ps.setString(1, "%" + name + "%");

            // Ejecutar la consulta SQL y obtener el resultado.
            rs = ps.executeQuery();

            // Iterar a través de cada fila del resultado.
            while (rs.next()){
                // Crear un nuevo objeto Employee para cada registro encontrado.
                Employee employee = new Employee();
                // Asignar los valores de las columnas a los atributos del objeto Employee.
                employee.setId(rs.getInt("id"));
                employee.setName(rs.getString("name"));
                employee.setPosition(rs.getString("position"));
                employee.setSalary(rs.getDouble("salary"));
                employee.setPhoneNumber(rs.getString("phoneNumber"));
                employee.setAddressEmployees(rs.getString("addressemployess"));
                // Agregar el objeto Employee a la lista de resultados.
                records.add(employee);
            }
        } catch (SQLException ex){
            // Capturar cualquier excepción SQL que ocurra durante el proceso.
            throw new SQLException("Error al buscar empleados: " + ex.getMessage(), ex);
        } finally {
            // Bloque finally para asegurar que los recursos se liberen.
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar ResultSet en search: " + e.getMessage());
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar PreparedStatement en search: " + e.getMessage());
            }
            conn.disconnect(); // Desconectar de la base de datos.
        }
        return records;
    }

    /**
     * Obtiene un empleado de la base de datos basado en su ID.
     *
     * @param id El ID del empleado que se desea obtener.
     * @return Un objeto Employee si se encuentra un empleado con el ID especificado,
     * null si no se encuentra ningún empleado con ese ID.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos
     * durante la obtención del empleado.
     */
    public Employee getById(int id) throws SQLException{
        Employee employee = null;

        try {
            // Preparar la sentencia SQL para seleccionar un empleado por su ID.
            ps = conn.connect().prepareStatement("SELECT id, name, position, salary, phoneNumber, addressemployess " +
                    "FROM Employees " +
                    "WHERE id = ?");

            // Establecer el valor del parámetro en la sentencia preparada (el ID a buscar).
            ps.setInt(1, id);

            // Ejecutar la consulta SQL y obtener el resultado.
            rs = ps.executeQuery();

            // Verificar si se encontró algún registro.
            if (rs.next()) {
                // Si se encontró un empleado, crear un nuevo objeto Employee y asignar los valores de las columnas.
                employee = new Employee();
                employee.setId(rs.getInt("id"));
                employee.setName(rs.getString("name"));
                employee.setPosition(rs.getString("position"));
                employee.setSalary(rs.getDouble("salary"));
                employee.setPhoneNumber(rs.getString("phoneNumber"));
                employee.setAddressEmployees(rs.getString("addressemployess"));
            }
        } catch (SQLException ex){
            // Capturar cualquier excepción SQL que ocurra durante el proceso.
            throw new SQLException("Error al obtener un empleado por id: " + ex.getMessage(), ex);
        } finally {
            // Bloque finally para asegurar que los recursos se liberen.
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar ResultSet en getById: " + e.getMessage());
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar PreparedStatement en getById: " + e.getMessage());
            }
            conn.disconnect(); // Desconectar de la base de datos.
        }
        return employee;
    }
}