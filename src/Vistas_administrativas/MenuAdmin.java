package Vistas_administrativas;

import Vistas.login;
import java.awt.*;
import javax.swing.*;

public class MenuAdmin extends JFrame {
    private String nombreAdmin;

    public MenuAdmin() {
        this("Administrador");
    }

    public MenuAdmin(String nombreAdmin) {
        this.nombreAdmin = nombreAdmin;
        initComponents();
    }

    private void initComponents() {
        AdminTheme.prepararFrame(this, "Panel Administrativo", 960, 580);
        JPanel root = AdminTheme.fondo();
        root.setLayout(new BorderLayout(18, 18));

        JPanel header = AdminTheme.card();
        header.setLayout(new BorderLayout(18, 8));

        JPanel textos = new JPanel(new GridLayout(2,1));
        textos.setBackground(Color.WHITE);
        textos.add(AdminTheme.titulo("Panel administrativo"));
        textos.add(AdminTheme.subtitulo("Bienvenido, " + nombreAdmin + " | Control general del sistema"));
        header.add(textos, BorderLayout.CENTER);

        JLabel etiqueta = new JLabel("ADMIN", SwingConstants.CENTER);
        etiqueta.setFont(AdminTheme.bold(14));
        etiqueta.setForeground(Color.WHITE);
        etiqueta.setOpaque(true);
        etiqueta.setBackground(AdminTheme.PRIMARIO);
        etiqueta.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        header.add(etiqueta, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);

        JPanel opciones = AdminTheme.card();
        opciones.setLayout(new GridLayout(1, 2, 18, 18));
        JButton historial = opcion("Historial total", "Ver compras de todos los clientes", AdminTheme.PRIMARIO);
        JButton productos = opcion("Productos", "Agregar, actualizar precios y stock", AdminTheme.ACENTO);
        opciones.add(historial);
        opciones.add(productos);
        root.add(opciones, BorderLayout.CENTER);

        JPanel footer = AdminTheme.card();
        footer.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton cerrarSesion = AdminTheme.botonRojo("Cerrar sesión");
        footer.add(cerrarSesion);
        root.add(footer, BorderLayout.SOUTH);

        historial.addActionListener(e -> new HistorialGeneralAdmin().setVisible(true));
        productos.addActionListener(e -> new GestionProductosAdmin().setVisible(true));
        cerrarSesion.addActionListener(e -> {
            new login().setVisible(true);
            dispose();
        });

        setContentPane(root);
    }

    private JButton opcion(String titulo, String desc, Color color) {
        JButton b = AdminTheme.boton("<html><div style='text-align:left'><b style='font-size:18px'>" + titulo + "</b><br><span style='font-size:11px'>" + desc + "</span></div></html>", color);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        return b;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuAdmin().setVisible(true));
    }
}
