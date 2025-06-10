package esfe;

import javax.swing.*;

public class FormInicioSesion {
    private JButton iniciarSesiónButton;
    private JFormattedTextField formattedTextField1;
    private JButton registrarseButton;
    private JFormattedTextField formattedTextField2;

    public static void main(String[] args) {
        // Ejecutar la GUI en el Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            new FormInicioSesion();
        });
    }
}
