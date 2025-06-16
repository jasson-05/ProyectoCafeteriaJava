package esfe.Persistencia;

import esfe.dominio.VentaItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class VentaItemDAO {

    // ... (otras variables y constructor si los tienes)

    public VentaItem getById(int id) throws SQLException {
        String sql = "SELECT idventa, nomProduct, precioProduct, nombreClient, estado, fecha FROM vent WHERE idventa = ?";
        try (Connection conn = ConnectionManager.getInstance().connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    VentaItem item = new VentaItem();
                    item.setIdventa(rs.getInt("idventa"));
                    item.setNomProduct(rs.getInt("nomProduct"));
                    item.setPrecioProduct(rs.getDouble("precioProduct"));
                    item.setNombreClient(rs.getString("nombreClient"));
                    item.setEstado(rs.getString("estado"));

                    // Manejo de la fecha
                    Timestamp timestamp = rs.getTimestamp("fecha");
                    if (timestamp != null) {
                        item.setFecha(timestamp.toLocalDateTime());
                    } else {
                        item.setFecha(null); // O maneja el caso de fecha nula como prefieras
                    }
                    return item;
                }
            }
        }
        return null;
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM vent WHERE idventa = ?";
        try (Connection conn = ConnectionManager.getInstance().connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Retorna true si se eliminó al menos una fila
        }
    }

    public ArrayList<VentaItem> getAll() throws SQLException {
        ArrayList<VentaItem> ventaItems = new ArrayList<>();
        String sql = "SELECT idventa, nomProduct, precioProduct, nombreClient, estado, fecha FROM vent";
        try (Connection conn = ConnectionManager.getInstance().connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                VentaItem item = new VentaItem();
                item.setIdventa(rs.getInt("idventa"));
                item.setNomProduct(rs.getInt("nomProduct"));
                item.setPrecioProduct(rs.getDouble("precioProduct"));
                item.setNombreClient(rs.getString("nombreClient"));
                item.setEstado(rs.getString("estado"));

                // Manejo de la fecha
                Timestamp timestamp = rs.getTimestamp("fecha");
                if (timestamp != null) {
                    item.setFecha(timestamp.toLocalDateTime());
                } else {
                    item.setFecha(null); // O maneja el caso de fecha nula como prefieras
                }
                ventaItems.add(item);
            }
        }
        return ventaItems;
    }

    public ArrayList<VentaItem> search(String searchTerm) throws SQLException {
        ArrayList<VentaItem> ventaItems = new ArrayList<>();
        // Usamos LIKE para buscar nombres de cliente que contengan el término de búsqueda
        String sql = "SELECT idventa, nomProduct, precioProduct, nombreClient, estado, fecha FROM vent WHERE nombreClient LIKE ?";
        try (Connection conn = ConnectionManager.getInstance().connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + searchTerm + "%"); // El % permite buscar coincidencias parciales

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    VentaItem item = new VentaItem();
                    item.setIdventa(rs.getInt("idventa"));
                    item.setNomProduct(rs.getInt("nomProduct"));
                    item.setPrecioProduct(rs.getDouble("precioProduct"));
                    item.setNombreClient(rs.getString("nombreClient"));
                    item.setEstado(rs.getString("estado"));

                    // Manejo de la fecha
                    Timestamp timestamp = rs.getTimestamp("fecha");
                    if (timestamp != null) {
                        item.setFecha(timestamp.toLocalDateTime());
                    } else {
                        item.setFecha(null);
                    }
                    ventaItems.add(item);
                }
            }
        }
        return ventaItems;
    }

    public VentaItem addVentaItem(VentaItem item) throws SQLException {
        String sql = "INSERT INTO vent (nomProduct, precioProduct, nombreClient, estado, fecha) VALUES (?, ?, ?, ?, ?)";
        // Añadimos Statement.RETURN_GENERATED_KEYS para obtener el ID generado
        try (Connection conn = ConnectionManager.getInstance().connect();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, item.getNomProduct());
            stmt.setDouble(2, item.getPrecioProduct());
            stmt.setString(3, item.getNombreClient());
            stmt.setString(4, item.getEstado());
            stmt.setTimestamp(5, Timestamp.valueOf(item.getFecha())); // Convertir LocalDateTime a Timestamp

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Recuperar el ID generado por la base de datos
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        item.setIdventa(generatedKeys.getInt(1)); // Asignar el ID al objeto VentaItem
                    }
                }
                return item; // Retorna el objeto con el ID actualizado
            }
        }
        return null; // En caso de que no se inserte ninguna fila
    }

    public boolean update(VentaItem item) throws SQLException {
        // Asegúrate de que el idventa no sea 0 para actualizar
        if (item.getIdventa() == 0) {
            System.err.println("Error: No se puede actualizar un VentaItem sin un ID válido.");
            return false;
        }

        String sql = "UPDATE vent SET nomProduct = ?, precioProduct = ?, nombreClient = ?, estado = ?, fecha = ? WHERE idventa = ?";
        try (Connection conn = ConnectionManager.getInstance().connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, item.getNomProduct());
            stmt.setDouble(2, item.getPrecioProduct());
            stmt.setString(3, item.getNombreClient());
            stmt.setString(4, item.getEstado());
            stmt.setTimestamp(5, Timestamp.valueOf(item.getFecha())); // Convertir LocalDateTime a Timestamp
            stmt.setInt(6, item.getIdventa()); // Usar el ID para la cláusula WHERE

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Retorna true si se actualizó al menos una fila
        }
    }


    // ... (otros métodos como addVentaItem, update, delete, getAll, search)
}