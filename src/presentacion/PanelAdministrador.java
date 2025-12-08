package presentacion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import usuarios.Administrador;
import eventos.Evento;
import eventos.Venue;
import tiquetes.Tiquete;

@SuppressWarnings("serial")
public class PanelAdministrador extends JPanel {

    private FPrincipal ventanaPrincipal;
    private Administrador admin;

    private JTextField txtTipoEvento;
    private JTextField txtPorcentaje;
    private JTextField txtCuota;
    private JTextField txtVenue;
    private JTextField txtEventoCancelar;
    private JTextArea areaLog;

    public PanelAdministrador(FPrincipal principal, Administrador admin) {
        this.ventanaPrincipal = principal;
        this.admin = admin;

        setLayout(new BorderLayout());
        setBackground(new Color(30,30,30));

        inicializarUI();
    }

    private void inicializarUI() {

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(10,10,10,10));

        JLabel titulo = new JLabel("Panel del Administrador");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        titulo.setForeground(Color.WHITE);

        header.add(titulo, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(15,15,15,15));

        body.add(crearSeccionCargos());
        body.add(Box.createRigidArea(new Dimension(0,15)));

        body.add(crearSeccionCuota());
        body.add(Box.createRigidArea(new Dimension(0,15)));

        body.add(crearSeccionVenue());
        body.add(Box.createRigidArea(new Dimension(0,15)));

        body.add(crearSeccionCancelar());
        body.add(Box.createRigidArea(new Dimension(0,15)));

        body.add(crearSeccionFinanzas());

        JScrollPane scroll = new JScrollPane(body);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);

        add(scroll, BorderLayout.CENTER);

        areaLog = new JTextArea(7,40);
        areaLog.setBackground(new Color(20,20,20));
        areaLog.setForeground(Color.GREEN);
        areaLog.setEditable(false);
        areaLog.setFont(new Font("Consolas", Font.PLAIN, 14));

        add(new JScrollPane(areaLog), BorderLayout.SOUTH);
    }

    private JPanel crearSeccionCargos() {
        JPanel p = crearPanelSeccion("Ajustar Cargo por Tipo de Evento");

        txtTipoEvento = new JTextField(10);
        txtPorcentaje = new JTextField(10);

        JButton btn = new JButton("Aplicar");
        btn.addActionListener(this::accionFijarCargo);

        p.add(new JLabel("Tipo evento:"));
        p.add(txtTipoEvento);

        p.add(new JLabel("Porcentaje (0 - 1):"));
        p.add(txtPorcentaje);

        p.add(btn);

        return p;
    }

    private JPanel crearSeccionCuota() {
        JPanel p = crearPanelSeccion("Fijar Cuota de Emisión");

        txtCuota = new JTextField(10);

        JButton btn = new JButton("Aplicar");
        btn.addActionListener(this::accionFijarCuota);

        p.add(new JLabel("Cuota fija:"));
        p.add(txtCuota);
        p.add(btn);

        return p;
    }

    private JPanel crearSeccionVenue() {
        JPanel p = crearPanelSeccion("Aprobar un Venue");

        txtVenue = new JTextField(10);

        JButton btn = new JButton("Aprobar");
        btn.addActionListener(this::accionAprobarVenue);

        p.add(new JLabel("Nombre Venue:"));
        p.add(txtVenue);
        p.add(btn);

        return p;
    }

    private JPanel crearSeccionCancelar() {
        JPanel p = crearPanelSeccion("Cancelar un Evento");

        txtEventoCancelar = new JTextField(10);

        JButton btn = new JButton("Cancelar Evento");
        btn.addActionListener(this::accionCancelarEvento);

        p.add(new JLabel("Nombre evento:"));
        p.add(txtEventoCancelar);
        p.add(btn);

        return p;
    }

    private JPanel crearSeccionFinanzas() {
        JPanel p = crearPanelSeccion("Consultar Finanzas");

        JButton btn = new JButton("Consultar");
        btn.addActionListener(e -> {
            admin.consultarFinanzas("todos");
            areaLog.append("📊 Consultadas finanzas.\n");
        });

        p.add(btn);
        return p;
    }

    private JPanel crearPanelSeccion(String titulo) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                titulo,
                0, 0,
                new Font("SansSerif", Font.BOLD, 14),
                Color.WHITE));

        p.setLayout(new FlowLayout(FlowLayout.LEFT));
        return p;
    }


    private void accionFijarCargo(ActionEvent e) {
        try {
            String tipo = txtTipoEvento.getText().trim();
            double porc = Double.parseDouble(txtPorcentaje.getText().trim());

            admin.fijarCargoServicio(tipo, porc);

            areaLog.append("✔ Cargo aplicado: " + tipo + " = " + porc + "\n");
        } catch (Exception ex) {
            areaLog.append("❌ Error al fijar cargo.\n");
        }
    }

    private void accionFijarCuota(ActionEvent e) {
        try {
            double monto = Double.parseDouble(txtCuota.getText());
            admin.fijarCuotaEmision(monto);

            areaLog.append("✔ Cuota fijada: " + monto + "\n");
        } catch (Exception ex) {
            areaLog.append("❌ Error al fijar cuota.\n");
        }
    }

    private void accionAprobarVenue(ActionEvent e) {
        String nombre = txtVenue.getText().trim();

        if (nombre.isEmpty()) {
            areaLog.append("⚠ Ingrese un nombre de venue.\n");
            return;
        }

        Venue v = new Venue("VEN-" + System.currentTimeMillis(), nombre, "Dir X", 2000);
        admin.aprobarVenue(v);

        areaLog.append("✔ Venue aprobado: " + nombre + "\n");
    }

    private void accionCancelarEvento(ActionEvent e) {
        String nombre = txtEventoCancelar.getText().trim();

        if (nombre.isEmpty()) {
            areaLog.append("⚠ Escriba el nombre del evento.\n");
            return;
        }

        Evento ev = new Evento("EV-" + System.currentTimeMillis(), nombre, "Concierto", "Fecha x", "20:00", null);
        admin.cancelarEvento(ev, "Decisión administrativa");

        areaLog.append("⚠ Evento cancelado: " + nombre + "\n");
    }
}