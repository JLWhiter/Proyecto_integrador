package Vistas;

import Modelo.Cart;
import ModeloDTO.ClienteDTO;
import ModeloDTO.ProductoDTO;
import ModeloDAO.ProductoDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CatalogoForm extends javax.swing.JFrame {

    private List<ProductoDTO> productosCatalogo;
    private ClienteDTO clienteLogueado;
    private javax.swing.JButton btnAgregarCarrito;
    private javax.swing.JButton btnVerCarrito;
    private javax.swing.JButton btnVerHistorial;
    private javax.swing.JButton btnVolverMenu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;

    public CatalogoForm(ClienteDTO cliente) {
        this.clienteLogueado = cliente;
        initComponents();
        setTitle("Catálogo de Productos - " + (cliente != null ? cliente.getNombre() : "Invitado"));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        
        cargarProductosDesdeBD();
        llenarTablaCatalogo();
        configurarBotones();
    }

    public CatalogoForm() {
        this(null);
        setTitle("Catálogo de Productos (Modo Invitado)");
    }

    private void configurarBotones() {
        btnAgregarCarrito.addActionListener(e -> agregarProductoAlCarrito());
        btnVerCarrito.addActionListener(e -> abrirCarrito());
        btnVerHistorial.addActionListener(e -> abrirHistorial());
        btnVolverMenu.addActionListener(e -> volverAlMenu());
        
        if (clienteLogueado == null) {
            btnAgregarCarrito.setEnabled(false);
            btnVerHistorial.setEnabled(false);
        }
    }

    private void cargarProductosDesdeBD() {
        ProductoDAO productoDAO = new ProductoDAO();
        productosCatalogo = productoDAO.listarTodo(); 
        if (productosCatalogo == null || productosCatalogo.isEmpty()) { 
            JOptionPane.showMessageDialog(this, "No se encontraron productos en la base de datos.", 
                                         "Advertencia", JOptionPane.WARNING_MESSAGE);
            productosCatalogo = new ArrayList<>();
        }
    }

    private void llenarTablaCatalogo() {
        String[] columnas = {"Código", "Nombre", "Precio", "Stock"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3) return Integer.class; // Stock
                return Object.class;
            }
        };

        for (ProductoDTO p : productosCatalogo) {
            Object[] fila = {
                p.getIdProducto(),
                p.getNombre(),
                String.format("$%.2f", p.getPrecio()),
                p.getStock()
            };
            model.addRow(fila);
        }

        jTable1.setModel(model);
        
        // Ajustar el ancho de las columnas
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(80);  // Código
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(200); // Nombre
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(80);  // Precio
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(50);  // Stock
    }

    private void agregarProductoAlCarrito() {
    int filaSeleccionada = jTable1.getSelectedRow();
    if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(this, "Seleccione un producto para agregar.",
                                     "Advertencia", JOptionPane.WARNING_MESSAGE);
        return;
    }

    ProductoDTO productoSeleccionado = productosCatalogo.get(filaSeleccionada);

    if (productoSeleccionado.getStock() <= 0) {
        JOptionPane.showMessageDialog(this, "Producto agotado: " + productoSeleccionado.getNombre(),
                                     "Stock Insuficiente", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // ✅ Agregar el producto al carrito con cantidad 1
    Cart.addProducto(productoSeleccionado, 1);

    // ✅ Mostrar mensaje al usuario
    JOptionPane.showMessageDialog(this, "Producto agregado al carrito: " + productoSeleccionado.getNombre(),
                                 "Producto Agregado", JOptionPane.INFORMATION_MESSAGE);

    // ✅ Actualizar stock localmente
    productoSeleccionado.setStock(productoSeleccionado.getStock() - 1);
    llenarTablaCatalogo();
}



    private void abrirCarrito() {
        // CORRECCIÓN: Pasa clienteLogueado (puede ser null) en lugar de usar constructor vacío
        new CarritoForm(clienteLogueado).setVisible(true);
        this.dispose();
    }

    private void abrirHistorial() {
        if (clienteLogueado != null) {
            new HistorialCompras(clienteLogueado).setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Debe iniciar sesión para ver el historial.", 
                                         "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void volverAlMenu() {
        new Menu(clienteLogueado).setVisible(true);
        this.dispose();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btnAgregarCarrito = new javax.swing.JButton();
        btnVerCarrito = new javax.swing.JButton();
        btnVerHistorial = new javax.swing.JButton();
        btnVolverMenu = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Catálogo de Productos");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Catálogo de Productos");

        btnAgregarCarrito.setText("Agregar al Carrito");

        btnVerCarrito.setText("Ir al Carrito");

        btnVerHistorial.setText("Ver Historial");

        btnVolverMenu.setText("Volver al Menú");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnAgregarCarrito)
                        .addGap(18, 18, 18)
                        .addComponent(btnVerCarrito)
                        .addGap(18, 18, 18)
                        .addComponent(btnVerHistorial)
                        .addGap(18, 18, 18)
                        .addComponent(btnVolverMenu)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregarCarrito)
                    .addComponent(btnVerCarrito)
                    .addComponent(btnVerHistorial)
                    .addComponent(btnVolverMenu))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CatalogoForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new CatalogoForm().setVisible(true);
        });
    }
}