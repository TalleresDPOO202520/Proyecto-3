package presentacion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

import usuarios.Administrador;
import eventos.Evento;
import eventos.Venue;
import tiquetes.Tiquete;

@SuppressWarnings({ "serial", "unused" })
public class PanelAdministrador extends JPanel {

    private FPrincipal ventanaPrincipal;
    private Administrador adminActual;

    private JTextField txtTipoEvento, txtPorcentaje;
    private JTextField txtCuota;
    private JTextField txtVenueNombre, txtVenueCapacidad;
    private JTextField txtEventoCancelar, txtMotivoCancelacion;
    private JTextArea areaLog;

    public PanelAdministrador() {
        this(null, new Administrador("AdminDefault", "123", "ADM-00", "Default"));
    }

    public PanelAdministrador(FPrincipal principal, Object usuario) {
        this.ventanaPrincipal = principal;
        
        if (usuario instanceof Administrador) {
            this.adminActual = (Administrador) usuario;
        } else {
   
            this.adminActual = new Administrador("Invitado", "000", "ADM-INV", "Invitado");
        }

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245)); 

        inicializarUI();
    }

    private void inicializarUI() {
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(30, 30, 30)); 
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titulo = new JLabel("Panel de Administración - " + adminActual.getNombre());
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setForeground(Color.WHITE);

        header.add(titulo, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(20, 20, 20, 20));
        body.setBackground(Color.WHITE);

        body.add(crearSeccionConfiguracion());
        body.add(Box.createRigidArea(new Dimension(0, 20)));
        
        body.add(crearSeccionAprobaciones());
        body.add(Box.createRigidArea(new Dimension(0, 20)));
        
        body.add(crearSeccionControlEventos());
        body.add(Box.createRigidArea(new Dimension(0, 20)));
        
        body.add(crearSeccionAuditoria());

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);
    }

 
    private JPanel crearSeccionConfiguracion() {
        JPanel p = crearPanelBase("Configuración Financiera");
        p.setLayout(new GridLayout(2, 1, 10, 10));

        JPanel pCargos = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pCargos.setOpaque(false);
        txtTipoEvento = new JTextField(10);
        txtPorcentaje = new JTextField(5);
        JButton btnCargo = new JButton("Fijar % Servicio");
        btnCargo.addActionListener(this::accionFijarCargo);
        
        pCargos.add(new JLabel("Tipo Evento:")); pCargos.add(txtTipoEvento);
        pCargos.add(new JLabel("% (0.0-1.0):")); pCargos.add(txtPorcentaje);
        pCargos.add(btnCargo);

        JPanel pCuota = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pCuota.setOpaque(false);
        txtCuota = new JTextField(10);
        JButton btnCuota = new JButton("Fijar Cuota Emisión");
        btnCuota.addActionListener(this::accionFijarCuota);
        
        pCuota.add(new JLabel("Cuota Fija ($):")); pCuota.add(txtCuota);
        pCuota.add(btnCuota);

        p.add(pCargos);
        p.add(pCuota);
        return p;
    }

   
    private JPanel crearSeccionAprobaciones() {
        JPanel p = crearPanelBase("Aprobación de Venues");
        p.setLayout(new FlowLayout(FlowLayout.LEFT));

        txtVenueNombre = new JTextField(15);
        txtVenueCapacidad = new JTextField(8);
        JButton btnAprobar = new JButton("Aprobar Venue");
        btnAprobar.setBackground(new Color(34, 139, 34)); // Verde
        btnAprobar.setForeground(Color.WHITE);
        btnAprobar.addActionListener(this::accionAprobarVenue);

        p.add(new JLabel("Nombre Venue:")); p.add(txtVenueNombre);
        p.add(new JLabel("Capacidad:")); p.add(txtVenueCapacidad);
        p.add(btnAprobar);

        return p;
    }

  
    private JPanel crearSeccionControlEventos() {
        JPanel p = crearPanelBase("Gestión de Riesgos");
        p.setLayout(new FlowLayout(FlowLayout.LEFT));

        txtEventoCancelar = new JTextField(15);
        txtMotivoCancelacion = new JTextField(20);
        JButton btnCancelar = new JButton("CANCELAR EVENTO");
        btnCancelar.setBackground(Color.RED);
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.addActionListener(this::accionCancelarEvento);

        p.add(new JLabel("ID/Nombre Evento:")); p.add(txtEventoCancelar);
        p.add(new JLabel("Motivo:")); p.add(txtMotivoCancelacion);
        p.add(btnCancelar);

        return p;
    }

    
    private JPanel crearSeccionAuditoria() {
        JPanel p = crearPanelBase("Log de Auditoría");
        p.setLayout(new BorderLayout());

        areaLog = new JTextArea(8, 40);
        areaLog.setEditable(false);
        areaLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaLog.setBackground(new Color(240, 240, 240));
        
        // Botón para consultar finanzas globales
        JButton btnFinanzas = new JButton("Consultar Reporte Financiero Global");
        btnFinanzas.addActionListener(e -> {
            adminActual.consultarFinanzas("Global");
            log("Reporte financiero generado en consola/log.");
        });

        p.add(new JScrollPane(areaLog), BorderLayout.CENTER);
        p.add(btnFinanzas, BorderLayout.SOUTH);
        
        return p;
    }

   
    private JPanel crearPanelBase(String titulo) {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), 
                titulo, 
                0, 0, 
                new Font("SansSerif", Font.BOLD, 14), 
                Color.BLACK));
        return p;
    }

    private void log(String msg) {
        areaLog.append(">> " + msg + "\n");
     
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }

  

    private void accionFijarCargo(ActionEvent e) {
        try {
            String tipo = txtTipoEvento.getText().trim();
            double porc = Double.parseDouble(txtPorcentaje.getText().trim());
            
            
            adminActual.fijarCargoServicio(tipo, porc);
            
            log("Cargo actualizado: " + tipo + " -> " + (porc * 100) + "%");
            JOptionPane.showMessageDialog(this, "Cargo actualizado correctamente.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El porcentaje debe ser un número (ej. 0.15)", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            log("Error al fijar cargo: " + ex.getMessage());
        }
    }

    private void accionFijarCuota(ActionEvent e) {
        try {
            double monto = Double.parseDouble(txtCuota.getText());
            

            adminActual.fijarCuotaEmision(monto);
            
            log("Cuota de emisión fijada en: $" + monto);
            JOptionPane.showMessageDialog(this, "Cuota actualizada.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Monto inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionAprobarVenue(ActionEvent e) {
        String nombre = txtVenueNombre.getText().trim();
        String capStr = txtVenueCapacidad.getText().trim();

        if (nombre.isEmpty() || capStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int cap = Integer.parseInt(capStr);
            
            Venue v = new Venue("VEN-" + System.currentTimeMillis(), nombre, "Ubicación Pendiente", cap);
            

            if (adminActual.aprobarVenue(v)) {
                log("Venue APROBADO: " + nombre + " (Cap: " + cap + ")");
                JOptionPane.showMessageDialog(this, "Venue aprobado y registrado en el sistema.");
                txtVenueNombre.setText(""); txtVenueCapacidad.setText("");
            }
        } catch (Exception ex) {
            log("Error al aprobar venue: " + ex.getMessage());
        }
    }

    private void accionCancelarEvento(ActionEvent e) {
        String nombre = txtEventoCancelar.getText().trim();
        String motivo = txtMotivoCancelacion.getText().trim();

        if (nombre.isEmpty() || motivo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe especificar el evento y el motivo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de cancelar el evento '" + nombre + "'?\nEsta acción disparará reembolsos.", 
            "Confirmar Cancelación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
 
            Evento ev = new Evento("E-CANCEL", nombre, "General", "N/A", "00:00", null);
  
            adminActual.cancelarEvento(ev, motivo);
            
            log("EVENTO CANCELADO: " + nombre + " | Motivo: " + motivo);
            JOptionPane.showMessageDialog(this, "Evento cancelado. Se han iniciado los reembolsos.");
        }
    }
}