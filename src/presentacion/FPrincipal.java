package presentacion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList; // Necesario para inicializar la lista
import java.util.List; // Necesario para List<Tiquete>

import javax.sound.sampled.*;
// Importaciones de tus clases de dominio
import tiquetes.Tiquete;
import tiquetes.TiqueteSimple;
import eventos.Evento;
import eventos.Localidad;
import eventos.Venue;
import utils.Qr;


@SuppressWarnings("serial")
public class FPrincipal extends JFrame {

    private JTabbedPane tabbedPane;
    
    // 1. ESTADO GLOBAL: Declaración de variables de estado
    private Object usuarioActivo;
    private List<Tiquete> misBoletasCompradas; 

    private final Color COLOR_BOTON_AMARILLO = new Color(255, 215, 0);

    public FPrincipal() {
        setTitle("Boleta Master Kakashi");
        
        // 🚨 FIX 1: Inicializar la lista (Soluciona NullPointerException)
        this.misBoletasCompradas = new ArrayList<>();
        
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
        setVisible(true); // Mantengo el setVisible aquí por el código antiguo
        
        reproducirMusica("src/fotos/blue_bird_cantado_por_spaceronin7.wav");
    }

    // =================================================================================
    // MÉTODOS AÑADIDOS PARA COMUNICACIÓN (FIXES)
    // =================================================================================

    /**
     * Permite a Main.java pasar el usuario activo y deshabilitar pestañas.
     */
    public void setUsuarioActivo(Object usuario) {
        this.usuarioActivo = usuario;
        String rol = usuario.getClass().getSimpleName();
        this.setTitle("Boleta Master - Sesión: " + rol);
        
        if (rol.equals("Cliente")) {
             tabbedPane.setEnabledAt(3, false); 
        }
    }

    /** FIX 2: Permite a PanelHome guardar el ticket en la lista central. */
    public void registrarCompra(tiquetes.Tiquete t) {
        if (t != null) {
            misBoletasCompradas.add(t);
        }
    }
    
    /** FIX 3: Permite a PanelMisBoletas mostrar el QR en una nueva pestaña. */
    public void abrirPestanaTiquete(String tituloEvento, String rutaImagen) {
        
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
            lblFotoTiquete.setText("Error: Imagen QR no generada.");
            lblFotoTiquete.setForeground(Color.RED);
        }
        panelTiquete.add(lblFotoTiquete, BorderLayout.CENTER);

        JButton btnCerrar = new JButton("Cerrar y Volver");
        btnCerrar.setBackground(new Color(220, 50, 50)); 
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        btnCerrar.addActionListener(e -> {
            tabbedPane.remove(panelTiquete);
            tabbedPane.setSelectedIndex(0); 
        });

        JPanel panelBoton = new JPanel(new FlowLayout());
        panelBoton.setBackground(new Color(30,30,30));
        panelBoton.add(btnCerrar);
        panelTiquete.add(panelBoton, BorderLayout.SOUTH);

        tabbedPane.addTab("🎟️ Ticket: " + tituloEvento, panelTiquete);
        tabbedPane.setSelectedComponent(panelTiquete);
    }

    // =================================================================================
    // INICIALIZACIÓN DE PANELES (Modificado solo para pasar la lista)
    // =================================================================================
    private void inicializarComponentes() {
        tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 14));

        // --- LAS REFERENCIAS SE PASAN A LOS CONSTRUCTORES ---
        
        // PanelHome necesita la referencia del padre (this)
        tabbedPane.addTab("<html><div style='padding: 15px;'>🏠 Home</div></html>", new PanelHome(this));
        
        // PanelMisBoletas necesita la referencia del padre y la lista
        tabbedPane.addTab("<html><div style='padding: 15px;'>🎫 Mis Boletas</div></html>", new PanelMisBoletas(this, misBoletasCompradas));
        
        // Placeholders
        tabbedPane.addTab("<html><div style='padding: 15px;'>🛒 Market</div></html>", new PanelMarketplace());
        tabbedPane.addTab("<html><div style='padding: 15px;'>⚙️ Opciones</div></html>", new PanelOrganizador());

        add(tabbedPane, BorderLayout.CENTER);
    }

    // --- MÉTODOS ANTIGUOS SIN MODIFICAR ---
    // (Asegurando que no rompan las llamadas de otros archivos)

    public void abrirPestanaNueva(String titulo, JComponent contenido) {
        tabbedPane.addTab(titulo, contenido);
        tabbedPane.setSelectedComponent(contenido);
    }
    
    public void cerrarPestana(JComponent contenido) {
        tabbedPane.remove(contenido);
        tabbedPane.setSelectedIndex(0); 
    }

    private void reproducirMusica(String ruta) {
        try {
            File f = new File(ruta);
            if(f.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(f);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                try {
                    FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gain.setValue(-10.0f);
                } catch(Exception e){}
                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } catch(Exception e) { e.printStackTrace(); }
    }
}