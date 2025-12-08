package presentacion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import usuarios.Organizador;
import eventos.Evento;
import eventos.Venue;
import eventos.Localidad;

@SuppressWarnings("serial")
public class PanelOrganizador extends JPanel {

    @SuppressWarnings("unused")
	private FPrincipal ventanaPrincipal;
    private Organizador organizadorActual;

    private Map<String, Venue> venuesRegistrados = new HashMap<>();
    private Map<String, Evento> eventosRegistrados = new HashMap<>();

    private JTabbedPane pestañasInternas;

    // Campos Venue
    private JTextField txtVenueId, txtVenueNombre, txtVenueUbicacion, txtVenueCapacidad;

    // Campos Evento
    private JTextField txtEventoId, txtEventoNombre, txtEventoTipo, txtEventoFecha, txtEventoHora;
    private JComboBox<String> cmbVenuesDisponibles;

    // Campos Localidad
    private JComboBox<String> cmbEventosDisponibles;
    private JTextField txtLocNombre, txtLocPrecio, txtLocCapacidad;
    private JCheckBox chkLocNumerada;

    // Finanzas
    private JLabel lblIngresos, lblGastos, lblSaldo;

    public PanelOrganizador() {
        this(null, new Organizador("OrganizadorDefault", "123"));
    }

    public PanelOrganizador(FPrincipal principal, Object usuario) {
        this.ventanaPrincipal = principal;

        if (usuario instanceof Organizador) {
            this.organizadorActual = (Organizador) usuario;
        } else {
            this.organizadorActual = new Organizador("Invitado", "0000");
        }

        setLayout(new BorderLayout());
        setBackground(Color.magenta);

   
        inicializarComponentes();

        cargarDatosMock();   
    }

    private void inicializarComponentes() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(40, 40, 40));
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTitulo = new JLabel("Panel de Gestión - " + organizadorActual.getLogin());
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        header.add(lblTitulo, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);

        pestañasInternas = new JTabbedPane();
        pestañasInternas.setFont(new Font("SansSerif", Font.PLAIN, 14));

        pestañasInternas.addTab("📍 Crear Venue", crearFormularioVenue());
        pestañasInternas.addTab("📅 Crear Evento", crearFormularioEvento());
        pestañasInternas.addTab("🎟️ Añadir Localidad", crearFormularioLocalidad());
        pestañasInternas.addTab("💰 Finanzas", crearPanelFinanzas());

        add(pestañasInternas, BorderLayout.CENTER);
    }

 
    private JScrollPane crearFormularioVenue() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtVenueId = new JTextField(15);
        txtVenueNombre = new JTextField(15);
        txtVenueUbicacion = new JTextField(15);
        txtVenueCapacidad = new JTextField(15);

        JButton btnGuardar = new JButton("Guardar Venue");
        btnGuardar.setBackground(new Color(0, 122, 204));
        btnGuardar.setForeground(Color.black);

        agregarCampo(panel, "ID Venue:", txtVenueId, 0, gbc);
        agregarCampo(panel, "Nombre:", txtVenueNombre, 1, gbc);
        agregarCampo(panel, "Ubicación:", txtVenueUbicacion, 2, gbc);
        agregarCampo(panel, "Capacidad Máx:", txtVenueCapacidad, 3, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(btnGuardar, gbc);

        btnGuardar.addActionListener(e -> {
            try {
                String id = txtVenueId.getText();
                String nom = txtVenueNombre.getText();
                String ubi = txtVenueUbicacion.getText();
                int cap = Integer.parseInt(txtVenueCapacidad.getText());

                Venue nuevoVenue = new Venue(id, nom, ubi, cap);
                venuesRegistrados.put(id, nuevoVenue);

                actualizarCombos();
                JOptionPane.showMessageDialog(this, "Venue '" + nom + "' creado exitosamente.");
                limpiarCampos(txtVenueId, txtVenueNombre, txtVenueUbicacion, txtVenueCapacidad);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "La capacidad debe ser un número.");
            }
        });

        return new JScrollPane(panel);
    }

    private JScrollPane crearFormularioEvento() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtEventoId = new JTextField(15);
        txtEventoNombre = new JTextField(15);
        txtEventoTipo = new JTextField(15);
        txtEventoFecha = new JTextField(15);
        txtEventoHora = new JTextField(15);
        cmbVenuesDisponibles = new JComboBox<>();

        JButton btnCrear = new JButton("Publicar Evento");
        btnCrear.setBackground(new Color(34, 139, 34));
        btnCrear.setForeground(Color.WHITE);

        agregarCampo(panel, "ID Evento:", txtEventoId, 0, gbc);
        agregarCampo(panel, "Nombre:", txtEventoNombre, 1, gbc);
        agregarCampo(panel, "Tipo:", txtEventoTipo, 2, gbc);
        agregarCampo(panel, "Fecha:", txtEventoFecha, 3, gbc);
        agregarCampo(panel, "Hora:", txtEventoHora, 4, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Seleccionar Venue:"), gbc);

        gbc.gridx = 1;
        panel.add(cmbVenuesDisponibles, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        panel.add(btnCrear, gbc);

        btnCrear.addActionListener(e -> {
            try {
                String idVenue = (String) cmbVenuesDisponibles.getSelectedItem();
                if (idVenue == null) throw new Exception("Debes crear un Venue primero.");

                Venue v = venuesRegistrados.get(idVenue);

                Evento nuevoEvento = new Evento(
                        txtEventoId.getText(),
                        txtEventoNombre.getText(),
                        txtEventoTipo.getText(),
                        txtEventoFecha.getText(),
                        txtEventoHora.getText(),
                        v
                );

                eventosRegistrados.put(nuevoEvento.getIdEvento(), nuevoEvento);
                actualizarCombos();

                JOptionPane.showMessageDialog(this, "Evento creado!");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        return new JScrollPane(panel);
    }


    private JScrollPane crearFormularioLocalidad() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cmbEventosDisponibles = new JComboBox<>();
        txtLocNombre = new JTextField(15);
        txtLocPrecio = new JTextField(15);
        txtLocCapacidad = new JTextField(15);

        chkLocNumerada = new JCheckBox("Es Numerada");
        chkLocNumerada.setBackground(Color.WHITE);

        JButton btnAdd = new JButton("Añadir Localidad");
        btnAdd.setBackground(new Color(255, 140, 0));
        btnAdd.setForeground(Color.black);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Seleccionar Evento:"), gbc);

        gbc.gridx = 1;
        panel.add(cmbEventosDisponibles, gbc);

        agregarCampo(panel, "Nombre Localidad:", txtLocNombre, 1, gbc);
        agregarCampo(panel, "Precio:", txtLocPrecio, 2, gbc);
        agregarCampo(panel, "Capacidad:", txtLocCapacidad, 3, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(chkLocNumerada, gbc);

        gbc.gridy = 5;
        panel.add(btnAdd, gbc);

        btnAdd.addActionListener(e -> {
            try {
                String idEvento = (String) cmbEventosDisponibles.getSelectedItem();
                if (idEvento == null) throw new Exception("Debes seleccionar evento.");

                Evento ev = eventosRegistrados.get(idEvento);

                Localidad loc = new Localidad(
                        "LOC-" + System.currentTimeMillis(),
                        txtLocNombre.getText(),
                        Double.parseDouble(txtLocPrecio.getText()),
                        chkLocNumerada.isSelected(),
                        Integer.parseInt(txtLocCapacidad.getText())
                );

                ev.agregarLocalidad(loc);

                JOptionPane.showMessageDialog(this,
                        "Localidad agregada. Tiquetes generados: " +
                                loc.generarTiquetes(ev).size());

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        return new JScrollPane(panel);
    }

 
    private JPanel crearPanelFinanzas() {
        
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));
        panel.setBackground(Color.WHITE);

        
        JPanel panelInfo = new JPanel(new GridLayout(3, 1, 10, 10));
        panelInfo.setOpaque(false);

        lblIngresos = new JLabel("Ingresos Totales: $ 0.0");
        lblGastos = new JLabel("Gastos Totales: $ 0.0");
        lblSaldo = new JLabel("Saldo en Plataforma: $ 0.0");
        
        
        lblIngresos.setForeground(new Color(0, 128, 0)); // Verde
        lblIngresos.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblGastos.setForeground(Color.RED);
        lblSaldo.setFont(new Font("SansSerif", Font.BOLD, 18));

        panelInfo.add(lblIngresos);
        panelInfo.add(lblGastos);
        panelInfo.add(lblSaldo);
        
        panel.add(panelInfo, BorderLayout.NORTH);

        PanelGrafica panelGrafica = new PanelGrafica();
       
        JScrollPane scrollGrafica = new JScrollPane(panelGrafica);
        scrollGrafica.setBorder(BorderFactory.createTitledBorder("Actividad Reciente"));
        
        panel.add(scrollGrafica, BorderLayout.CENTER);

    
        JButton btnActualizar = new JButton("Actualizar Reporte");
        btnActualizar.addActionListener(e -> {
            actualizarFinanzas();
            panelGrafica.repaint(); 
        });

        panel.add(btnActualizar, BorderLayout.SOUTH);

        return panel;
    }

    private void agregarCampo(JPanel p, String label, JComponent campo, int y, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = y;
        p.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        p.add(campo, gbc);
    }

    private void actualizarCombos() {
        if (cmbVenuesDisponibles != null) {
            cmbVenuesDisponibles.removeAllItems();
            for (String id : venuesRegistrados.keySet()) {
                cmbVenuesDisponibles.addItem(id);
            }
        }

        if (cmbEventosDisponibles != null) {
            cmbEventosDisponibles.removeAllItems();
            for (String id : eventosRegistrados.keySet()) {
                cmbEventosDisponibles.addItem(id);
            }
        }
    }

    private void actualizarFinanzas() {
        lblIngresos.setText("Ingresos Totales: $ " + organizadorActual.getIngresosTotales());
        lblGastos.setText("Gastos Totales: $ " + organizadorActual.getGastosTotales());
        lblSaldo.setText("Saldo en Plataforma: $ " + organizadorActual.getSaldoPlataforma());
    }

    private void limpiarCampos(JTextField... campos) {
        for (JTextField c : campos) c.setText("");
    }

    private void cargarDatosMock() {
        Venue v = new Venue("V1", "Movistar Arena", "Bogotá", 14000);
        venuesRegistrados.put("V1", v);

        Evento e = new Evento("E1", "Concierto Mock", "Musical", "2026-01-01", "20:00", v);
        eventosRegistrados.put("E1", e);

        actualizarCombos();
    }
}
