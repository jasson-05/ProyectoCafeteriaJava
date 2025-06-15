package esfe.Persistencia;

import esfe.dominio.Menu;
import java.sql.*;
import java.util.ArrayList;

public class MenuDAO {
    private ConnectionManager conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public MenuDAO() {
        conn = ConnectionManager.getInstance();
    }

    public Menu create(Menu menu) throws SQLException {
        Menu result = null;
        try {
            PreparedStatement ps = conn.connect().prepareStatement(
                    "INSERT INTO menuss (nompro, precio, categoria, tipo) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, menu.getNompro());
            ps.setFloat(2, menu.getPrecio());
            ps.setString(3, menu.getCategoria());
            ps.setString(4, menu.getTipo());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    result = getById(generatedId);
                } else {
                    throw new SQLException("Creating menu failed, no ID obtained.");
                }
            }
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al crear el menú: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return result;
    }

    public boolean update(Menu menu) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement(
                    "UPDATE menuss SET nompro = ?, precio = ?, categoria = ?, tipo = ? WHERE id_codpro = ?"
            );
            ps.setString(1, menu.getNompro());
            ps.setFloat(2, menu.getPrecio());
            ps.setString(3, menu.getCategoria());
            ps.setString(4, menu.getTipo());
            ps.setInt(5, menu.getCodpro());  // <-- Aquí estaba el error, faltaba asignar el parámetro 5

            if (ps.executeUpdate() > 0) {
                res = true;
            }
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al modificar el menú: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    public boolean delete(Menu menu) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement("DELETE FROM menuss WHERE id_codpro = ?");
            ps.setInt(1, menu.getCodpro());  // <-- Parámetro asignado correctamente aquí

            if (ps.executeUpdate() > 0) {
                res = true;
            }
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar el menú: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    public ArrayList<Menu> search(String nombre) throws SQLException {
        ArrayList<Menu> list = new ArrayList<>();
        try {
            ps = conn.connect().prepareStatement(
                    "SELECT id_codpro, nompro, precio, categoria, tipo FROM menuss WHERE nompro LIKE ?"
            );
            ps.setString(1, "%" + nombre + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                Menu m = new Menu();
                m.setCodpro(rs.getInt("id_codpro"));  // <-- Falta asignar el id_codpro aquí
                m.setNompro(rs.getString("nompro"));
                m.setPrecio(rs.getFloat("precio"));
                m.setCategoria(rs.getString("categoria"));
                m.setTipo(rs.getString("tipo"));
                list.add(m);
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al buscar menús: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return list;
    }

    public Menu getById(int codpro) throws SQLException {
        Menu menu = null;
        try {
            ps = conn.connect().prepareStatement(
                    "SELECT id_codpro, nompro, precio, categoria, tipo FROM menuss WHERE id_codpro = ?"
            );
            ps.setInt(1, codpro);
            rs = ps.executeQuery();

            if (rs.next()) {
                menu = new Menu();
                menu.setCodpro(rs.getInt("id_codpro"));  // <-- Falta asignar el id_codpro aquí
                menu.setNompro(rs.getString("nompro"));
                menu.setPrecio(rs.getFloat("precio"));
                menu.setCategoria(rs.getString("categoria"));
                menu.setTipo(rs.getString("tipo"));
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener el menú por id: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return menu;
    }
}