package esfe.presentacion;

import esfe.dominio.Menu;
import esfe.Persistencia.MenuDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class CafeteriaSistema {
    private JTabbedPane tabbedPane1;
    private JPanel panel1; // Este es tu panel principal, ya lo vincula el .form
    private JPanel Usuario; // Panel dentro del tabbedPane, vinculado por .form
    private JPanel Orden;
    private JPanel Venta;
    private JPanel Empleados;
    private JTextField textField1; // Campo para Nombre
    private JTextField textField2; // Campo para Precio
    private JTextField textField3; // Campo para Categoria
    private JTextField textField4; // Campo para Tipo
    private JLabel Titulo; // "Cup of Coffee", vinculado por .form
    private JButton crearButton; // Botón "Crear", vinculado por .form
    private JButton modificarButton; // Botón "Modificar", vinculado por .form
    private JButton eliminarButton; // Botón "Eliminar", vinculado por .form
    private JButton buscarButton; // Botón "Buscar", vinculado por .form
    private JTable table1; // Tu tabla, vinculada por .form
    private JLabel NuevoP; // "Nuevo Producto", vinculado por .form
    private JPanel Nombre; // JPanel "Nombre" (posiblemente un contenedor), vinculado por .form
    private JLabel Precio; // JLabel "Precio", vinculado por .form
    private JLabel Categoria; // JLabel "Categoria", vinculado por .form
    private JLabel Tipo; // JLabel "Tipo", vinculado por .form
    private JPanel JPanel; // Este JPanel tiene un nombre genérico "JPanel", vinculado por .form
    private JLabel menu; // JLabel "Nuestro Menu", vinculado por .form
    private JTextField textField5; // Campo para Buscar

    // No necesitas inicializar manualmente los componentes aquí si usas un diseñador de UI
    // El diseñador lo hace a través de código generado (ej. $$$setupUI$$$())

    public CafeteriaSistema() {
        // El constructor de CafeteriaSistema es el lugar para inicializar la lógica,
        // NO para crear los componentes de UI si ya están definidos en el .form.
        // Los componentes (textField1, crearButton, table1, etc.) ya estarán inicializados
        // por el código generado del diseñador de UI cuando se llegue a este constructor.

        cargarMenus(""); // Cargar todos los menús al inicio

        // --- Event Listeners ---

        // Evento Crear
        crearButton.addActionListener(e -> {
            try {
                String nombre = textField1.getText();
                float precio = Float.parseFloat(textField2.getText());
                String categoria = textField3.getText();
                String tipo = textField4.getText();

                // Validación de campos no vacíos
                if (nombre.isEmpty() || categoria.isEmpty() || tipo.isEmpty() || textField2.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos de texto.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Menu nuevo = new Menu(nombre, precio, categoria, tipo); // Usar el constructor sin codpro
                MenuDAO dao = new MenuDAO();
                dao.create(nuevo);
                cargarMenus(""); // Recargar tabla
                limpiarCampos();
                JOptionPane.showMessageDialog(null, "Producto creado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Error: El precio debe ser un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                mostrarError(ex);
            }
        });

        // Evento Modificar
        modificarButton.addActionListener(e -> {
            try {
                int fila = table1.getSelectedRow();
                if (fila >= 0) {
                    int id = (int) table1.getValueAt(fila, 0); // Obtener ID de la fila seleccionada
                    String nombre = textField1.getText();
                    float precio = Float.parseFloat(textField2.getText());
                    String categoria = textField3.getText();
                    String tipo = textField4.getText();

                    // Validación de campos no vacíos
                    if (nombre.isEmpty() || categoria.isEmpty() || tipo.isEmpty() || textField2.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos de texto.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    Menu m = new Menu(id, nombre, precio, categoria, tipo); // Usar constructor con codpro
                    MenuDAO dao = new MenuDAO();
                    dao.update(m);
                    cargarMenus(""); // Recargar tabla
                    limpiarCampos();
                    JOptionPane.showMessageDialog(null, "Producto modificado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione una fila para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Error: El precio debe ser un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                mostrarError(ex);
            }
        });

        // Evento Eliminar
        eliminarButton.addActionListener(e -> {
            try {
                int fila = table1.getSelectedRow();
                if (fila >= 0) {
                    // Manejo de casteo de ID
                    Object idObj = table1.getValueAt(fila, 0);
                    int id;
                    if (idObj instanceof Number) {
                        id = ((Number) idObj).intValue();
                    } else {
                        id = Integer.parseInt(idObj.toString());
                    }

                    int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar este producto?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        Menu m = new Menu();
                        m.setCodpro(id);
                        MenuDAO dao = new MenuDAO();
                        dao.delete(m);
                        cargarMenus(""); // Recargar tabla
                        limpiarCampos();
                        JOptionPane.showMessageDialog(null, "Producto eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione una fila para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) {
                mostrarError(ex);
            }
        });

        // Evento para seleccionar fila en la tabla y cargar datos en los JTextFields
        table1.getSelectionModel().addListSelectionListener(e -> {
            // Este evento puede dispararse múltiples veces. Solo actúa cuando la selección finaliza.
            if (!e.getValueIsAdjusting()) {
                int fila = table1.getSelectedRow();
                if (fila >= 0) {
                    // Asegurarse de que los valores no sean nulos antes de convertirlos a String
                    textField1.setText(table1.getValueAt(fila, 1) != null ? table1.getValueAt(fila, 1).toString() : ""); // Nombre
                    textField2.setText(table1.getValueAt(fila, 2) != null ? table1.getValueAt(fila, 2).toString() : ""); // Precio
                    textField3.setText(table1.getValueAt(fila, 3) != null ? table1.getValueAt(fila, 3).toString() : ""); // Categoría
                    textField4.setText(table1.getValueAt(fila, 4) != null ? table1.getValueAt(fila, 4).toString() : ""); // Tipo
                } else {
                    // Si no hay fila seleccionada (ej. después de eliminar), limpiar campos
                    limpiarCampos();
                }
            }
        });

        // Evento Buscar (botón explícito)
        buscarButton.addActionListener(e -> {
            String searchTerm = textField5.getText();
            cargarMenus(searchTerm);
        });

        // Evento para búsqueda en tiempo real mientras el usuario escribe
        textField5.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String searchTerm = textField5.getText();
                cargarMenus(searchTerm);
            }
        });

        // Nota: Necesitas un botón "limpiar" en tu formulario si quieres usar este.
        // Si no tienes uno, las siguientes 3 líneas no tendrán efecto.
        // Si no lo tienes, puedes asignar esta acción a otro botón o eliminarla.
        // Si lo tienes, asegúrate de que el "field name" en el diseñador sea 'limpiarButton'.
        // Aquí simulamos que sí existe un botón limpiarButton para su funcionalidad:
        // Por la captura, no lo veo, pero es buena práctica tenerlo.
        // Si 'limpiarButton' no está declarado en tu .form, esta línea dará un NullPointerException
        // si no lo inicializas manualmente (pero no deberías inicializar manualmente).
        // Por ahora, asumiré que QUIERES uno y quizás lo agregarás a tu .form.
        // Si no lo tienes y no lo quieres, puedes comentar/eliminar la línea de abajo.
        // limpiarButton.addActionListener(e -> limpiarCampos()); // <-- Reemplaza esto con un botón real si no lo tienes
        // Dado que tu captura no muestra un botón de limpiar, el "limpiarCampos()"
        // se ejecuta después de cada CRUD (crear, modificar, eliminar)
        // y cuando no hay selección en la tabla.
        // Si realmente quieres un botón "Limpiar", DEBES agregarlo a tu .form
        // y nombrarlo 'limpiarButton'.
    }

    /**
     * Carga los elementos del menú en la tabla, opcionalmente filtrados por un término de búsqueda.
     * @param filter El término de búsqueda. Una cadena vacía ("") carga todos los menús.
     */
    private void cargarMenus(String filter) {
        try {
            MenuDAO dao = new MenuDAO();
            ArrayList<Menu> lista = dao.search(filter); // Llama al método search de DAO

            // 1. Crea el modelo de tabla
            DefaultTableModel model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Hace que las celdas de la tabla no sean editables directamente
                }
            };

            // 2. Define las columnas
            model.addColumn("ID");
            model.addColumn("Nombre");
            model.addColumn("Precio");
            model.addColumn("Categoría");
            model.addColumn("Tipo");

            // 3. Agrega las filas al modelo
            for (Menu m : lista) {
                model.addRow(new Object[]{
                        m.getCodpro(), m.getNompro(), m.getPrecio(), m.getCategoria(), m.getTipo()
                });
            }

            // 4. Asigna el modelo a la tabla
            table1.setModel(model);

        } catch (Exception e) {
            mostrarError(e);
        }
    }

    /**
     * Limpia todos los campos de entrada y la selección de la tabla.
     */
    private void limpiarCampos() {
        textField1.setText(""); // Nombre
        textField2.setText(""); // Precio
        textField3.setText(""); // Categoría
        textField4.setText(""); // Tipo
        textField5.setText(""); // Búsqueda
        table1.clearSelection(); // Limpiar selección de la tabla
        cargarMenus(""); // Recargar todos los elementos sin filtro para mostrar la lista completa
    }

    /**
     * Muestra un mensaje de error al usuario y imprime la traza de la pila para depuración.
     * @param e La excepción que ocurrió.
     */
    private void mostrarError(Exception e) {
        e.printStackTrace(); // Imprime la traza de la pila en la consola para depuración
        JOptionPane.showMessageDialog(null, "Ha ocurrido un error: " + e.getMessage(), "Error del Sistema", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Retorna el panel principal de la aplicación, el cual es cargado por el diseñador de UI.
     * @return El JPanel principal.
     */
    public JPanel getMainPanel() {
        return panel1; // panel1 ya está enlazado por el diseñador de UI
    }

    public static void main(String[] args) {
        // Asegurarse de que las actualizaciones de la GUI se realicen en el Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Sistema de Gestión de Menú de Cafetería");
            CafeteriaSistema sistema = new CafeteriaSistema(); // Instancia la clase
            frame.setContentPane(sistema.getMainPanel()); // Asigna el panel principal cargado por el .form
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack(); // Ajusta el tamaño de la ventana al contenido
            // frame.setSize(800, 600); // Puedes usar pack() o setSize()
            frame.setLocationRelativeTo(null); // Centra la ventana en la pantalla
            frame.setVisible(true);
        });
    }
}