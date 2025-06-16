package esfe.utils; // Manteniendo tu paquete original

import esfe.dominio.VentaItem;
import esfe.Persistencia.VentaItemDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class VentasPanelCrud extends JPanel { // Renombrado para Claridad
    private JTable ventasTable;
    private DefaultTableModel tableModel;
    private JButton btnNuevo;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnBuscar;
    private JTextField txtBuscar;

    private VentaItemDAO ventaItemDAO;
    private JFrame parentFrame; // Referencia al JFrame principal

    public VentasPanelCrud(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        ventaItemDAO = new VentaItemDAO();
        initComponents();
        setupLayout();
        loadVentaItems(); // Carga los ítems de venta al iniciar el panel
        addListeners();
    }

    private void initComponents() {
        // Configuración de la tabla
        String[] columnNames = {"ID Venta", "ID Producto", "Precio Ítem", "Cliente", "Estado", "Fecha"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Celdas de la tabla no editables
            }
        };
        ventasTable = new JTable(tableModel);
        ventasTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Botones y campo de búsqueda
        btnNuevo = new JButton("Nueva Venta");
        btnEditar = new JButton("Editar Venta");
        btnEliminar = new JButton("Eliminar Venta");
        btnBuscar = new JButton("Buscar por Cliente");
        txtBuscar = new JTextField(20);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Panel de búsqueda
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Buscar por Cliente:"));
        searchPanel.add(txtBuscar);
        searchPanel.add(btnBuscar);
        add(searchPanel, BorderLayout.NORTH);

        // Tabla de ítems de venta
        JScrollPane scrollPane = new JScrollPane(ventasTable);
        add(scrollPane, BorderLayout.CENTER);

        // Panel de botones CRUD
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnNuevo);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnEliminar);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addListeners() {
        btnNuevo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openVentaItemForm(null); // Abre el formulario para una nueva venta
            }
        });

        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = ventasTable.getSelectedRow();
                if (selectedRow != -1) {
                    int idVentaItem = (int) tableModel.getValueAt(selectedRow, 0);
                    try {
                        VentaItem ventaItem = ventaItemDAO.getById(idVentaItem);
                        if (ventaItem != null) {
                            openVentaItemForm(ventaItem); // Abre el formulario para editar
                        } else {
                            JOptionPane.showMessageDialog(VentasPanelCrud.this, "Venta no encontrada en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(VentasPanelCrud.this, "Error al obtener la venta para editar: " + ex.getMessage(), "Error de DB", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(VentasPanelCrud.this, "Por favor, seleccione una venta para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = ventasTable.getSelectedRow();
                if (selectedRow != -1) {
                    int idVentaItem = (int) tableModel.getValueAt(selectedRow, 0);
                    int confirm = JOptionPane.showConfirmDialog(VentasPanelCrud.this,
                            "¿Está seguro de que desea eliminar la venta con ID: " + idVentaItem + "?",
                            "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            if (ventaItemDAO.delete(idVentaItem)) {
                                JOptionPane.showMessageDialog(VentasPanelCrud.this, "Venta eliminada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                                loadVentaItems(); // Recarga la tabla después de eliminar
                            } else {
                                JOptionPane.showMessageDialog(VentasPanelCrud.this, "No se pudo eliminar la venta.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(VentasPanelCrud.this, "Error al eliminar la venta: " + ex.getMessage(), "Error de DB", JOptionPane.ERROR_MESSAGE);
                            ex.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(VentasPanelCrud.this, "Por favor, seleccione una venta para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchVentaItems();
            }
        });

        txtBuscar.addActionListener(new ActionListener() { // Permite buscar con Enter
            @Override
            public void actionPerformed(ActionEvent e) {
                searchVentaItems();
            }
        });
    }

    /**
     * Carga todos los ítems de venta desde la base de datos y los muestra en la tabla.
     */
    public void loadVentaItems() {
        tableModel.setRowCount(0); // Limpia la tabla
        try {
            ArrayList<VentaItem> ventaItems = ventaItemDAO.getAll();
            for (VentaItem item : ventaItems) {
                Object[] rowData = {
                        item.getIdventa(),
                        item.getNomProduct(),
                        String.format("%.2f", item.getPrecioProduct()),
                        item.getNombreClient(),
                        item.getEstado().trim(), // Quitar espacios del NCHAR
                        item.getFecha().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                };
                tableModel.addRow(rowData);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar los ítems de venta: " + ex.getMessage(), "Error de DB", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Realiza una búsqueda de ítems de venta por nombre de cliente y actualiza la tabla.
     */
    private void searchVentaItems() {
        String searchTerm = txtBuscar.getText().trim();
        tableModel.setRowCount(0);

        if (searchTerm.isEmpty()) {
            loadVentaItems();
            return;
        }

        try {
            ArrayList<VentaItem> ventaItems = ventaItemDAO.search(searchTerm);
            if (ventaItems.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontraron ventas para el cliente '" + searchTerm + "'.", "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
            }
            for (VentaItem item : ventaItems) {
                Object[] rowData = {
                        item.getIdventa(),
                        item.getNomProduct(),
                        String.format("%.2f", item.getPrecioProduct()),
                        item.getNombreClient(),
                        item.getEstado().trim(),
                        item.getFecha().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                };
                tableModel.addRow(rowData);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar ítems de venta: " + ex.getMessage(), "Error de DB", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Abre un JDialog para el formulario de Venta (crear o editar).
     * @param ventaItem Si es null, es una nueva venta. Si es un objeto VentaItem, es para editar.
     */
    private void openVentaItemForm(VentaItem ventaItem) {
        JDialog dialog = new JDialog(parentFrame, (ventaItem == null ? "Nueva Venta" : "Editar Venta"), true); // true para modal
        Venta formPanel = new Venta(); // Instancia tu clase Venta como el formulario
        formPanel.setVentaItem(ventaItem); // Carga los datos o limpia para nuevo

        dialog.add(formPanel);
        dialog.pack(); // Ajusta el tamaño del diálogo al contenido
        dialog.setLocationRelativeTo(parentFrame);

        // Acciones de los botones del formulario
        formPanel.getBtnGuardar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentaItem itemToSave = formPanel.getVentaItem();
                if (itemToSave == null) {
                    return; // Hubo un error de validación en el formulario
                }

                try {
                    if (itemToSave.getIdventa() == 0) { // Es una nueva venta (ID 0)
                        VentaItem createdItem = ventaItemDAO.addVentaItem(itemToSave);
                        if (createdItem != null && createdItem.getIdventa() > 0) {
                            JOptionPane.showMessageDialog(dialog, "Venta creada con éxito. ID: " + createdItem.getIdventa(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                            dialog.dispose();
                            loadVentaItems();
                        } else {
                            JOptionPane.showMessageDialog(dialog, "Error al crear la venta.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else { // Es una venta existente (editar)
                        if (ventaItemDAO.update(itemToSave)) {
                            JOptionPane.showMessageDialog(dialog, "Venta actualizada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                            dialog.dispose();
                            loadVentaItems();
                        } else {
                            JOptionPane.showMessageDialog(dialog, "No se pudo actualizar la venta.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(dialog, "Error de base de datos: " + ex.getMessage(), "Error de DB", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        formPanel.getBtnCancelar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        dialog.setVisible(true);
    }
}
