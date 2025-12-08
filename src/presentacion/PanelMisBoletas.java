package presentacion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

import tiquetes.Tiquete;
import utils.Qr;

@SuppressWarnings("serial")
public class PanelMisBoletas extends JPanel {

    private FPrincipal ventanaPrincipal;
    private List<Tiquete> listaBoletas;
    private JPanel panelLista;

    public PanelMisBoletas(FPrincipal principal, List<Tiquete> boletas) {
        this.ventanaPrincipal = principal;
        this.listaBoletas = boletas;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        inicializarUI();
    }

    private void inicializarUI() {
        JLabel lblTitulo = new JLabel("Mis Boletas Compradas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setBorder(new EmptyBorder(10, 0, 10, 0));
        add(lblTitulo, BorderLayout.NORTH);

        panelLista = new JPanel();
        panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));

        JScrollPane scroll = new JScrollPane(panelLista);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        JButton btnRefrescar = new JButton("Actualizar Lista");
        btnRefrescar.addActionListener(e -> actualizarLista());
        add(btnRefrescar, BorderLayout.SOUTH);

        actualizarLista();
    }

    public void actualizarLista() {
        panelLista.removeAll();

        if (listaBoletas == null || listaBoletas.isEmpty()) {
            JLabel lbl = new JLabel("Aún no has comprado boletas.", SwingConstants.CENTER);
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
            panelLista.add(lbl);
        } else {
            for (Tiquete t : listaBoletas) {
                panelLista.add(crearTarjetaBoleta(t));
                panelLista.add(Box.createRigidArea(new Dimension(0, 8)));
            }
        }

        panelLista.revalidate();
        panelLista.repaint();
    }

    private JPanel crearTarjetaBoleta(Tiquete t) {
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180,180,180)),
            new EmptyBorder(10,10,10,10)
        ));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setMaximumSize(new Dimension(700, 90));

        String estado = t.isImpreso() ? "IMPRESO" :
                        (t.isTransferido() ? "TRANSFERIDO" : "DIGITAL");

        JLabel lblInfo = new JLabel(
            "<html><b>" + t.getEvento().getNombre() + "</b><br>" +
            "ID: " + t.getIdTiquete() + "<br>" +
            "Estado: " + estado + "</html>"
        );
        tarjeta.add(lblInfo, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(Color.WHITE);

        JButton btnQR = new JButton(t.isImpreso() ? "Ya Impreso" : "Ver QR");

        if (!t.isImpreso()) {
            btnQR.addActionListener(e -> {
                String rutaQR = "C:/QRs_Generados/qr_" + t.getIdTiquete() + ".png";

                if (Qr.crearCodigoQR(t.generarDatosQR(), rutaQR)) {
                    ventanaPrincipal.abrirPestanaTiquete(t.getEvento().getNombre(), rutaQR);
                    t.marcarImpreso();
                    actualizarLista();
                }
            });
        } else {
            btnQR.setEnabled(false);
            btnQR.setBackground(Color.GRAY);
        }

        JButton btnVender = new JButton("Vender");
        btnVender.addActionListener(e -> ventanaPrincipal.enviarBoletaAlMarket(t));

        panelBotones.add(btnQR);
        panelBotones.add(btnVender);

        tarjeta.add(panelBotones, BorderLayout.EAST);

        return tarjeta;
    }

}
