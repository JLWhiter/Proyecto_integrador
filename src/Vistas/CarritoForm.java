package Vistas;

import Modelo.Cart;
import ModeloDTO.ClienteDTO;
import ModeloDTO.ItemCarritoDTO;
import ModeloDTO.ProductoDTO;
import ModeloDAO.ProductoDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class CarritoForm extends javax.swing.JFrame {

    private ClienteDTO clienteLogueado;

    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnPagar;
    private javax.swing.JButton btnVolver;
    private javax.swing.JLabel jLabelTotal;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;

    public CarritoForm(ClienteDTO cliente) {
        this.clienteLogueado = cliente;
        initComponents();
        setTitle("Carrito de Compras - " + (cliente != null ? cliente.getNombre() : "Invitado"));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                volverAlCatalogo();
            }
        });

        actualizarTabla();
        configurarEventos();
    }

    private void actualizarTabla() {
        String[] columnas = {"ID Producto", "Nombre", "P. Unitario", "Cantidad", "Subtotal"};

        DefaultTableModel model = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<ItemCarritoDTO> itemsCarrito = Cart.getItems();

        for (ItemCarritoDTO item : itemsCarrito) {
            ProductoDTO p = item.getProducto();

            Object[] fila = {
                p.getIdProducto(),
                p.getNombre(),
                String.format("S/ %.2f", p.getPrecio()),
                item.getCantidad(),
                String.format("S/ %.2f", item.getSubtotal())
            };

            model.addRow(fila);
        }

        jTable1.setModel(model);
        jLabelTotal.setText("Total del Carrito: " + String.format("S/ %.2f", Cart.getTotal()));
    }

    private void configurarEventos() {
        btnEliminar.addActionListener(e -> eliminarProducto());
        btnPagar.addActionListener(e -> realizarCompra());
        btnVolver.addActionListener(e -> volverAlCatalogo());
    }

    private void eliminarProducto() {
        int fila = jTable1.getSelectedRow();

        if (fila != -1) {
            Cart.removeProducto(fila);
            JOptionPane.showMessageDialog(this, "Producto eliminado del carrito.");
            actualizarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void realizarCompra() {
        if (Cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El carrito está vacío.");
            return;
        }

        if (clienteLogueado == null) {
            JOptionPane.showMessageDialog(this, "Debe iniciar sesión para realizar la compra.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Desea continuar con el pago por " + String.format("S/ %.2f", Cart.getTotal()) + "?",
                "Confirmar compra",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            ProductoDAO productoDAO = new ProductoDAO();

            for (ItemCarritoDTO item : Cart.getItems()) {
                ProductoDTO productoDB = productoDAO.buscarPorId(item.getProducto().getIdProducto());

                if (productoDB == null) {
                    JOptionPane.showMessageDialog(this, "El producto no existe: " + item.getProducto().getNombre());
                    return;
                }

                if (productoDB.getStock() < item.getCantidad()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Stock insuficiente de: " + item.getProducto().getNombre()
                    );
                    return;
                }
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Stock validado correctamente. Complete los datos del comprobante."
            );

            irBoletaFactura();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Error al validar la compra: " + e.getMessage()
            );
        }
    }

    private void volverAlCatalogo() {
        this.dispose();
        new CatalogoForm(clienteLogueado).setVisible(true);
    }

    private void irBoletaFactura() {
        this.dispose();
        new BoletaForm(clienteLogueado).setVisible(true);
    }

    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnEliminar = new javax.swing.JButton();
        btnPagar = new javax.swing.JButton();
        btnVolver = new javax.swing.JButton();
        jLabelTotal = new javax.swing.JLabel();
        lblTitulo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Carrito de Compras");

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new java.awt.Color(230, 240, 250));
        mainPanel.setLayout(new GroupLayout(mainPanel));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{"ID Producto", "Nombre", "P. Unitario", "Cantidad", "Subtotal"}
        ));

        jScrollPane1.setViewportView(jTable1);

        lblTitulo.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("Tu Carrito de Compras");

        btnEliminar.setText("Eliminar Seleccionado");
        btnPagar.setText("Realizar Compra");
        btnVolver.setText("Volver al Catálogo");

        jLabelTotal.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 16));
        jLabelTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelTotal.setText("Total del Carrito: S/ 0.00");

        GroupLayout mainPanelLayout = (GroupLayout) mainPanel.getLayout();

        mainPanelLayout.setHorizontalGroup(
                mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(mainPanelLayout.createSequentialGroup()
                                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(btnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                                                        .addComponent(btnPagar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(btnVolver, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabelTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGap(18, 18, 18)
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)))
                                .addContainerGap())
        );

        mainPanelLayout.setVerticalGroup(
                mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblTitulo)
                                .addGap(18, 18, 18)
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(mainPanelLayout.createSequentialGroup()
                                                .addComponent(jLabelTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(25, 25, 25)
                                                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnPagar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnVolver, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE))
                                .addContainerGap())
        );

        getContentPane().setLayout(new java.awt.BorderLayout());
        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        pack();
    }
}