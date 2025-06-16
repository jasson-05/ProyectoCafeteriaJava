package esfe.utils; // Manteniendo tu paquete original

import esfe.dominio.Producto;
import esfe.dominio.VentaItem;
import esfe.Persistencia.ProductoDAO; // Necesario para cargar productos en el JComboBox

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// Venta ahora es un JPanel que funcionará como formulario para un VentaItem
public class Venta extends JPanel {
    private JTextField txtIdVentaItem; // Campo para mostrar el ID (no editable)
    private JTextField txtClientName;
    private JTextField txtEstado;
    private JTable tblVentaItems; // Esta tabla mostrará el producto seleccionado para el VentaItem
    private JLabel lblTotal; // Ahora será el precio del "Ítem"
    private DefaultTableModel tableModel;

    // Elementos para seleccionar producto (si se mantiene la funcionalidad de seleccionar aquí)
    private JComboBox<Producto> cmbProductos;
    private JTextField txtCantidad;
    private JButton btnEstablecerProducto; // Cambiado de "Añadir Producto"

    private JButton btnGuardar;   // Antes btnFinalizarVenta
    private JButton btnCancelar;

    private ProductoDAO productoDAO;
    private List<Producto> productosDisponibles;

    /**
     * Constructor para el modo "Crear VentaItem" o "Editar VentaItem".
     */
    public Venta() {
        // No llamamos a super(title) porque ahora es un JPanel
        initComponents();
        setupTable(); // Configura la tabla
        addListeners(); // Añade los listeners de los botones internos

        productoDAO = new ProductoDAO();
        loadProductos(); // Carga productos para el combobox
        setVentaItem(null); // Prepara el formulario para una nueva entrada por defecto
    }

    /**
     * Inicializa y organiza todos los componentes de la interfaz de usuario.
     */
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Panel Superior: Información del VentaItem ---
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ID Venta Item
        gbc.gridx = 0; gbc.gridy = 0;
        topPanel.add(new JLabel("ID Venta Item:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        txtIdVentaItem = new JTextField(10);
        txtIdVentaItem.setEditable(false); // No editable, es el ID
        topPanel.add(txtIdVentaItem, gbc);

        // Nombre del Cliente
        gbc.gridx = 0; gbc.gridy = 1;
        topPanel.add(new JLabel("Nombre del Cliente:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 1.0;
        txtClientName = new JTextField(20);
        topPanel.add(txtClientName, gbc);

        // Estado de la Venta
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0; // Reset weightx
        topPanel.add(new JLabel("Estado de la Venta:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.weightx = 1.0;
        txtEstado = new JTextField(20);
        txtEstado.setText("PENDIENTE"); // Valor predeterminado
        topPanel.add(txtEstado, gbc);

        // Selección de Producto
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.weightx = 0;
        topPanel.add(new JLabel("Producto:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.weightx = 1.0;
        cmbProductos = new JComboBox<>();
        topPanel.add(cmbProductos, gbc);

        // Cantidad del Producto
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.weightx = 0;
        topPanel.add(new JLabel("Cantidad:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        gbc.weightx = 1.0;
        txtCantidad = new JTextField("1", 5); // Cantidad por defecto 1
        topPanel.add(txtCantidad, gbc);

        // Botón para establecer producto y precio
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; // Ocupa dos columnas
        gbc.weightx = 0;
        btnEstablecerProducto = new JButton("Establecer Producto y Precio");
        topPanel.add(btnEstablecerProducto, gbc);
        gbc.gridwidth = 1; // Reset gridwidth

        add(topPanel, BorderLayout.NORTH);

        // --- Panel Central: Tabla de Items de Venta (para mostrar el producto seleccionado) ---
        String[] columnNames = {"ID Producto", "Nombre Producto", "Precio Calculado"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Las celdas de la tabla no son editables
            }
        };
        tblVentaItems = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblVentaItems);
        add(scrollPane, BorderLayout.CENTER);

        // --- Panel Inferior: Total y Botones de Acción ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        lblTotal = new JLabel("Precio Ítem: $0.00");
        lblTotal.setFont(lblTotal.getFont().deriveFont(Font.BOLD, 16f));

        btnGuardar = new JButton("Guardar Venta");     // Renombrado
        btnCancelar = new JButton("Cancelar");

        bottomPanel.add(lblTotal);
        bottomPanel.add(btnGuardar);
        bottomPanel.add(btnCancelar);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Configura el modelo de la tabla y sus propiedades.
     */
    private void setupTable() {
        // La tabla se usa para mostrar el producto actualmente seleccionado para el VentaItem
    }

    /**
     * Añade los listeners a los botones internos del formulario.
     */
    private void addListeners() {
        btnEstablecerProducto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProductAndPriceDisplay();
            }
        });
    }

    /**
     * Carga los productos disponibles desde la base de datos y los añade al JComboBox.
     */
    private void loadProductos() {
        productosDisponibles = productoDAO.getAllProductos();
        if (productosDisponibles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron productos en la base de datos. Por favor, añada productos primero.", "Error de Carga", JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (Producto p : productosDisponibles) {
            cmbProductos.addItem(p);
        }
    }

    /**
     * Actualiza la tabla y el precio total del ítem basado en la selección del producto y la cantidad.
     */
    private void updateProductAndPriceDisplay() {
        Producto selectedProduct = (Producto) cmbProductos.getSelectedItem();
        if (selectedProduct == null) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un producto.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText());
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser un número positivo.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese una cantidad numérica válida.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Limpia la tabla y añade solo el producto seleccionado con el precio calculado
        tableModel.setRowCount(0);
        double precioCalculado = selectedProduct.getPrecio() * cantidad;
        Object[] rowData = {
                selectedProduct.getIdCodpro(),
                selectedProduct.getNompro(),
                String.format("%.2f", precioCalculado)
        };
        tableModel.addRow(rowData);
        lblTotal.setText(String.format("Precio Ítem: $%.2f", precioCalculado));
    }


    /**
     * Carga los datos de un objeto VentaItem en el formulario para su edición.
     * Si ventaItem es null, el formulario se prepara para una nueva entrada.
     * @param ventaItem El objeto VentaItem a cargar.
     */
    public void setVentaItem(VentaItem ventaItem) {
        clearForm(); // Siempre limpia el formulario primero

        if (ventaItem != null) {
            txtIdVentaItem.setText(String.valueOf(ventaItem.getIdventa()));
            txtClientName.setText(ventaItem.getNombreClient());
            txtEstado.setText(ventaItem.getEstado().trim()); // Importante trim() por NCHAR(10)

            // Intentar seleccionar el producto en el ComboBox
            Producto productoSeleccionado = null;
            for (int i = 0; i < cmbProductos.getItemCount(); i++) {
                Producto p = cmbProductos.getItemAt(i);
                if (p.getIdCodpro() == ventaItem.getNomProduct()) {
                    cmbProductos.setSelectedItem(p);
                    productoSeleccionado = p;
                    break;
                }
            }

            // Asumimos cantidad 1 por defecto al cargar para edición
            txtCantidad.setText("1");
            if(productoSeleccionado != null){
                // Intentar recalcular precio si tenemos el producto original
                double precioItemRecalculado = productoSeleccionado.getPrecio() * Double.parseDouble(txtCantidad.getText());
                lblTotal.setText(String.format("Precio Ítem: $%.2f", precioItemRecalculado));
                // Volver a cargar la tabla para reflejar el producto
                tableModel.setRowCount(0);
                Object[] rowData = {
                        productoSeleccionado.getIdCodpro(),
                        productoSeleccionado.getNompro(),
                        String.format("%.2f", precioItemRecalculado)
                };
                tableModel.addRow(rowData);
            } else {
                // Si no encontramos el producto, mostrar el precio tal como está en el VentaItem
                lblTotal.setText(String.format("Precio Ítem: $%.2f", ventaItem.getPrecioProduct()));
                // Y la tabla con lo que sabemos
                tableModel.setRowCount(0);
                Object[] rowData = {
                        ventaItem.getNomProduct(), // Solo el ID del producto
                        "Producto Desconocido", // No podemos saber el nombre sin el objeto Producto
                        String.format("%.2f", ventaItem.getPrecioProduct())
                };
                tableModel.addRow(rowData);
            }

        } else {
            // Para nueva entrada
            txtIdVentaItem.setText("");
            txtClientName.setText("");
            txtEstado.setText("PENDIENTE");
            txtCantidad.setText("1");
            tableModel.setRowCount(0);
            lblTotal.setText("Precio Ítem: $0.00");
            if (cmbProductos.getItemCount() > 0) {
                cmbProductos.setSelectedIndex(0); // Seleccionar el primer producto
            }
        }
    }

    /**
     * Obtiene los datos del formulario y los encapsula en un objeto VentaItem.
     * @return Un objeto VentaItem con los datos del formulario, o null si hay errores de validación.
     */
    public VentaItem getVentaItem() {
        VentaItem item = new VentaItem();
        if (!txtIdVentaItem.getText().isEmpty()) {
            item.setIdventa(Integer.parseInt(txtIdVentaItem.getText()));
        }

        String clientName = txtClientName.getText().trim();
        String estado = txtEstado.getText().trim().toUpperCase(); // Asegurarse de que el estado esté en mayúsculas

        if (clientName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del cliente no puede estar vacío.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        // Validar el estado contra los valores permitidos (opcional pero recomendado)
        if (!estado.equals("PENDIENTE") && !estado.equals("ENVIADO") && !estado.equals("ENTREGADO")) {
            JOptionPane.showMessageDialog(this, "El estado debe ser 'PENDIENTE', 'ENVIADO' o 'ENTREGADO'.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar y establecer un producto para el ítem de venta.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        // Obtener datos del producto mostrado en la tabla (que debería ser el único)
        int idProducto = (int) tableModel.getValueAt(0, 0);
        double precioProducto = Double.parseDouble(tableModel.getValueAt(0, 2).toString()); // Precio ya calculado

        item.setNomProduct(idProducto);
        item.setPrecioProduct(precioProducto);
        item.setNombreClient(clientName);
        item.setEstado(estado);
        item.setFecha(LocalDateTime.now()); // La fecha se establece al momento de guardar/actualizar

        return item;
    }

    /**
     * Limpia todos los campos del formulario.
     */
    public void clearForm() {
        txtIdVentaItem.setText("");
        txtClientName.setText("");
        txtEstado.setText("PENDIENTE");
        txtCantidad.setText("1");
        tableModel.setRowCount(0); // Limpia la tabla
        lblTotal.setText("Precio Ítem: $0.00");
        if (cmbProductos.getItemCount() > 0) {
            cmbProductos.setSelectedIndex(0); // Seleccionar el primer producto
        }
    }

    // --- Getters para los botones para que el panel controlador pueda añadir listeners ---
    public JButton getBtnGuardar() {
        return btnGuardar;
    }

    public JButton getBtnCancelar() {
        return btnCancelar;
    }
}