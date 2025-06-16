package esfe.Persistencia;

import esfe.dominio.Producto;
import java.sql.Connection;
// import java.sql.DriverManager; // Ya no necesitas esta importación si usas ConnectionManager
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO (Data Access Object) para interactuar con la tabla 'menuss'.
 * Se encarga de la lógica para obtener datos de productos de la base de datos.
 */
public class ProductoDAO {
    // Ya no necesitas definir STR_CONNECTION, DB_USER, DB_PASSWORD aquí,
    // ya que ConnectionManager se encargará de gestionar la conexión.
    // private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=Cafeteria_BD;encrypt=true;trustServerCertificate=true";
    // private static final String DB_USER = "DESKTOP-UKKJ1T9\\SQLEXPRESS01";
    // private static final String DB_PASSWORD = "12345";

    /**
     * Obtiene una lista de todos los productos disponibles en la tabla 'menuss'.
     * @return Una lista de objetos Producto.
     */
    public List<Producto> getAllProductos() {
        List<Producto> productos = new ArrayList<>();
        // Consulta SQL para seleccionar todos los productos
        // Asegúrate que la columna para el ID sea 'codpro' según tu CREATE TABLE
        String sql = "SELECT codpro, nompro, precio, categoria, tipo FROM menuss";

        // Uso de try-with-resources para asegurar que los recursos se cierren automáticamente.
        // Se obtiene la conexión a través de ConnectionManager.getInstance().connect()
        try (Connection conn = ConnectionManager.getInstance().connect(); // ¡Aquí está el cambio clave!
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Itera sobre cada fila del resultado
            while (rs.next()) {
                Producto p = new Producto();
                // Asegúrate que tu clase Producto tenga los métodos setCodpro, setNompro, etc.
                p.setIdCodpro(rs.getInt("id_codpro"));
                p.setNompro(rs.getString("nompro"));
                p.setPrecio(rs.getDouble("precio"));
                p.setCategoria(rs.getString("categoria"));
                p.setTipo(rs.getString("tipo"));
                productos.add(p); // Añade el producto a la lista
            }
        } catch (SQLException e) {
            // Manejo de errores de SQL: imprime la traza para depuración
            System.err.println("Error al obtener productos de la base de datos:");
            e.printStackTrace();
            // Opcional: relanzar la excepción o devolver una lista vacía para manejar en capas superiores
        }
        return productos;
    }

    // --- Puedes añadir aquí otros métodos CRUD para Producto usando la misma conexión ---
    /*
    public Producto getProductoById(int codpro) throws SQLException {
        // Implementación similar a getById de VentaItemDAO
    }

    public boolean addProducto(Producto producto) throws SQLException {
        // Implementación similar a addVentaItem de VentaItemDAO
    }

    public boolean updateProducto(Producto producto) throws SQLException {
        // Implementación similar a update de VentaItemDAO
    }

    public boolean deleteProducto(int codpro) throws SQLException {
        // Implementación similar a delete de VentaItemDAO
    }
    */
}
