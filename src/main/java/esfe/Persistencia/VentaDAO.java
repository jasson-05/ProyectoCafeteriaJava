package esfe.Persistencia;

import java.sql.Connection;        // Clase para gestionar la conexión a la base de datos.
import java.sql.PreparedStatement; // Clase para ejecutar consultas SQL preparadas.
import java.sql.ResultSet;        // Interfaz para representar el resultado de una consulta SQL.
import java.sql.SQLException;     // Clase para manejar errores relacionados con la base de datos SQL.
import java.sql.Timestamp;        // Para mapear tipos DATETIME de SQL Server a LocalDateTime de Java.
import java.time.LocalDateTime;   // Clase para manejar fecha y hora sin zona horaria.
import java.util.ArrayList;       // Clase para crear listas dinámicas de objetos.

import esfe.dominio.Venta;        // Clase que representa la entidad Venta en el dominio de la aplicación.

public class VentaDAO {
    private ConnectionManager conn; // Objeto para gestionar la conexión con la base de datos.
    private PreparedStatement ps;   // Objeto para ejecutar consultas SQL preparadas.
    private ResultSet rs;           // Objeto para almacenar el resultado de una consulta SQL.

    // Constructor de la clase VentaDAO
    public VentaDAO() {
        conn = ConnectionManager.getInstance(); // Obtiene la instancia del gestor de conexiones.
    }

    /**
     * Crea un nuevo registro de venta en la base de datos.
     *
     * @param venta El objeto Venta que contiene la información de la nueva venta a crear.
     * Se espera que el objeto Venta tenga los campos 'nomProducto', 'precioProducto',
     * 'nombreCliente', 'estado' y 'fecha' correctamente establecidos. El campo 'idVenta' será
     * generado automáticamente por la base de datos.
     * @return El objeto Venta recién creado, incluyendo el ID generado por la base de datos,
     * o null si ocurre algún error durante la creación.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos
     * durante la creación de la venta.
     */
    public Venta create(Venta venta) throws SQLException {
        Venta res = null; // Variable para almacenar la venta creada que se retornará.
        Connection connection = null; // Declaramos la conexión aquí para el finally
        try {
            connection = conn.connect(); // Obtener la conexión a la base de datos.
            // Preparar la sentencia SQL para la inserción de una nueva venta.
            // Se especifica que se retornen las claves generadas automáticamente (idventa).
            ps = connection.prepareStatement(
                    "INSERT INTO vent (nomProduct, precioProduct, nombreClient, estado, fecha) " +
                            "VALUES (?, ?, ?, ?, ?)",
                    java.sql.Statement.RETURN_GENERATED_KEYS
            );
            // Establecer los valores de los parámetros en la sentencia preparada.
            ps.setInt(1, venta.getNomProducto());     // Asignar el ID/nombre del producto.
            ps.setDouble(2, venta.getPrecioProducto()); // Asignar el precio del producto.
            ps.setString(3, venta.getNombreCliente()); // Asignar el nombre del cliente.
            ps.setString(4, venta.getEstado());        // Asignar el estado de la venta.
            // Convertir LocalDateTime a Timestamp para compatibilidad con DATETIME de SQL Server.
            ps.setTimestamp(5, Timestamp.valueOf(venta.getFecha()));

            // Ejecutar la sentencia de inserción y obtener el número de filas afectadas.
            int affectedRows = ps.executeUpdate();

            // Verificar si la inserción fue exitosa (al menos una fila afectada).
            if (affectedRows != 0) {
                // Obtener las claves generadas automáticamente por la base de datos (en este caso, el ID de venta).
                ResultSet generatedKeys = ps.getGeneratedKeys();
                // Mover el cursor al primer resultado (si existe).
                if (generatedKeys.next()) {
                    // Obtener el ID generado. Generalmente la primera columna contiene la clave primaria.
                    int idGenerado = generatedKeys.getInt(1);
                    // Recuperar la venta completa utilizando el ID generado para asegurar todos los datos.
                    res = getById(idGenerado);
                } else {
                    // Lanzar una excepción si la creación de la venta falló y no se obtuvo un ID.
                    throw new SQLException("Creating sale failed, no ID obtained.");
                }
                generatedKeys.close(); // Cerrar ResultSet de claves generadas
            }
            ps.close(); // Cerrar la sentencia preparada para liberar recursos.
        } catch (SQLException ex) {
            // Capturar cualquier excepción SQL que ocurra durante el proceso.
            throw new SQLException("Error al crear la venta: " + ex.getMessage(), ex);
        } finally {
            // Bloque finally para asegurar que los recursos se liberen.
            if (ps != null) { try { ps.close(); } catch (SQLException e) { /* Ignorar */ } }
            // Solo desconectar si la conexión fue abierta dentro de este método o si ConnectionManager lo maneja globalmente.
            // Dado el ejemplo, ConnectionManager.disconnect() parece global.
            conn.disconnect();
        }
        return res; // Retornar la venta creada (con su ID asignado) o null si hubo un error.
    }

    /**
     * Actualiza la información de una venta existente en la base de datos.
     *
     * @param venta El objeto Venta que contiene la información actualizada de la venta.
     * Se requiere que el objeto Venta tenga el campo 'idVenta' correctamente establecido
     * para realizar la actualización.
     * @return true si la actualización de la venta fue exitosa (al menos una fila afectada),
     * false en caso contrario.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos
     * durante la actualización de la venta.
     */
    public boolean update(Venta venta) throws SQLException {
        boolean res = false; // Variable para indicar si la actualización fue exitosa.
        Connection connection = null;
        try {
            connection = conn.connect();
            // Preparar la sentencia SQL para actualizar la información de una venta.
            // No actualizamos idventa ya que es la clave primaria.
            ps = connection.prepareStatement(
                    "UPDATE vent " +
                            "SET nomProduct = ?, precioProduct = ?, nombreClient = ?, estado = ?, fecha = ? " +
                            "WHERE idventa = ?"
            );

            // Establecer los valores de los parámetros en la sentencia preparada.
            ps.setInt(1, venta.getNomProducto());     // Nuevo ID/nombre del producto.
            ps.setDouble(2, venta.getPrecioProducto()); // Nuevo precio del producto.
            ps.setString(3, venta.getNombreCliente()); // Nuevo nombre del cliente.
            ps.setString(4, venta.getEstado());        // Nuevo estado de la venta.
            ps.setTimestamp(5, Timestamp.valueOf(venta.getFecha())); // Nueva fecha de la venta.
            ps.setInt(6, venta.getIdVenta());         // Condición WHERE para identificar la venta a actualizar por su ID.

            // Ejecutar la sentencia de actualización y verificar si se afectó alguna fila.
            if (ps.executeUpdate() > 0) {
                res = true; // Si executeUpdate() retorna un valor mayor que 0, significa que la actualización fue exitosa.
            }
            ps.close();
        } catch (SQLException ex) {
            // Capturar cualquier excepción SQL que ocurra durante el proceso.
            throw new SQLException("Error al modificar la venta: " + ex.getMessage(), ex);
        } finally {
            if (ps != null) { try { ps.close(); } catch (SQLException e) { /* Ignorar */ } }
            conn.disconnect();
        }
        return res; // Retornar el resultado de la operación de actualización.
    }

    /**
     * Elimina una venta de la base de datos basándose en su ID.
     *
     * @param idVenta El ID de la venta a eliminar.
     * @return true si la eliminación de la venta fue exitosa (al menos una fila afectada),
     * false en caso contrario.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos
     * durante la eliminación de la venta.
     */
    public boolean delete(int idVenta) throws SQLException {
        boolean res = false; // Variable para indicar si la eliminación fue exitosa.
        Connection connection = null;
        try {
            connection = conn.connect();
            // Preparar la sentencia SQL para eliminar una venta por su ID.
            ps = connection.prepareStatement(
                    "DELETE FROM vent WHERE idventa = ?"
            );
            // Establecer el valor del parámetro en la sentencia preparada (el ID de la venta a eliminar).
            ps.setInt(1, idVenta);

            // Ejecutar la sentencia de eliminación y verificar si se afectó alguna fila.
            if (ps.executeUpdate() > 0) {
                res = true; // Si executeUpdate() retorna un valor mayor que 0, significa que la eliminación fue exitosa.
            }
            ps.close();
        } catch (SQLException ex) {
            // Capturar cualquier excepción SQL que ocurra durante el proceso.
            throw new SQLException("Error al eliminar la venta: " + ex.getMessage(), ex);
        } finally {
            if (ps != null) { try { ps.close(); } catch (SQLException e) { /* Ignorar */ } }
            conn.disconnect();
        }
        return res; // Retornar el resultado de la operación de eliminación.
    }

    /**
     * Busca ventas en la base de datos cuyo nombre de cliente contenga la cadena de búsqueda proporcionada.
     * La búsqueda se realiza de forma parcial, es decir, si el nombre del cliente contiene
     * la cadena de búsqueda (ignorando mayúsculas y minúsculas), será incluida en los resultados.
     *
     * @param nombreCliente La cadena de texto a buscar dentro de los nombres de clientes.
     * @return Un ArrayList de objetos Venta que coinciden con el criterio de búsqueda.
     * Retorna una lista vacía si no se encuentran ventas con el nombre de cliente especificado.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos
     * durante la búsqueda de ventas.
     */
    public ArrayList<Venta> search(String nombreCliente) throws SQLException {
        ArrayList<Venta> records = new ArrayList<>(); // Lista para almacenar las ventas encontradas.
        Connection connection = null;
        try {
            connection = conn.connect();
            // Preparar la sentencia SQL para buscar ventas por nombre de cliente (usando LIKE para búsqueda parcial).
            ps = connection.prepareStatement("SELECT idventa, nomProduct, precioProduct, nombreClient, estado, fecha " +
                    "FROM vent " +
                    "WHERE nombreClient LIKE ?");

            // Establecer el valor del parámetro en la sentencia preparada.
            // El '%' al inicio y al final permiten la búsqueda de la cadena 'nombreCliente' en cualquier parte del nombre.
            ps.setString(1, "%" + nombreCliente + "%");

            // Ejecutar la consulta SQL y obtener el resultado.
            rs = ps.executeQuery();

            // Iterar a través de cada fila del resultado.
            while (rs.next()) {
                // Crear un nuevo objeto Venta para cada registro encontrado.
                Venta venta = new Venta();
                // Asignar los valores de las columnas a los atributos del objeto Venta.
                venta.setIdVenta(rs.getInt("idventa"));
                // Aunque nomProduct es int, si en la DB es un ID que apunta a una tabla de productos,
                // aquí estamos asumiendo que es un valor directo para nomProduct.
                venta.setNomProducto(rs.getInt("nomProduct"));
                venta.setPrecioProducto(rs.getDouble("precioProduct"));
                venta.setNombreCliente(rs.getString("nombreClient"));
                // El campo nchar(10) puede tener espacios al final, se recomienda trim()
                venta.setEstado(rs.getString("estado").trim());
                // Mapear Timestamp de SQL Server a LocalDateTime de Java.
                venta.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                // Agregar el objeto Venta a la lista de resultados.
                records.add(venta);
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            // Capturar cualquier excepción SQL que ocurra durante el proceso.
            throw new SQLException("Error al buscar ventas: " + ex.getMessage(), ex);
        } finally {
            if (ps != null) { try { ps.close(); } catch (SQLException e) { /* Ignorar */ } }
            if (rs != null) { try { rs.close(); } catch (SQLException e) { /* Ignorar */ } }
            conn.disconnect();
        }
        return records; // Retornar la lista de ventas encontradas.
    }

    /**
     * Obtiene una venta de la base de datos basado en su ID.
     *
     * @param idVenta El ID de la venta que se desea obtener.
     * @return Un objeto Venta si se encuentra una venta con el ID especificado,
     * null si no se encuentra ninguna venta con ese ID.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos
     * durante la obtención de la venta.
     */
    public Venta getById(int idVenta) throws SQLException {
        Venta venta = null; // Inicializar un objeto Venta que se retornará.
        Connection connection = null;
        try {
            connection = conn.connect();
            // Preparar la sentencia SQL para seleccionar una venta por su ID.
            ps = connection.prepareStatement("SELECT idventa, nomProduct, precioProduct, nombreClient, estado, fecha " +
                    "FROM vent " +
                    "WHERE idventa = ?");

            // Establecer el valor del parámetro en la sentencia preparada (el ID a buscar).
            ps.setInt(1, idVenta);

            // Ejecutar la consulta SQL y obtener el resultado.
            rs = ps.executeQuery();

            // Verificar si se encontró algún registro.
            if (rs.next()) {
                // Si se encontró una venta, crear un nuevo objeto Venta y asignar los valores.
                venta = new Venta();
                venta.setIdVenta(rs.getInt("idventa"));
                venta.setNomProducto(rs.getInt("nomProduct"));
                venta.setPrecioProducto(rs.getDouble("precioProduct"));
                venta.setNombreCliente(rs.getString("nombreClient"));
                venta.setEstado(rs.getString("estado").trim()); // trim() para nchar
                venta.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            // Capturar cualquier excepción SQL que ocurra durante el proceso.
            throw new SQLException("Error al obtener una venta por id: " + ex.getMessage(), ex);
        } finally {
            if (ps != null) { try { ps.close(); } catch (SQLException e) { /* Ignorar */ } }
            if (rs != null) { try { rs.close(); } catch (SQLException e) { /* Ignorar */ } }
            conn.disconnect();
        }
        return venta; // Retornar el objeto Venta encontrado o null si no existe.
    }

    /**
     * Obtiene todas las ventas de la base de datos.
     *
     * @return Un ArrayList de objetos Venta que representan todas las ventas en la base de datos.
     * Retorna una lista vacía si no hay ventas.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos.
     */
    public ArrayList<Venta> getAll() throws SQLException {
        ArrayList<Venta> records = new ArrayList<>();
        Connection connection = null;
        try {
            connection = conn.connect();
            ps = connection.prepareStatement("SELECT idventa, nomProduct, precioProduct, nombreClient, estado, fecha FROM vent");
            rs = ps.executeQuery();

            while (rs.next()) {
                Venta venta = new Venta();
                venta.setIdVenta(rs.getInt("idventa"));
                venta.setNomProducto(rs.getInt("nomProduct"));
                venta.setPrecioProducto(rs.getDouble("precioProduct"));
                venta.setNombreCliente(rs.getString("nombreClient"));
                venta.setEstado(rs.getString("estado").trim());
                venta.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                records.add(venta);
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener todas las ventas: " + ex.getMessage(), ex);
        } finally {
            if (ps != null) { try { ps.close(); } catch (SQLException e) { /* Ignorar */ } }
            if (rs != null) { try { rs.close(); } catch (SQLException e) { /* Ignorar */ } }
            conn.disconnect();
        }
        return records;
    }
}
