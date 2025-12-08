package presentacion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

import tiquetes.*;
import eventos.*;
import utils.Qr;

@SuppressWarnings("serial")
public class PanelHome extends JPanel {

    private FPrincipal ventanaPrincipal; // Referencia al padre para abrir pestañas

    public PanelHome(FPrincipal principal) {
        this.ventanaPrincipal = principal;
        setLayout(new BorderLayout());
        inicializarUI();
    }

    // PINTAR EL FONDO DE KAKASHI
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon imagenFondo = new ImageIcon("src/fotos/boleta maste kakshi.png");
        if (imagenFondo.getImage() != null) {
            g.drawImage(imagenFondo.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void inicializarUI() {
        // Header Transparente
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JLabel lblTitulo = new JLabel("Boleta Master Kakashi");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        headerPanel.add(lblTitulo, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // Lista Eventos
        JPanel panelLista = new JPanel();
        panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));
        panelLista.setOpaque(false);
        panelLista.setBorder(new EmptyBorder(10, 10, 10, 10));

        // AGREGAR EVENTOS
        panelLista.add(crearTarjeta("Got Back", "Paul McCartney", "Abril 11", "Estadio Campín", "src/fotos/juancho.jpg", 450000));
        panelLista.add(Box.createRigidArea(new Dimension(0, 15)));
        panelLista.add(crearTarjeta("Las mujeres ya no lloran", "Shakira", "Julio 15", "Movistar Arena", "src/fotos/Enrique.jpeg", 300000));

        // Scroll
        JScrollPane scroll = new JScrollPane(panelLista);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel crearTarjeta(String tour, String artista, String fecha, String lugar, String rutaFoto, double precio) {
        JPanel tarjeta = new JPanel(new BorderLayout(15, 0));
        tarjeta.setBackground(new Color(50, 50, 50)); // Dark mode
        tarjeta.setBorder(BorderFactory.createLineBorder(new Color(80,80,80), 1));
        tarjeta.setMaximumSize(new Dimension(800, 140));

        // Foto
        JLabel lblFoto = new JLabel();
        ImageIcon icon = new ImageIcon(rutaFoto);
        if (icon.getIconWidth() > 0) {
            lblFoto.setIcon(new ImageIcon(icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
        } else {
            lblFoto.setText("Sin Foto");
            lblFoto.setForeground(Color.WHITE);
        }
        tarjeta.add(lblFoto, BorderLayout.WEST);

        // Info
        JPanel info = new JPanel(new GridLayout(4, 1));
        info.setBackground(new Color(50, 50, 50));
        
        JLabel l1 = new JLabel(tour); l1.setForeground(Color.WHITE); l1.setFont(new Font("SansSerif", Font.BOLD, 16));
        JLabel l2 = new JLabel(artista); l2.setForeground(Color.LIGHT_GRAY);
        JLabel l3 = new JLabel(fecha); l3.setForeground(Color.LIGHT_GRAY);
        JLabel l4 = new JLabel(lugar); l4.setForeground(Color.LIGHT_GRAY);
        
        info.add(l1); info.add(l2); info.add(l3); info.add(l4);
        tarjeta.add(info, BorderLayout.CENTER);

        // Botón
        JButton btn = new JButton("Comprar ahora");
        btn.setBackground(new Color(255, 215, 0));
        btn.addActionListener(e -> logicaCompra(tour, artista, fecha, lugar, precio, rutaFoto));
        
        JPanel pBtn = new JPanel(); 
        pBtn.setBackground(new Color(50, 50, 50));
        pBtn.add(btn);
        tarjeta.add(pBtn, BorderLayout.EAST);

        return tarjeta;
    }

    private void logicaCompra(String nombre, String artista, String fecha, String lugar, double precio, String img) {
        try {
            // Mocks
            Venue v = new Venue("V1", lugar, "Dir", 5000);
            Evento ev = new Evento("E1", nombre + " - " + artista, "Concierto", fecha, "20:00", v);
            Localidad loc = new Localidad("L1", "General", precio, false, 100);
            Tiquete t = new TiqueteSimple(ev, loc, "TKT-" + System.currentTimeMillis());

            String rutaQR = "C:/QRs_Generados/qr_" + t.getIdTiquete() + ".png";
            new File("C:/QRs_Generados").mkdirs();

            if (Qr.crearCodigoQR(t.generarDatosQR(), rutaQR)) {
                t.marcarImpreso();
                
                // *** AQUI LLAMAMOS AL PADRE PARA ABRIR LA PESTAÑA ***
                JPanel panelBoleta = crearPanelBoleta(artista, rutaQR);
                ventanaPrincipal.abrirPestanaNueva("🎟️ " + artista, panelBoleta);
                
                JOptionPane.showMessageDialog(this, "Compra Exitosa!");
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Crea el panel visual de la boleta que se abre en la nueva pestaña
    private JPanel crearPanelBoleta(String titulo, String rutaImg) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(30, 30, 30));
        
        JLabel lbl = new JLabel("¡Tu Boleta!", SwingConstants.CENTER);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 20)); 
        lbl.setForeground(Color.WHITE);
        p.add(lbl, BorderLayout.NORTH);

        JLabel img = new JLabel();
        img.setHorizontalAlignment(SwingConstants.CENTER);
        img.setIcon(new ImageIcon(new ImageIcon(rutaImg).getImage().getScaledInstance(500, -1, Image.SCALE_SMOOTH)));
        p.add(img, BorderLayout.CENTER);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setBackground(Color.RED);
        btnCerrar.setForeground(Color.black);
        btnCerrar.addActionListener(e -> ventanaPrincipal.cerrarPestana(p)); // Llama al padre para cerrarse
        
        JPanel pBtn = new JPanel(); pBtn.setBackground(new Color(30,30,30));
        pBtn.add(btnCerrar);
        p.add(pBtn, BorderLayout.SOUTH);
        
        return p;
    }
}