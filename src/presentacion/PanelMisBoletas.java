package presentacion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.io.File; 

import tiquetes.Tiquete;
import utils.Qr; 
// Importaciones de Evento, Localidad, etc., si son necesarias para los mocks de tu proyecto

@SuppressWarnings({ "serial", "unused" })
public class PanelMisBoletas extends JPanel {

    private FPrincipal ventanaPrincipal;
    private List<Tiquete> listaBoletas; 
    private JPanel panelLista;

    // El constructor recibe la lista inicializada desde FPrincipal
    public PanelMisBoletas(FPrincipal principal, List<Tiquete> boletas) {
        this.ventanaPrincipal = principal;
        this.listaBoletas = boletas; 
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        // La lista ya es segura, no es NULL
        inicializarUI();
    }

    private void inicializarUI() {
        JLabel lblTitulo = new JLabel("Mis Boletas Compradas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setBorder(new EmptyBorder(10, 0, 10, 0));
        add(lblTitulo, BorderLayout.NORTH);

        panelLista = new JPanel();
        panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));
        
        actualizarLista();

        JScrollPane scroll = new JScrollPane(panelLista);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);
        
        JButton btnRefrescar = new JButton("Actualizar Lista");
        btnRefrescar.setBackground(Color.LIGHT_GRAY);
        btnRefrescar.addActionListener(e -> actualizarLista());
        add(btnRefrescar, BorderLayout.SOUTH);
    }

    private void actualizarLista() {
        panelLista.removeAll();
        
        if (listaBoletas.isEmpty()) { // Ahora es seguro llamar a isEmpty()
            panelLista.add(new JLabel("Aún no has comprado boletas.", SwingConstants.CENTER));
        } else {
            for (Tiquete t : listaBoletas) {
                JPanel tarjeta = crearTarjetaBoleta(t);
                panelLista.add(tarjeta);
                panelLista.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }
        
        panelLista.revalidate();
        panelLista.repaint();
    }

    private JPanel crearTarjetaBoleta(Tiquete t) {
        JPanel tarjeta = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        tarjeta.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setMaximumSize(new Dimension(700, 50));
        
        String estado = t.isImpreso() ? " (IMPRESO)" : (t.isTransferido() ? " (TRANSFERIDO)" : " (DIGITAL)");
        tarjeta.add(new JLabel("<html><b>" + t.getEvento().getNombre() + "</b> | ID: " + t.getIdTiquete() + estado + "</html>"));
        
        // Botón Ver QR / Imprimir
        JButton btnVerQR = new JButton(t.isImpreso() ? "Ya Impreso" : "Ver QR");
        if (t.isImpreso()) {
            btnVerQR.setEnabled(false);
            btnVerQR.setBackground(Color.GRAY);
        } else {
            btnVerQR.addActionListener(e -> {
                String rutaQR = "C:/QRs_Generados/qr_" + t.getIdTiquete() + ".png";
                if (Qr.crearCodigoQR(t.generarDatosQR(), rutaQR)) {
                    ventanaPrincipal.abrirPestanaTiquete(t.getEvento().getNombre(), rutaQR);
                    t.marcarImpreso(); 
                    actualizarLista(); 
                }
            });
        }
        tarjeta.add(btnVerQR);
        
        return tarjeta;
    }
}