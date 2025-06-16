package esfe.Persistencia;

import esfe.dominio.orden;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class OrdenDAO {

    // Cadena de conexión a la base de datos SQL Server
    private static final String STR_CONNECTION = "jdbc:sqlserver://DESKTOP-UKKJ1T9\\SQLEXPRESS01;" +
            "encrypt=true;" +
            "database=Cafeteria_BD;" +
            "trustServerCertificate=true;" +
            "user=Java2025;" +
            "password=12345;";
    /**
    /**
     * Retorna una nueva conexión a la base de datos.
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(STR_CONNECTION);
    }

    /**
     * Inserta una nueva orden y devuelve la orden con el ID generado.
     */
    public orden create(orden order) throws SQLException {
        String sql = "INSERT INTO Orders (UserId, TotalAmount, OrderDate) OUTPUT INSERTED.ID VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, order.getUserId());
            stmt.setDouble(2, order.getTotalAmount());
            stmt.setTimestamp(3, Timestamp.valueOf(order.getOrderDate()));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                order.setId(rs.getInt(1));
                return order;
            }
        }

        return null;
    }

    /**
     * Actualiza una orden existente.
     */
    public boolean update(orden order) throws SQLException {
        String sql = "UPDATE Orders SET UserId = ?, TotalAmount = ?, OrderDate = ? WHERE Id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, order.getUserId());
            stmt.setDouble(2, order.getTotalAmount());
            stmt.setTimestamp(3, Timestamp.valueOf(order.getOrderDate()));
            stmt.setInt(4, order.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Elimina una orden por ID.
     */
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM Orders WHERE Id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Obtiene una orden por su ID.
     */
    public orden getById(int id) throws SQLException {
        String sql = "SELECT * FROM Orders WHERE Id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new orden(
                        rs.getInt("Id"),
                        rs.getInt("UserId"),
                        rs.getDouble("TotalAmount"),
                        rs.getTimestamp("OrderDate").toLocalDateTime()
                );
            }
        }

        return null;
    }

    /**
     * Devuelve todas las órdenes registradas.
     */
    public List<orden> getAll() throws SQLException {
        List<orden> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                orders.add(new orden(
                        rs.getInt("Id"),
                        rs.getInt("UserId"),
                        rs.getDouble("TotalAmount"),
                        rs.getTimestamp("OrderDate").toLocalDateTime()
                ));
            }
        }

        return orders;
    }
}
