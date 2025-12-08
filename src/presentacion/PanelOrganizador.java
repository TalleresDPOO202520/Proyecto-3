package presentacion;

import javax.swing.*;
import java.awt.*;

public class PanelOrganizador extends JFrame {

    private CardLayout layout;
    private JPanel mainPanel;

    private static final String MENU = "menu";
    private static final String CREAR_VENUE = "crear_venue";
    private static final String CREAR_EVENTO = "crear_evento";
    private static final String AGREGAR_LOCALIDAD = "agregar_localidad";
    private static final String GENERAR_TIQUETES = "generar_tiquetes";
    private static final String LISTAR_EVENTOS = "listar_eventos";

    public PanelOrganizador() {
        setTitle("Panel del Organizador");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        layout = new CardLayout();
        mainPanel = new JPanel(layout);

        mainPanel.add(crearPanelMenu(), MENU);
        mainPanel.add(crearPanelCrearVenue(), CREAR_VENUE);
        mainPanel.add(crearPanelCrearEvento(), CREAR_EVENTO);
        mainPanel.add(crearPanelAgregarLocalidad(), AGREGAR_LOCALIDAD);
        mainPanel.add(crearPanelGenerarTiquetes(), GENERAR_TIQUETES);
        mainPanel.add(crearPanelListarEventos(), LISTAR_EVENTOS);

        add(mainPanel);

        layout.show(mainPanel, MENU);
    }

    private JPanel crearPanelMenu() {
        JPanel p = new JPanel(new GridLayout(6, 1, 10, 10));

        JButton btn1 = new JButton("Crear venue");
        JButton btn2 = new JButton("Crear evento");
        JButton btn3 = new JButton("Agregar localidad a evento");
        JButton btn4 = new JButton("Generar tiquetes");
        JButton btn5 = new JButton("Listar eventos y localidades");

        btn1.addActionListener(e -> layout.show(mainPanel, CREAR_VENUE));
        btn2.addActionListener(e -> layout.show(mainPanel, CREAR_EVENTO));
        btn3.addActionListener(e -> layout.show(mainPanel, AGREGAR_LOCALIDAD));
        btn4.addActionListener(e -> layout.show(mainPanel, GENERAR_TIQUETES));
        btn5.addActionListener(e -> layout.show(mainPanel, LISTAR_EVENTOS));

        p.add(btn1);
        p.add(btn2);
        p.add(btn3);
        p.add(btn4);
        p.add(btn5);

        return p;
    }

    private JPanel crearPanelCrearVenue() {
        JPanel p = new JPanel(new GridLayout(4, 2, 5, 5));

        JTextField txtNombre = new JTextField();
        JTextField txtDireccion = new JTextField();

        JButton btnGuardar = new JButton("Crear");
        JButton btnVolver = new JButton("Volver");

        p.add(new JLabel("Nombre:"));
        p.add(txtNombre);
        p.add(new JLabel("Dirección:"));
        p.add(txtDireccion);
        p.add(btnGuardar);
        p.add(btnVolver);

        btnGuardar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Venue creado.");
        });

        btnVolver.addActionListener(e -> layout.show(mainPanel, MENU));

        return p;
    }

    private JPanel crearPanelCrearEvento() {
        JPanel p = new JPanel(new GridLayout(5, 2, 5, 5));

        JTextField txtNombre = new JTextField();
        JTextField txtFecha = new JTextField();
        JComboBox<String> cbVenue = new JComboBox<>(new String[]{
                "Venue 1", "Venue 2"
        });

        JButton btnCrear = new JButton("Crear evento");
        JButton btnVolver = new JButton("Volver");

        p.add(new JLabel("Nombre del evento:"));
        p.add(txtNombre);
        p.add(new JLabel("Fecha:"));
        p.add(txtFecha);
        p.add(new JLabel("Venue:"));
        p.add(cbVenue);
        p.add(btnCrear);
        p.add(btnVolver);

        btnCrear.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Evento creado.");
        });

        btnVolver.addActionListener(e -> layout.show(mainPanel, MENU));

        return p;
    }

    private JPanel crearPanelAgregarLocalidad() {
        JPanel p = new JPanel(new GridLayout(5, 2, 5, 5));

        JComboBox<String> cbEventos = new JComboBox<>(new String[]{"Evento 1", "Evento 2"});
        JTextField txtNombreLoc = new JTextField();
        JTextField txtCap = new JTextField();

        JButton btnAgregar = new JButton("Agregar");
        JButton btnVolver = new JButton("Volver");

        p.add(new JLabel("Evento:"));
        p.add(cbEventos);
        p.add(new JLabel("Nombre de localidad:"));
        p.add(txtNombreLoc);
        p.add(new JLabel("Capacidad:"));
        p.add(txtCap);
        p.add(btnAgregar);
        p.add(btnVolver);

        btnAgregar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Localidad agregada.");
        });

        btnVolver.addActionListener(e -> layout.show(mainPanel, MENU));

        return p;
    }

    private JPanel crearPanelGenerarTiquetes() {
        JPanel p = new JPanel(new GridLayout(5, 2, 5, 5));

        JComboBox<String> cbEventos = new JComboBox<>(new String[]{"Evento 1", "Evento 2"});
        JComboBox<String> cbLocalidades = new JComboBox<>(new String[]{"VIP", "General"});
        JTextField txtCantidad = new JTextField();

        JButton btnGenerar = new JButton("Generar");
        JButton btnVolver = new JButton("Volver");

        p.add(new JLabel("Evento:"));
        p.add(cbEventos);
        p.add(new JLabel("Localidad:"));
        p.add(cbLocalidades);
        p.add(new JLabel("Cantidad:"));
        p.add(txtCantidad);
        p.add(btnGenerar);
        p.add(btnVolver);

        btnGenerar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Tiquetes generados.");
        });

        btnVolver.addActionListener(e -> layout.show(mainPanel, MENU));

        return p;
    }

    private JPanel crearPanelListarEventos() {
        JPanel p = new JPanel(new BorderLayout());

        JTextArea txt = new JTextArea();
        txt.setEditable(false);

        txt.setText("Evento 1 - Localidades: VIP, General\nEvento 2 - Localidades: Balcón, Palco");

        JButton btnVolver = new JButton("Volver");

        p.add(new JScrollPane(txt), BorderLayout.CENTER);
        p.add(btnVolver, BorderLayout.SOUTH);

        btnVolver.addActionListener(e -> layout.show(mainPanel, MENU));

        return p;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PanelOrganizador().setVisible(true));
    }
}

