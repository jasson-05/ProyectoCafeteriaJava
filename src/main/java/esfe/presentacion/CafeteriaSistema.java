package esfe.presentacion;

import esfe.dominio.Menu;
import esfe.Persistencia.MenuDAO;
import esfe.dominio.orden;
import esfe.Persistencia.OrdenDAO;
import esfe.dominio.Employee;
import esfe.Persistencia.EmployeeDAO;
import esfe.dominio.Venta;       // Nueva importación
import esfe.Persistencia.VentaDAO; // Nueva importación

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter; // Para formatear la fecha

public class CafeteriaSistema {
    private JTabbedPane tabbedPane1;
    private JPanel panel1; // Ivan - Panel principal
    private JPanel Usuario; // Ivan - Pestaña de Usuario (Empleados)
    private JPanel Orden; // Ivan - Pestaña de Orden
    private JPanel Venta; // Ivan - Pestaña de Venta (¡Este es tu nuevo panel para el formulario de Ventas!)

    // Componentes de Menú (Ivan)
    private JTextField textField1; // Campo para Nombre (Menú)
    private JTextField textField2; // Campo para Precio (Menú)
    private JTextField textField3; // Campo para Categoria (Menú)
    private JTextField textField4; // Campo para Tipo (Menú)
    private JLabel Titulo;
    private JButton crearButton;
    private JButton modificarButton;
    private JButton eliminarButton;
    private JButton buscarButton;
    private JTable table1; // Tabla de Menú
    private JLabel NuevoP;
    private JPanel Nombre;
    private JLabel Precio;
    private JLabel Categoria;
    private JLabel Tipo;
    private JPanel JPanel;
    private JLabel menu;
    private JTextField textField5; // Campo para Buscar (Menú)

    // Componentes de Orden (Emily)
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

    // Componentes de Empleados (Ivan)
    private JPanel EmpleadosPanel;
    private JTextField textNombreEm;
    private JTextField textSalarioEm;
    private JTextField textTelefonoEm;
    private JTextField textPosicionEm;
    private JTextField textEmailEm;
    private JTextField textBuscarEm;
    private JTable JTableEm;
    private JButton buscarButtonEm;
    private JButton crearButtonEm;
    private JButton modificarButtonEm;
    private JButton eliminarButtonEm;

    // Instancia del DAO para Empleados
    private EmployeeDAO employeeDAO = new EmployeeDAO();

    // --- NUEVOS CAMPOS Y COMPONENTES PARA LA SECCIÓN DE VENTAS ---
    // (Asegúrate de vincular estos en tu archivo .form dentro del JPanel 'Venta')
    private JTextField textVentaNomProducto; // nomProducto (ID del producto vendido)
    private JTextField textVentaPrecioProducto; // precioProducto
    private JTextField textVentaNombreCliente; // nombreCliente
    private JComboBox<String> comboVentaEstado; // estado (PENDIENTE, ENVIADO, ENTREGADO)
    private JLabel labelVentaFecha; // fecha (muestra la fecha, se autogenera en create, se carga en select)
    private JTextField textBuscarVenta; // Campo de búsqueda para ventas por nombre de cliente
    private JTable JTableVentas; // La tabla para mostrar las ventas
    private JButton crearButtonVenta; // Botón "Crear Venta"
    private JButton modificarButtonVenta; // Botón "Modificar Venta"
    private JButton eliminarButtonVenta; // Botón "Eliminar Venta"
    private JButton buscarButtonVenta; // Botón "Buscar Venta" (para el campo de búsqueda)

    // Instancia del DAO para Ventas
    private VentaDAO ventaDAO = new VentaDAO();
    // Formateador para mostrar y parsear LocalDateTime
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public CafeteriaSistema() {
        // Inicializar la tabla de menús
        cargarMenus(""); // Cargar todos los menús al inicio

        // Inicializar la tabla de empleados al inicio
        cargarEmpleados("");

        // Inicializar la tabla de órdenes y listeners (código de Emily)
        ordenDAO = new OrdenDAO();
        initTableOrden();
        initListenersOrden();
        OtxtFecha.setText(LocalDateTime.now().toString());

        // --- INICIALIZACIÓN Y LISTENERS PARA LA SECCIÓN DE VENTAS (NUEVO) ---
        initVentasTab(); // Inicializa el JComboBox y la fecha inicial
        cargarVentas(""); // Cargar todas las ventas al inicio

        // Event Listeners para la sección de Menú (código existente de Ivan)
        crearButton.addActionListener(e -> {
            try {
                String nombre = textField1.getText();
                if (textField2.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "El campo Precio no puede estar vacío.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                float precio = Float.parseFloat(textField2.getText());
                String categoria = textField3.getText();
                String tipo = textField4.getText();

                if (nombre.isEmpty() || categoria.isEmpty() || tipo.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos de texto (Nombre, Categoría, Tipo) del Menú.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Menu nuevo = new Menu(nombre, precio, categoria, tipo);
                MenuDAO dao = new MenuDAO();
                dao.create(nuevo);
                cargarMenus("");
                limpiarCamposMenus();
                JOptionPane.showMessageDialog(null, "Producto de Menú creado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Error: El precio debe ser un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                mostrarError(ex);
            }
        });

        modificarButton.addActionListener(e -> {
            try {
                int fila = table1.getSelectedRow();
                if (fila >= 0) {
                    int id = (int) table1.getValueAt(fila, 0);
                    String nombre = textField1.getText();
                    if (textField2.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "El campo Precio no puede estar vacío.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    float precio = Float.parseFloat(textField2.getText());
                    String categoria = textField3.getText();
                    String tipo = textField4.getText();

                    if (nombre.isEmpty() || categoria.isEmpty() || tipo.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos de texto (Nombre, Categoría, Tipo) del Menú.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    Menu m = new Menu(id, nombre, precio, categoria, tipo);
                    MenuDAO dao = new MenuDAO();
                    dao.update(m);
                    cargarMenus("");
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

        eliminarButton.addActionListener(e -> {
            try {
                int fila = table1.getSelectedRow();
                if (fila >= 0) {
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
                        cargarMenus("");
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

        table1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = table1.getSelectedRow();
                if (fila >= 0) {
                    textField1.setText(table1.getValueAt(fila, 1) != null ? table1.getValueAt(fila, 1).toString() : "");
                    textField2.setText(table1.getValueAt(fila, 2) != null ? table1.getValueAt(fila, 2).toString() : "");
                    textField3.setText(table1.getValueAt(fila, 3) != null ? table1.getValueAt(fila, 3).toString() : "");
                    textField4.setText(table1.getValueAt(fila, 4) != null ? table1.getValueAt(fila, 4).toString() : "");
                } else {
                    limpiarCamposMenus();
                }
            }
        });

        buscarButton.addActionListener(e -> {
            String searchTerm = textField5.getText();
            cargarMenus(searchTerm);
        });

        textField5.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String searchTerm = textField5.getText();
                cargarMenus(searchTerm);
            }
        });


        // Event Listeners para la sección de Empleados (código CRUD de empleados)
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
            if (!e.getValueIsAdjusting()) {
                cargarEmpleadoSeleccionado();
            }
        });

        // --- LISTENERS PARA LA SECCIÓN DE VENTAS (NUEVO) ---
        crearButtonVenta.addActionListener(e -> crearVenta());
        modificarButtonVenta.addActionListener(e -> modificarVenta());
        eliminarButtonVenta.addActionListener(e -> eliminarVenta());

        buscarButtonVenta.addActionListener(e -> {
            String searchTerm = textBuscarVenta.getText();
            cargarVentas(searchTerm);
        });

        textBuscarVenta.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String searchTerm = textBuscarVenta.getText();
                cargarVentas(searchTerm);
            }
        });

        JTableVentas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarVentaSeleccionada();
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
            ArrayList<Menu> lista = dao.search(filter);

            DefaultTableModel model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            model.addColumn("ID");
            model.addColumn("Nombre");
            model.addColumn("Precio");
            model.addColumn("Categoría");
            model.addColumn("Tipo");

            for (Menu m : lista) {
                model.addRow(new Object[]{
                        m.getCodpro(), m.getNompro(), m.getPrecio(), m.getCategoria(), m.getTipo()
                });
            }

            table1.setModel(model);

        } catch (Exception e) {
            mostrarError(e);
        }
    }

    /**
     * Limpia todos los campos de entrada y la selección de la tabla de Menús.
     */
    private void limpiarCamposMenus() {
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
        textField5.setText("");
        table1.clearSelection();
        cargarMenus("");
    }

    /**
     * Carga los datos de los empleados en la tabla de empleados.
     * @param filter Término de búsqueda para filtrar por nombre o posición del empleado.
     */
    private void cargarEmpleados(String filter) {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.addColumn("ID");
        model.addColumn("Nombre");
        model.addColumn("Posición");
        model.addColumn("Salario");
        model.addColumn("Teléfono");
        model.addColumn("Email");

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
            if (textSalarioEm.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "El campo Salario de Empleado no puede estar vacío.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }
            double salario = Double.parseDouble(textSalarioEm.getText());
            String telefono = textTelefonoEm.getText();
            String email = textEmailEm.getText();

            if (nombre.isEmpty() || posicion.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos de texto de Empleado.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Employee nuevo = new Employee(nombre, posicion, salario, telefono, email);
            employeeDAO.create(nuevo);
            cargarEmpleados("");
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
            String email = textEmailEm.getText();

            if (nombre.isEmpty() || posicion.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos de texto de Empleado.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Employee emp = new Employee(id, nombre, posicion, salario, telefono, email);
            if (employeeDAO.update(emp)) {
                cargarEmpleados("");
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
                    cargarEmpleados("");
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
        cargarEmpleados("");
    }

    /**
     * Muestra un mensaje de error al usuario y imprime la traza de la pila para depuración.
     * @param e La excepción que ocurrió.
     */
    private void mostrarError(Exception e) {
        e.printStackTrace();
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
        return panel1;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Sistema de Gestión de Cafetería");
            CafeteriaSistema sistema = new CafeteriaSistema();
            frame.setContentPane(sistema.getMainPanel());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    // -------- CÓDIGO DE EMILY PARA ÓRDENES (métodos adaptados) ---------
    private void initTableOrden() {
        tableModel = new DefaultTableModel(new String[]{"ID", "UserID", "Total", "Fecha"}, 0);
        OTblaOrden.setModel(tableModel);
        cargarDatosOrden();
    }

    private void initListenersOrden() {
        ObtnActualizarOrdenes.addActionListener(e -> cargarDatosOrden());

        ObtnGuardar.addActionListener(e -> {
            try {
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
        OtxtFecha.setText(LocalDateTime.now().toString());
        OTblaOrden.clearSelection();
    }

    // -------- NUEVOS MÉTODOS PARA LA SECCIÓN DE VENTAS ---------
    private void initVentasTab() {
        // Rellenar el JComboBox de Estado
        comboVentaEstado.addItem("PENDIENTE");
        comboVentaEstado.addItem("ENVIADO");
        comboVentaEstado.addItem("ENTREGADO");

        // Establecer la fecha actual en el JLabel al iniciar
        labelVentaFecha.setText(LocalDateTime.now().format(formatter));
    }

    /**
     * Carga las ventas en la tabla, opcionalmente filtradas por el nombre del cliente.
     * @param filter El término de búsqueda (nombre del cliente). Vacío para cargar todas.
     */
    private void cargarVentas(String filter) {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Las celdas de la tabla de ventas no son editables
            }
        };

        model.addColumn("ID Venta");
        model.addColumn("ID Producto");
        model.addColumn("Precio Producto");
        model.addColumn("Nombre Cliente");
        model.addColumn("Estado");
        model.addColumn("Fecha");

        try {
            ArrayList<Venta> lista = ventaDAO.search(filter);
            for (Venta venta : lista) {
                model.addRow(new Object[]{
                        venta.getIdVenta(),
                        venta.getNomProducto(),
                        venta.getPrecioProducto(),
                        venta.getNombreCliente(),
                        venta.getEstado().trim(), // Limpiar espacios del nchar(10)
                        venta.getFecha().format(formatter) // Formatear para mostrar
                });
            }
            JTableVentas.setModel(model);
        } catch (SQLException ex) {
            mostrarError(ex);
        }
    }

    /**
     * Crea una nueva venta con los datos de los campos de entrada.
     */
    private void crearVenta() {
        try {
            // Validaciones de campos vacíos
            if (textVentaNomProducto.getText().isEmpty() || textVentaPrecioProducto.getText().isEmpty() ||
                    textVentaNombreCliente.getText().isEmpty() || comboVentaEstado.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos (ID Producto, Precio, Cliente, Estado) para crear la Venta.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Parseo de datos y validación de formato numérico
            int nomProducto = Integer.parseInt(textVentaNomProducto.getText());
            double precioProducto = Double.parseDouble(textVentaPrecioProducto.getText());
            String nombreCliente = textVentaNombreCliente.getText();
            String estado = (String) comboVentaEstado.getSelectedItem();
            LocalDateTime fecha = LocalDateTime.now(); // La fecha se autogenera al crear

            Venta nuevaVenta = new Venta(nomProducto, precioProducto, nombreCliente, estado, fecha);
            ventaDAO.create(nuevaVenta);
            cargarVentas(""); // Recargar tabla
            limpiarCamposVenta(); // Limpiar campos
            JOptionPane.showMessageDialog(null, "Venta creada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Error: 'ID Producto' y 'Precio Producto' deben ser números válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            mostrarError(ex);
        } catch (Exception ex) {
            mostrarError(ex);
        }
    }

    /**
     * Modifica una venta existente con los datos de los campos de entrada.
     */
    private void modificarVenta() {
        int fila = JTableVentas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione una fila de venta para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Validaciones de campos vacíos
            if (textVentaNomProducto.getText().isEmpty() || textVentaPrecioProducto.getText().isEmpty() ||
                    textVentaNombreCliente.getText().isEmpty() || comboVentaEstado.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos (ID Producto, Precio, Cliente, Estado) para actualizar la Venta.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Obtener el ID de la venta de la tabla
            Object idObj = JTableVentas.getValueAt(fila, 0);
            int idVenta;
            if (idObj instanceof Number) {
                idVenta = ((Number) idObj).intValue();
            } else {
                idVenta = Integer.parseInt(idObj.toString());
            }

            // Parseo de datos
            int nomProducto = Integer.parseInt(textVentaNomProducto.getText());
            double precioProducto = Double.parseDouble(textVentaPrecioProducto.getText());
            String nombreCliente = textVentaNombreCliente.getText();
            String estado = (String) comboVentaEstado.getSelectedItem();
            // La fecha se toma del JLabel si ya fue cargada, o se regenera si el campo estaba vacío.
            LocalDateTime fecha = LocalDateTime.parse(labelVentaFecha.getText(), formatter);

            Venta ventaModificada = new Venta(nomProducto, precioProducto, nombreCliente, estado, fecha);
            ventaModificada.setIdVenta(idVenta); // Importante: Establecer el ID para la actualización

            if (ventaDAO.update(ventaModificada)) {
                cargarVentas(""); // Recargar tabla
                limpiarCamposVenta(); // Limpiar campos
                JOptionPane.showMessageDialog(null, "Venta modificada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo modificar la venta. ID no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Error: Los campos numéricos ('ID Producto', 'Precio Producto' o el 'ID Venta' de la tabla) deben ser válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            mostrarError(ex);
        } catch (Exception ex) {
            mostrarError(ex);
        }
    }

    /**
     * Elimina una venta seleccionada de la tabla.
     */
    private void eliminarVenta() {
        int fila = JTableVentas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione una fila de venta para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Obtener el ID de la venta de la tabla
            Object idObj = JTableVentas.getValueAt(fila, 0);
            int idVenta;
            if (idObj instanceof Number) {
                idVenta = ((Number) idObj).intValue();
            } else {
                idVenta = Integer.parseInt(idObj.toString());
            }

            int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar esta venta?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (ventaDAO.delete(idVenta)) {
                    cargarVentas(""); // Recargar tabla
                    limpiarCamposVenta(); // Limpiar campos
                    JOptionPane.showMessageDialog(null, "Venta eliminada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "No se pudo eliminar la venta. ID no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            mostrarError(ex);
        } catch (Exception ex) {
            mostrarError(ex);
        }
    }

    /**
     * Carga los datos de la venta seleccionada de la tabla en los campos de texto.
     */
    private void cargarVentaSeleccionada() {
        int fila = JTableVentas.getSelectedRow();
        if (fila >= 0) {
            // Columna 0 es ID Venta (no se muestra en campo, solo se usa para modificar/eliminar)
            textVentaNomProducto.setText(JTableVentas.getValueAt(fila, 1) != null ? JTableVentas.getValueAt(fila, 1).toString() : "");
            textVentaPrecioProducto.setText(JTableVentas.getValueAt(fila, 2) != null ? JTableVentas.getValueAt(fila, 2).toString() : "");
            textVentaNombreCliente.setText(JTableVentas.getValueAt(fila, 3) != null ? JTableVentas.getValueAt(fila, 3).toString() : "");

            String estadoTabla = JTableVentas.getValueAt(fila, 4) != null ? JTableVentas.getValueAt(fila, 4).toString().trim() : "";
            // Asegurarse de que el elemento exista en el JComboBox antes de seleccionarlo
            boolean foundState = false;
            for (int i = 0; i < comboVentaEstado.getItemCount(); i++) {
                if (comboVentaEstado.getItemAt(i).equalsIgnoreCase(estadoTabla)) {
                    comboVentaEstado.setSelectedIndex(i);
                    foundState = true;
                    break;
                }
            }
            if (!foundState) { // Si no se encuentra el estado, seleccionar el primero o un valor por defecto
                comboVentaEstado.setSelectedIndex(0);
            }

            labelVentaFecha.setText(JTableVentas.getValueAt(fila, 5) != null ? JTableVentas.getValueAt(fila, 5).toString() : "");
        } else {
            // Si no hay fila seleccionada (ej. después de eliminar), limpiar campos
            limpiarCamposVenta();
        }
    }

    /**
     * Limpia todos los campos de entrada y la selección de la tabla de Ventas.
     */
    private void limpiarCamposVenta() {
        textVentaNomProducto.setText("");
        textVentaPrecioProducto.setText("");
        textVentaNombreCliente.setText("");
        comboVentaEstado.setSelectedIndex(0); // Seleccionar el primer elemento (ej. PENDIENTE)
        labelVentaFecha.setText(LocalDateTime.now().format(formatter)); // Resetear a la fecha actual
        textBuscarVenta.setText("");
        JTableVentas.clearSelection();
        cargarVentas(""); // Recargar todos los elementos sin filtro
    }
}