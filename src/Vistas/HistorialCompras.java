package Vistas;

import ModeloDTO.*;
import ModeloDAO.*;

import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class HistorialCompras extends javax.swing.JFrame {

    private ClienteDTO clienteLogueado;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /* ---------- CONSTRUCTORES ---------- */
    public HistorialCompras(ClienteDTO cliente) {
        this.clienteLogueado = cliente;
        initComponents();
        setLocationRelativeTo(null);

        if (clienteLogueado != null) {
            lblTitulo.setText("Historial de Compras de "
                    + clienteLogueado.getNombre() + " "
                    + clienteLogueado.getApellido());
            cargarDatosHistorial();
        } else {
            lblTitulo.setText("Historial de Compras (Cliente no especificado)");
            jTableHistorial.setModel(new DefaultTableModel()); // tabla vacía
        }
    }

    /* Constructor sin cliente (opcional) */
    public HistorialCompras() {
        initComponents();
        setLocationRelativeTo(null);
        lblTitulo.setText("Historial de Compras");
        jTableHistorial.setModel(new DefaultTableModel()); // tabla vacía
    }

    /* ---------- CARGA DATOS ---------- */
    private void cargarDatosHistorial() {

        DefaultTableModel modelo = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        modelo.setColumnIdentifiers(new String[]{
            "ID Venta",
            "Fecha",
            "Documento",
            "Producto",
            "Cantidad",
            "Precio",
            "Subtotal"
        });

        try {

            BoletaDAO boletaDAO = new BoletaDAO();

            ArrayList<Object[]> lista
                    = boletaDAO.obtenerHistorialCompras(
                            clienteLogueado.getIdCliente()
                    );

            for (Object[] fila : lista) {
                modelo.addRow(fila);
            }

            jTableHistorial.setModel(modelo);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error cargando historial: " + e.getMessage()
            );
        }
    }

    /* ---------- UI ---------- */
    @SuppressWarnings("unchecked")
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTableHistorial = new javax.swing.JTable();
        btnVolver = new javax.swing.JButton();
        lblTitulo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTableHistorial.setModel(new DefaultTableModel());
        jScrollPane1.setViewportView(jTableHistorial);

        btnVolver.setText("Volver al Menú");
        btnVolver.addActionListener(evt -> {
            new Menu(clienteLogueado).setVisible(true);
            dispose();
        });

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 18));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("Historial de Compras");

        javax.swing.GroupLayout layout
                = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 680, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnVolver)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(15)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnVolver)
                                        .addComponent(lblTitulo))
                                .addGap(10)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 320,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(20, Short.MAX_VALUE))
        );
        pack();
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new HistorialCompras().setVisible(true));
    }

    private javax.swing.JButton btnVolver;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableHistorial;
    private javax.swing.JLabel lblTitulo;
}
