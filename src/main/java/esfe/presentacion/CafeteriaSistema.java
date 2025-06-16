package esfe.presentacion;

import esfe.dominio.Menu;
import esfe.Persistencia.MenuDAO; // Cambio de 'Persistencia' a 'persistencia' (minúscula)
import esfe.dominio.orden;
import esfe.Persistencia.OrdenDAO; // Cambio de 'Persistencia' a 'persistencia' (minúscula)
import esfe.dominio.Employee; // Nueva importación
import esfe.Persistencia.EmployeeDAO; // Nueva importación

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CafeteriaSistema {
    private JTabbedPane tabbedPane1;
    private JPanel panel1; // Ivan
    private JPanel Usuario; // Ivan
    private JPanel Orden; // Ivan
    private JPanel Venta; // Ivan
    private JTextField textField1; // Campo para Nombre (Menú) - Ivan
    private JTextField textField2; // Campo para Precio (Menú) - Ivan
    private JTextField textField3; // Campo para Categoria (Menú) - Ivan
    private JTextField textField4; // Campo para Tipo (Menú) - Ivan
    private JLabel Titulo; // Ivan
    private JButton crearButton; // Botón "Crear" (Menú) - Ivan
    private JButton modificarButton; // Botón "Modificar" (Menú) - Ivan
    private JButton eliminarButton; // Botón "Eliminar" (Menú) - Ivan
    private JButton buscarButton; // Botón "Buscar" (Menú) - Ivan
    private JTable table1; // Tu tabla (Menú) - Ivan
    private JLabel NuevoP; // "Nuevo Producto", vinculado por .form - Ivan
    private JPanel Nombre; // JPanel "Nombre" (posiblemente un contenedor), vinculado por .form - Ivan
    private JLabel Precio; // JLabel "Precio", vinculado por .form - Ivan
    private JLabel Categoria; // JLabel "Categoria", vinculado por .form - Ivan
    private JLabel Tipo; // JLabel "Tipo", vinculado por .form - Ivan
    private JPanel JPanel; // Este JPanel tiene un nombre genérico "JPanel", vinculado por .form - Ivan
    private JLabel menu; // JLabel "Nuestro Menu", vinculado por .form - Ivan
    private JTextField textField5; // Campo para Buscar (Menú) - Ivan

    // Ocupados por Emily en el formulario de Orden
    private JFormattedTextField OtxtNumorden;
    private JFormattedTextField OtxtUserid;
    private JFormattedTextField OtxtFecha;
    private JFormattedTextField Otxttotal;
    private JButton ObtnGuardar;
    private JButton ObtnActualizar;
    private JButton ObtnEliminar;
    private JTable OTblaOrden;
    private DefaultTableModel tableModel; // para Orden
    private JButton ObtnActualizarOrdenes;
    private JLabel OlabelNumorden;
    private JLabel OlabelUserid;
    private JLabel OlabelFecha;
    private JLabel Olabeltotal;
    private JLabel OlabelHist;
    private JLabel OlaelTitulo;
    private OrdenDAO ordenDAO; // para Orden

    // Campos y componentes Ocupados por IVAN en el formulario de Empleados
    private JPanel EmpleadosPanel; // El panel que contiene los controles y la tabla de empleados
    private JTextField textNombreEm; // Nombre del empleado
    private JTextField textSalarioEm; // Salario del empleado
    private JTextField textTelefonoEm; // Teléfono del empleado
    private JTextField textPosicionEm; // Posición del empleado
    private JTextField textEmailEm; // Email del empleado (usado como addressEmployees)
    private JTextField textBuscarEm; // Campo de búsqueda para empleados
    private JTable JTableEm; // La tabla para mostrar los empleados
    private JButton buscarButtonEm; // Botón buscar para empleados
    private JButton crearButtonEm; // Botón crear para empleados
    private JButton modificarButtonEm; // Botón modificar para empleados
    private JButton eliminarButtonEm; // Botón eliminar para empleados

    // Instancia del DAO para Empleados
    private EmployeeDAO employeeDAO = new EmployeeDAO();

    public CafeteriaSistema() {
        // Inicializar la tabla de menús
        cargarMenus(""); // Cargar todos los menús al inicio

        // Inicializar la tabla de empleados al inicio
        cargarEmpleados("");

        // Inicializar la tabla de órdenes y listeners (código de Emily)
        ordenDAO = new OrdenDAO();
        initTableOrden(); // Renombrado para evitar conflicto con posibles initTable de empleados
        initListenersOrden(); // Renombrado
        OtxtFecha.setText(LocalDateTime.now().toString());

        // --- Event Listeners para la sección de Menú (código existente de Ivan) ---
        // Evento Crear Menú
        crearButton.addActionListener(e -> {
            try {
                String nombre = textField1.getText();
                // Validar que el precio sea un número y no esté vacío
                if (textField2.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "El campo Precio no puede estar vacío.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                float precio = Float.parseFloat(textField2.getText());
                String categoria = textField3.getText();
                String tipo = textField4.getText();

                // Validación de campos de texto no vacíos (excepto precio ya validado como float)
                if (nombre.isEmpty() || categoria.isEmpty() || tipo.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos de texto (Nombre, Categoría, Tipo) del Menú.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Menu nuevo = new Menu(nombre, precio, categoria, tipo); // Usar el constructor sin codpro
                MenuDAO dao = new MenuDAO();
                dao.create(nuevo);
                cargarMenus(""); // Recargar tabla
                limpiarCamposMenus(); // Usar método específico para menús
                JOptionPane.showMessageDialog(null, "Producto de Menú creado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Error: El precio debe ser un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                mostrarError(ex);
            }
        });

        // Evento Modificar Menú
        modificarButton.addActionListener(e -> {
            try {
                int fila = table1.getSelectedRow();
                if (fila >= 0) {
                    int id = (int) table1.getValueAt(fila, 0); // Obtener ID de la fila seleccionada
                    String nombre = textField1.getText();
                    // Validar que el precio sea un número y no esté vacío
                    if (textField2.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "El campo Precio no puede estar vacío.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    float precio = Float.parseFloat(textField2.getText());
                    String categoria = textField3.getText();
                    String tipo = textField4.getText();

                    // Validación de campos de texto no vacíos
                    if (nombre.isEmpty() || categoria.isEmpty() || tipo.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos de texto (Nombre, Categoría, Tipo) del Menú.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    Menu m = new Menu(id, nombre, precio, categoria, tipo); // Usar constructor con codpro
                    MenuDAO dao = new MenuDAO();
                    dao.update(m);
                    cargarMenus(""); // Recargar tabla
                    limpiarCamposMenus();
                    JOptionPane.showMessageDialog(null, "Producto de Menú modificado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione una fila de Menú para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Error: El precio debe ser un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                mostrarError(ex);
            }
        });

        // Evento Eliminar Menú
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

                    int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar este producto del Menú?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        Menu m = new Menu();
                        m.setCodpro(id);
                        MenuDAO dao = new MenuDAO();
                        dao.delete(m);
                        cargarMenus(""); // Recargar tabla
                        limpiarCamposMenus();
                        JOptionPane.showMessageDialog(null, "Producto de Menú eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione una fila de Menú para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) {
                mostrarError(ex);
            }
        });

        // Evento para seleccionar fila en la tabla de Menú y cargar datos en los JTextFields
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
                    limpiarCamposMenus();
                }
            }
        });

        // Evento Buscar Menú (botón explícito)
        buscarButton.addActionListener(e -> {
            String searchTerm = textField5.getText();
            cargarMenus(searchTerm);
        });

        // Evento para búsqueda en tiempo real mientras el usuario escribe en Menú
        textField5.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String searchTerm = textField5.getText();
                cargarMenus(searchTerm);
            }
        });


        // --- Event Listeners para la sección de Empleados (código CRUD de empleados) ---
        crearButtonEm.addActionListener(e -> crearEmpleado());
        modificarButtonEm.addActionListener(e -> modificarEmpleado());
        eliminarButtonEm.addActionListener(e -> eliminarEmpleado());

        buscarButtonEm.addActionListener(e -> {
            String searchTerm = textBuscarEm.getText();
            cargarEmpleados(searchTerm);
        });

        textBuscarEm.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String searchTerm = textBuscarEm.getText();
                cargarEmpleados(searchTerm);
            }
        });

        JTableEm.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { // Solo actúa cuando la selección finaliza
                cargarEmpleadoSeleccionado();
            }
        });
    }


    /**
     * Carga los elementos del menú en la tabla, opcionalmente filtrados por un término de búsqueda.
     *
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
     * Limpia todos los campos de entrada y la selección de la tabla de Menús.
     */
    private void limpiarCamposMenus() {
        textField1.setText(""); // Nombre
        textField2.setText(""); // Precio
        textField3.setText(""); // Categoría
        textField4.setText(""); // Tipo
        textField5.setText(""); // Búsqueda
        table1.clearSelection(); // Limpiar selección de la tabla
        cargarMenus(""); // Recargar todos los elementos sin filtro para mostrar la lista completa
    }

    /**
     * Carga los datos de los empleados en la tabla de empleados.
     * @param filter Término de búsqueda para filtrar por nombre o posición del empleado.
     */
    private void cargarEmpleados(String filter) {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Las celdas de la tabla de empleados no son editables
            }
        };

        model.addColumn("ID");
        model.addColumn("Nombre");
        model.addColumn("Posición");
        model.addColumn("Salario");
        model.addColumn("Teléfono");
        model.addColumn("Email"); // Usado como AddressEmployees

        try {
            ArrayList<Employee> lista = employeeDAO.search(filter);
            for (Employee emp : lista) {
                model.addRow(new Object[]{
                        emp.getId(),
                        emp.getName(),
                        emp.getPosition(),
                        emp.getSalary(),
                        emp.getPhoneNumber(),
                        emp.getAddressEmployees()
                });
            }
            JTableEm.setModel(model);
        } catch (SQLException ex) {
            mostrarError(ex);
        }
    }

    /**
     * Crea un nuevo empleado utilizando los datos de los campos de texto.
     */
    private void crearEmpleado() {
        try {
            String nombre = textNombreEm.getText();
            String posicion = textPosicionEm.getText();
            // Validar que el salario sea un número y no esté vacío
            if (textSalarioEm.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "El campo Salario de Empleado no puede estar vacío.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }
            double salario = Double.parseDouble(textSalarioEm.getText());
            String telefono = textTelefonoEm.getText();
            String email = textEmailEm.getText(); // Usado como addressEmployees

            // Validación de campos de texto no vacíos
            if (nombre.isEmpty() || posicion.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos de texto de Empleado.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Employee nuevo = new Employee(nombre, posicion, salario, telefono, email);
            employeeDAO.create(nuevo);
            cargarEmpleados(""); // Recargar tabla
            limpiarCamposEmpleados();
            JOptionPane.showMessageDialog(null, "Empleado creado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Error: El salario debe ser un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            mostrarError(ex);
        } catch (Exception ex) {
            mostrarError(ex);
        }
    }

    /**
     * Modifica un empleado existente utilizando los datos de los campos de texto.
     */
    private void modificarEmpleado() {
        int fila = JTableEm.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione una fila de empleado para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Asegurarse de que el ID sea un número
            Object idObj = JTableEm.getValueAt(fila, 0);
            int id;
            if (idObj instanceof Number) {
                id = ((Number) idObj).intValue();
            } else {
                id = Integer.parseInt(idObj.toString());
            }

            String nombre = textNombreEm.getText();
            String posicion = textPosicionEm.getText();
            if (textSalarioEm.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "El campo Salario de Empleado no puede estar vacío.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }
            double salario = Double.parseDouble(textSalarioEm.getText());
            String telefono = textTelefonoEm.getText();
            String email = textEmailEm.getText(); // Usado como addressEmployees

            // Validación de campos de texto no vacíos
            if (nombre.isEmpty() || posicion.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos de texto de Empleado.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Employee emp = new Employee(id, nombre, posicion, salario, telefono, email);
            if (employeeDAO.update(emp)) {
                cargarEmpleados(""); // Recargar tabla
                limpiarCamposEmpleados();
                JOptionPane.showMessageDialog(null, "Empleado modificado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo modificar el empleado. ID no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Error: El salario debe ser un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            mostrarError(ex);
        } catch (Exception ex) {
            mostrarError(ex);
        }
    }

    /**
     * Elimina un empleado seleccionado de la tabla.
     */
    private void eliminarEmpleado() {
        int fila = JTableEm.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione una fila de empleado para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Object idObj = JTableEm.getValueAt(fila, 0);
            int id;
            if (idObj instanceof Number) {
                id = ((Number) idObj).intValue();
            } else {
                id = Integer.parseInt(idObj.toString());
            }

            int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar a este empleado?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                Employee emp = new Employee();
                emp.setId(id);
                if (employeeDAO.delete(emp)) {
                    cargarEmpleados(""); // Recargar tabla
                    limpiarCamposEmpleados();
                    JOptionPane.showMessageDialog(null, "Empleado eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "No se pudo eliminar el empleado. ID no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            mostrarError(ex);
        } catch (Exception ex) {
            mostrarError(ex);
        }
    }

    /**
     * Carga los datos del empleado seleccionado de la tabla en los campos de texto.
     */
    private void cargarEmpleadoSeleccionado() {
        int fila = JTableEm.getSelectedRow();
        if (fila >= 0) {
            textNombreEm.setText(JTableEm.getValueAt(fila, 1) != null ? JTableEm.getValueAt(fila, 1).toString() : "");
            textPosicionEm.setText(JTableEm.getValueAt(fila, 2) != null ? JTableEm.getValueAt(fila, 2).toString() : "");
            textSalarioEm.setText(JTableEm.getValueAt(fila, 3) != null ? JTableEm.getValueAt(fila, 3).toString() : "");
            textTelefonoEm.setText(JTableEm.getValueAt(fila, 4) != null ? JTableEm.getValueAt(fila, 4).toString() : "");
            textEmailEm.setText(JTableEm.getValueAt(fila, 5) != null ? JTableEm.getValueAt(fila, 5).toString() : "");
        } else {
            // Si no hay fila seleccionada (ej. después de eliminar), limpiar campos
            limpiarCamposEmpleados();
        }
    }

    /**
     * Limpia todos los campos de entrada y la selección de la tabla de Empleados.
     */
    private void limpiarCamposEmpleados() {
        textNombreEm.setText("");
        textPosicionEm.setText("");
        textSalarioEm.setText("");
        textTelefonoEm.setText("");
        textEmailEm.setText("");
        textBuscarEm.setText("");
        JTableEm.clearSelection();
        cargarEmpleados(""); // Recargar todos los elementos sin filtro
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
     * Muestra un mensaje de error específico.
     * @param message El mensaje de error a mostrar.
     */
    private void mostrarError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
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
            JFrame frame = new JFrame("Sistema de Gestión de Cafetería");
            CafeteriaSistema sistema = new CafeteriaSistema(); // Instancia la clase
            frame.setContentPane(sistema.getMainPanel()); // Asigna el panel principal cargado por el .form
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack(); // Ajusta el tamaño de la ventana al contenido
            frame.setLocationRelativeTo(null); // Centra la ventana en la pantalla
            frame.setVisible(true);
        });
    }

    // -------- CÓDIGO DE EMILY PARA ÓRDENES (métodos adaptados) ---------
    private void initTableOrden() {
        tableModel = new DefaultTableModel(new String[]{"ID", "UserID", "Total", "Fecha"}, 0);
        OTblaOrden.setModel(tableModel);
        cargarDatosOrden(); // Cargar datos al inicializar la tabla de órdenes
    }

    private void initListenersOrden() {
        ObtnActualizarOrdenes.addActionListener(e -> cargarDatosOrden());

        ObtnGuardar.addActionListener(e -> {
            try {
                // Validaciones para campos de Orden
                if (OtxtUserid.getText().isEmpty() || Otxttotal.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, complete UserID y Total de la Orden.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                orden nueva = new orden();
                nueva.setUserId(Integer.parseInt(OtxtUserid.getText()));
                nueva.setTotalAmount(Double.parseDouble(Otxttotal.getText()));
                nueva.setOrderDate(LocalDateTime.now());
                ordenDAO.create(nueva);
                cargarDatosOrden();
                limpiarCamposOrden();
                JOptionPane.showMessageDialog(null, "Orden creada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Error: UserID y Total deben ser números válidos para la Orden.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                mostrarError(ex);
            }
        });
        ObtnActualizar.addActionListener(e -> {
            try {
                if (OtxtNumorden.getText().isEmpty() || OtxtUserid.getText().isEmpty() || Otxttotal.getText().isEmpty() || OtxtFecha.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione una orden y complete todos los campos para actualizar.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                orden o = new orden(
                        Integer.parseInt(OtxtNumorden.getText()),
                        Integer.parseInt(OtxtUserid.getText()),
                        Double.parseDouble(Otxttotal.getText()),
                        LocalDateTime.parse(OtxtFecha.getText())
                );
                ordenDAO.update(o);
                cargarDatosOrden();
                limpiarCamposOrden();
                JOptionPane.showMessageDialog(null, "Orden actualizada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Error: Los campos numéricos de la Orden deben ser válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                mostrarError(ex);
            }
        });

        ObtnEliminar.addActionListener(e -> {
            try {
                if (OtxtNumorden.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione una orden para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int id = Integer.parseInt(OtxtNumorden.getText());
                int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar esta orden?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    ordenDAO.delete(id);
                    cargarDatosOrden();
                    limpiarCamposOrden();
                    JOptionPane.showMessageDialog(null, "Orden eliminada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Error: El ID de la Orden debe ser un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                mostrarError(ex);
            }
        });

        OTblaOrden.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = OTblaOrden.getSelectedRow();
                if (fila >= 0) {
                    OtxtNumorden.setText(tableModel.getValueAt(fila, 0) != null ? tableModel.getValueAt(fila, 0).toString() : "");
                    OtxtUserid.setText(tableModel.getValueAt(fila, 1) != null ? tableModel.getValueAt(fila, 1).toString() : "");
                    Otxttotal.setText(tableModel.getValueAt(fila, 2) != null ? tableModel.getValueAt(fila, 2).toString() : "");
                    OtxtFecha.setText(tableModel.getValueAt(fila, 3) != null ? tableModel.getValueAt(fila, 3).toString() : "");
                } else {
                    limpiarCamposOrden();
                }
            }
        });
    }

    private void cargarDatosOrden() {
        try {
            tableModel.setRowCount(0);
            List<orden> ordenes = ordenDAO.getAll();
            for (orden o : ordenes) {
                tableModel.addRow(new Object[]{
                        o.getId(),
                        o.getUserId(),
                        o.getTotalAmount(),
                        o.getOrderDate()
                });
            }
        } catch (SQLException e) {
            mostrarError(e);
        }
    }

    private void limpiarCamposOrden() {
        OtxtNumorden.setText("");
        OtxtUserid.setText("");
        Otxttotal.setText("");
        OtxtFecha.setText(LocalDateTime.now().toString()); // Resetear a la fecha actual
        OTblaOrden.clearSelection(); // Limpiar selección de la tabla de órdenes
    }
}
