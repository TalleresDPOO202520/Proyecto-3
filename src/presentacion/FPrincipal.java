package presentacion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import tiquetes.Tiquete;
import tiquetes.TiqueteSimple;
import eventos.Evento;
import eventos.Localidad;
import eventos.Venue;
import utils.Qr;

@SuppressWarnings("serial")
public class FPrincipal extends JFrame {

    private JTabbedPane tabbedPane;
    
    private final Color COLOR_BOTON_AMARILLO = new Color(255, 215, 0); 

    public FPrincipal() {
        setTitle("Boleta Master Kakashi");
        
        
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                JOptionPane.showMessageDialog(FPrincipal.this, 
                    "gracias papu :v", 
                    "Adiós", 
                    JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        });

        setSize(700, 800); 
        setLayout(new BorderLayout());

        inicializarComponentes();

        setLocationRelativeTo(null); 
        setVisible(true);
        
        reproducirMusicaFondo("src/fotos/blue_bird_cantado_por_spaceronin7.wav");
    }

    private void inicializarComponentes() {
        tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
        
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 14));       
    
        tabbedPane.addTab("<html><div style='padding: 15px; width: 100px;'>🏠 Home</div></html>", crearPanelHome());
        tabbedPane.addTab("<html><div style='padding: 15px; width: 100px;'>🛒 Market</div></html>", crearPanelMarketplace());
        tabbedPane.addTab("<html><div style='padding: 15px; width: 100px;'>⚙️ Opciones</div></html>", crearPanelOrganizador());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel crearPanelHome() {
        JPanel panelPrincipal = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Fondo Kakashi
                ImageIcon imagenFondo = new ImageIcon("src/fotos/boleta maste kakshi.png");
                if (imagenFondo.getImage() != null) {
                    g.drawImage(imagenFondo.getImage(), 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JLabel lblTitulo = new JLabel("Boleta Master Kakashi");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        headerPanel.add(lblTitulo, BorderLayout.WEST);
        
        panelPrincipal.add(headerPanel, BorderLayout.NORTH);

        JPanel panelListaEventos = new JPanel();
        panelListaEventos.setLayout(new BoxLayout(panelListaEventos, BoxLayout.Y_AXIS));
        panelListaEventos.setOpaque(false);
        panelListaEventos.setBorder(new EmptyBorder(10, 10, 10, 10));

        panelListaEventos.add(crearTarjetaEvento(
            "Got Back", "Paul McCartney", "Abril 11, 8:00pm", "Estadio El Campín", 
            "src/fotos/juancho.jpg", 
            450000));

        panelListaEventos.add(Box.createRigidArea(new Dimension(0, 15)));

        panelListaEventos.add(crearTarjetaEvento(
            "Las mujeres ya no lloran", "Shakira", "Julio 15, 7:30pm", "Movistar Arena", 
            "src/fotos/Enrique.jpeg", 
            300000));

        // ScrollPane Invisible
        JScrollPane scrollPane = new JScrollPane(panelListaEventos);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);

        return panelPrincipal;
    }

    private JPanel crearTarjetaEvento(String nombreTour, String artista, String fecha, 
                                      String lugar, String rutaImagen, double precio) {

        Color colorFondoTarjeta = new Color(50, 50, 50); 
        Color colorTextoTitulo = Color.WHITE;
        Color colorTextoCuerpo = new Color(200, 200, 200); 

        JPanel tarjeta = new JPanel(new BorderLayout(15, 0));
        tarjeta.setBackground(colorFondoTarjeta); 
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80), 1),
                new EmptyBorder(10, 10, 10, 10)));
        tarjeta.setMaximumSize(new Dimension(800, 140));

        // Imagen Izquierda
        JPanel panelImagen = new JPanel(new BorderLayout());
        panelImagen.setPreferredSize(new Dimension(100, 100));
        panelImagen.setBackground(colorFondoTarjeta); 
        
        JLabel lblIcono = new JLabel();
        lblIcono.setHorizontalAlignment(SwingConstants.CENTER);

        ImageIcon iconOriginal = new ImageIcon(rutaImagen);
        if (iconOriginal.getIconWidth() > -1) {
            Image imgEscalada = iconOriginal.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            lblIcono.setIcon(new ImageIcon(imgEscalada));
        } else {
            lblIcono.setText("Sin Foto");
            lblIcono.setForeground(Color.WHITE);
            panelImagen.setBackground(Color.GRAY);
        }
        
        panelImagen.add(lblIcono, BorderLayout.CENTER);
        tarjeta.add(panelImagen, BorderLayout.WEST);

        // Detalles Centro
        JPanel panelDetalles = new JPanel(new GridLayout(4, 1));
        panelDetalles.setBackground(colorFondoTarjeta); 

        JLabel lblTour = new JLabel(nombreTour);
        lblTour.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTour.setForeground(colorTextoTitulo); 

        JLabel lblArtista = new JLabel(artista);
        lblArtista.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblArtista.setForeground(colorTextoCuerpo); 

        JLabel lblFecha = new JLabel(fecha);
        lblFecha.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblFecha.setForeground(colorTextoCuerpo); 

        JLabel lblLugar = new JLabel(lugar);
        lblLugar.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblLugar.setForeground(colorTextoCuerpo); 

        panelDetalles.add(lblTour);
        panelDetalles.add(lblArtista);
        panelDetalles.add(lblFecha);
        panelDetalles.add(lblLugar);

        tarjeta.add(panelDetalles, BorderLayout.CENTER);

        JButton btnComprar = new JButton("Comprar ahora");
        btnComprar.setBackground(COLOR_BOTON_AMARILLO); 
        btnComprar.setForeground(Color.BLACK); 
        btnComprar.setFocusPainted(false);
        btnComprar.setFont(new Font("SansSerif", Font.BOLD, 12));

        btnComprar.addActionListener(e -> 
            generarQrParaTiqueteDinamico(nombreTour, artista, fecha, lugar, precio, rutaImagen)
        );

        JPanel panelBoton = new JPanel(new GridBagLayout());
        panelBoton.setBackground(colorFondoTarjeta); 
        panelBoton.add(btnComprar);

        tarjeta.add(panelBoton, BorderLayout.EAST);

        return tarjeta;
    }

    private void abrirPestanaTiquete(String tituloEvento, String rutaImagen) {
        JPanel panelTiquete = new JPanel(new BorderLayout());
        panelTiquete.setBackground(new Color(30, 30, 30));

        JLabel lblTitulo = new JLabel("¡Tu Boleta está lista!", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(new EmptyBorder(20, 0, 20, 0));
        panelTiquete.add(lblTitulo, BorderLayout.NORTH);

        JLabel lblFotoTiquete = new JLabel();
        lblFotoTiquete.setHorizontalAlignment(SwingConstants.CENTER);
        
        ImageIcon icon = new ImageIcon(rutaImagen);
     
        if (icon.getIconWidth() > 0) {
            Image img = icon.getImage().getScaledInstance(500, -1, Image.SCALE_SMOOTH);
            lblFotoTiquete.setIcon(new ImageIcon(img));
        } else {
            lblFotoTiquete.setText("Imagen generada correctamente");
            lblFotoTiquete.setForeground(Color.WHITE);
        }
        
        panelTiquete.add(lblFotoTiquete, BorderLayout.CENTER);

        // Botón Cerrar
        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(new Color(30, 30, 30));
        panelBoton.setBorder(new EmptyBorder(20, 0, 20, 0));

        JButton btnCerrar = new JButton("Cerrar y Volver");
        btnCerrar.setBackground(new Color(220, 50, 50)); 
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        btnCerrar.addActionListener(e -> {
            tabbedPane.remove(panelTiquete);
            tabbedPane.setSelectedIndex(0); 
        });

        panelBoton.add(btnCerrar);
        panelTiquete.add(panelBoton, BorderLayout.SOUTH);

        tabbedPane.addTab("🎟️ Ticket: " + tituloEvento, panelTiquete);
        tabbedPane.setSelectedComponent(panelTiquete);
    }

   private void generarQrParaTiqueteDinamico(String nombreEvento, String artista,
                                              String fecha, String lugar, double precio, String rutaFotoArtista) {
        try {
            Venue venueMock = new Venue("VEN-001", lugar, "Dirección desconocida", 50000);
            Evento eventoMock = new Evento("EVE-" + System.currentTimeMillis(), 
                nombreEvento + " (" + artista + ")", "Concierto", fecha, "20:00", venueMock);
            Localidad localidadMock = new Localidad("LOC-GEN", "General", precio, false, 2000);
            Tiquete tiqueteMock = new TiqueteSimple(eventoMock, localidadMock, "TKT-" + System.currentTimeMillis());

            String datosParaQR = tiqueteMock.generarDatosQR();
            String nombreArchivo = "qr_" + tiqueteMock.getIdTiquete() + ".png";
            String carpetaDestino = "C:/QRs_Generados/";
            
            File carpeta = new File(carpetaDestino);
            if (!carpeta.exists()) carpeta.mkdirs();
            
            String rutaQR = carpetaDestino + nombreArchivo;
            
            if (Qr.crearCodigoQR(datosParaQR, rutaQR)) {
                tiqueteMock.marcarImpreso();
                
               
                abrirPestanaTiquete(artista, rutaQR); 
                
                JOptionPane.showMessageDialog(this, "Compra exitosa para: " + artista);
            } else {
                JOptionPane.showMessageDialog(this, "Error generando QR", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void reproducirMusicaFondo(String rutaArchivo) {
        try {
            File archivoMusica = new File(rutaArchivo);
            if (archivoMusica.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(archivoMusica);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                try {
                    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(-10.0f);
                } catch (Exception e) {}
                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private JPanel crearPanelMarketplace() {
        JPanel p = new JPanel(); p.add(new JLabel("Marketplace...")); return p;
    }
    private JPanel crearPanelOrganizador() {
        JPanel p = new JPanel(); p.add(new JLabel("Admin...")); return p;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new FPrincipal());
    }
}