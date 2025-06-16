package esfe.utils; // Manteniendo tu paquete original

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import esfe.Persistencia.ConnectionManager; // Asegúrate de importar tu ConnectionManager

public class VentasCrudMainFrame extends JFrame {

    public VentasCrudMainFrame() {
        setTitle("Sistema CRUD de Ventas - Cafetería");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana

        // Crear e integrar el panel de ventas CRUD
        VentasPanelCrud ventasPanel = new VentasPanelCrud(this);
        add(ventasPanel, BorderLayout.CENTER);

        // Asegurarse de cerrar la conexión de la base de datos al cerrar la aplicación
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    ConnectionManager.getInstance().disconnect();
                    System.out.println("Conexión a la base de datos cerrada al salir.");
                } catch (SQLException ex) {
                    System.err.println("Error al cerrar la conexión de la base de datos: " + ex.getMessage());
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al cerrar la conexión de la base de datos: " + ex.getMessage(), "Error de Conexión", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void main(String[] args) {
        // Ejecutar la GUI en el Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new VentasCrudMainFrame().setVisible(true);
            }
        });
    }
}
