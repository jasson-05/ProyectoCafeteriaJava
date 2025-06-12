package esfe.Persistencia;

import esfe.dominio.orden;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class OrdenDAO {

    private final String url = "jdbc:sqlserver://EMYLIS\\SQLEXPRESS;databaseName=CafeteriaDB;encrypt=true;trustServerCertificate=true;";
    private final String user = "java2025";
    private final String password = "12345";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public orden create(orden order) throws SQLException {
        String sql = "INSERT INTO Orders (UserId, TotalAmount, OrderDate) OUTPUT INSERTED.ID VALUES (?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
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

    public boolean update(orden order) throws SQLException {
        String sql = "UPDATE Orders SET UserId = ?, TotalAmount = ?, OrderDate = ? WHERE Id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, order.getUserId());
            stmt.setDouble(2, order.getTotalAmount());
            stmt.setTimestamp(3, Timestamp.valueOf(order.getOrderDate()));
            stmt.setInt(4, order.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM Orders WHERE Id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public orden getById(int id) throws SQLException {
        String sql = "SELECT * FROM Orders WHERE Id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
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

    public List<orden> getAll() throws SQLException {
        List<orden> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
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
