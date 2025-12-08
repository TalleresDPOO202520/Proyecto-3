package presentacion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.*;

import usuarios.Administrador;
import tiquetes.Tiquete;

@SuppressWarnings("serial")
public class FPrincipal extends JFrame {

    private JTabbedPane tabbedPane;

    // ESTADO GLOBAL
    private Object usuarioActivo;
    private List<Tiquete> misBoletasCompradas;

    private Administrador adminGlobal;

    public FPrincipal(Administrador admin) {
        this.adminGlobal = admin;   // 💥 Admin global accesible desde paneles
        this.misBoletasCompradas = new ArrayList<>();

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

        setSize(900, 700);
        setLayout(new BorderLayout());

        inicializarComponentes();
        setLocationRelativeTo(null);
        setVisible(true);

        reproducirMusica("src/fotos/blue_bird_cantado_por_spaceronin7.wav");
    }

    // ============================================================
    // GETTER DEL ADMIN → para que PanelAdministrador lo use
    // ============================================================
    public Administrador getAdministrador() {
        return adminGlobal;
    }

    // ============================================================
    // MÉTODOS DE ACCESO PARA OTROS PANELES
    // ============================================================

    public void setUsuarioActivo(Object usuario) {
        this.usuarioActivo = usuario;

        String rol = usuario.getClass().getSimpleName();
        this.setTitle("Boleta Master - Sesión: " + rol);

        if (rol.equals("Cliente")) {
            // Si es cliente, se desactiva panel organizador
            tabbedPane.setEnabledAt(3, false);
        }
    }

    /** Registrar compras globalmente */
    public void registrarCompra(Tiquete t) {
        if (t != null) {
            misBoletasCompradas.add(t);
        }       
       
    }
 // En src/presentacion/FPrincipal.java

 // Getter para que los paneles sepan quién está logueado
 public Object getUsuarioActivo() {
     return this.usuarioActivo;
 }

    /** Abrir una pestaña con un ticket */
    public void abrirPestanaTiquete(String tituloEvento, String rutaImagen) {

        JPanel panelTiquete = new JPanel(new BorderLayout());
        panelTiquete.setBackground(new Color(30, 30, 30));

        JLabel lblTitulo = new JLabel("¡Tu Boleta está lista!", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(new EmptyBorder(20, 0, 20, 0));
        panelTiquete.add(lblTitulo, BorderLayout.NORTH);

        JLabel lblFoto = new JLabel();
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);

        ImageIcon icon = new ImageIcon(rutaImagen);
        if (icon.getIconWidth() > 0) {
            Image img = icon.getImage().getScaledInstance(500, -1, Image.SCALE_SMOOTH);
            lblFoto.setIcon(new ImageIcon(img));
        } else {
            lblFoto.setText("Error: imagen no encontrada.");
            lblFoto.setForeground(Color.RED);
        }

        panelTiquete.add(lblFoto, BorderLayout.CENTER);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> {
            tabbedPane.remove(panelTiquete);
            tabbedPane.setSelectedIndex(0);
        });

        JPanel pBtn = new JPanel();
        pBtn.setBackground(new Color(30, 30, 30));
        pBtn.add(btnCerrar);

        panelTiquete.add(pBtn, BorderLayout.SOUTH);

        tabbedPane.addTab("🎟️ " + tituloEvento, panelTiquete);
        tabbedPane.setSelectedComponent(panelTiquete);
    }

    // ============================================================
    // INICIALIZACIÓN DE TABS
    // ============================================================
    private void inicializarComponentes() {

        tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 14));

        // Home
        tabbedPane.addTab("🏠 Home", new PanelHome(this));

        // Mis Boletas
        tabbedPane.addTab("🎫 Mis Boletas", new PanelMisBoletas(this, misBoletasCompradas));

        // Marketplace
        tabbedPane.addTab("🛒 Market", new PanelMarketplace(this));

        // Organizador
        tabbedPane.addTab("⚙️ Opciones", new PanelOrganizador());

        // 💥 Nuevo: Panel Administrador con comunicación total
        tabbedPane.addTab("🛡️ Admin", new PanelAdministrador(this, adminGlobal));

        add(tabbedPane, BorderLayout.CENTER);
    }


    // ============================================================
    // MÉTODOS DE UTILIDAD
    // ============================================================
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
            if (f.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(f);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);

                try {
                    FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gain.setValue(-12.0f);
                } catch (Exception ignore) {
                }

                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } catch (Exception e) {
            System.out.println("Error al reproducir música: " + e.getMessage());
        }
    }
}
