package presentacion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

// Importaciones de tu lógica de negocio
import marketplace.Marketplace;
import marketplace.Oferta;
import usuarios.Cliente;
import tiquetes.Tiquete;
import tiquetes.TiqueteSimple; // Necesario para el mock
import eventos.Evento;        // Necesario para el mock
import eventos.Localidad;     // Necesario para el mock
import eventos.Venue;         // Necesario para el mock

@SuppressWarnings("serial")
public class PanelMarketplace extends JPanel {

    private FPrincipal ventanaPrincipal;
    private Marketplace marketplace; // Instancia de la lógica
    private JPanel panelLista;

    public PanelMarketplace(FPrincipal principal) {
        this.ventanaPrincipal = principal;
        
        // Inicializamos la lógica del Marketplace
        this.marketplace = new Marketplace();
        
        // Cargar datos de prueba para que veas algo en la pantalla
        cargarDatosPrueba();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        inicializarUI();
    }

    private void inicializarUI() {
        // Título
        JLabel lblTitulo = new JLabel("Marketplace - Reventa de Tiquetes", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setBorder(new EmptyBorder(10, 0, 10, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // Contenedor de la lista
        panelLista = new JPanel();
        panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));
        panelLista.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(panelLista);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        // Botón para refrescar
        JButton btnRefrescar = new JButton("Actualizar Ofertas");
        btnRefrescar.addActionListener(e -> actualizarLista());
        add(btnRefrescar, BorderLayout.SOUTH);

        // Cargar lista inicial
        actualizarLista();
    }

    private void actualizarLista() {
        panelLista.removeAll();
        
        Map<String, Oferta> ofertas = marketplace.getOfertasActivas();

        if (ofertas.isEmpty()) {
            panelLista.add(new JLabel("No hay ofertas disponibles en este momento.", SwingConstants.CENTER));
        } else {
            for (Oferta oferta : ofertas.values()) {
                // Solo mostramos ofertas con estado 0 (Publicado)
                if (oferta.getEstado() == 0) {
                    panelLista.add(crearTarjetaOferta(oferta));
                    panelLista.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }
        }
        
        panelLista.revalidate();
        panelLista.repaint();
    }

    private JPanel crearTarjetaOferta(Oferta oferta) {
        JPanel tarjeta = new JPanel(new BorderLayout(10, 10));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(10, 10, 10, 10)
        ));
        tarjeta.setBackground(new Color(245, 245, 250));
        tarjeta.setMaximumSize(new Dimension(700, 100));

        // 1. Información de la Oferta
        // Obtenemos info del primer tiquete para mostrar de qué evento es
        String nombreEvento = "Evento Desconocido";
        if (!oferta.getTiquetes().isEmpty()) {
            nombreEvento = oferta.getTiquetes().get(0).getEvento().getNombre();
        }

        String info = String.format("<html><b>%s</b><br>Vendedor: %s<br>Cantidad Tiquetes: %d</html>", 
                nombreEvento, 
                oferta.getVendedor().getLogin(), 
                oferta.getTiquetes().size());
        
        JLabel lblInfo = new JLabel(info);
        lblInfo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tarjeta.add(lblInfo, BorderLayout.CENTER);

        // 2. Precio y Botones
        JPanel panelDerecho = new JPanel(new GridLayout(3, 1, 5, 5));
        panelDerecho.setOpaque(false);

        JLabel lblPrecio = new JLabel("$ " + oferta.getPrecio(), SwingConstants.RIGHT);
        lblPrecio.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblPrecio.setForeground(new Color(0, 100, 0));
        panelDerecho.add(lblPrecio);

        // Botón Comprar
        JButton btnComprar = new JButton("Comprar");
        btnComprar.setBackground(new Color(255, 215, 0)); // Amarillo
        btnComprar.addActionListener(e -> accionComprar(oferta));
        panelDerecho.add(btnComprar);

        // Botón Contraofertar
        JButton btnOfertar = new JButton("Ofertar");
        btnOfertar.setBackground(new Color(100, 149, 237)); // Azul
        btnOfertar.setForeground(Color.WHITE);
        btnOfertar.addActionListener(e -> accionContraOfertar(oferta));
        panelDerecho.add(btnOfertar);

        tarjeta.add(panelDerecho, BorderLayout.EAST);

        return tarjeta;
    }

    // ========================================================================
    // LÓGICA DE ACCIONES (Conectando con tu Backend)
    // ========================================================================

    private void accionComprar(Oferta oferta) {
        // Necesitamos saber quién es el usuario logueado
        Object usuario = ventanaPrincipal.getUsuarioActivo();

        if (usuario instanceof Cliente) {
            Cliente comprador = (Cliente) usuario;
            
            // Validar que no se compre a sí mismo
            if (oferta.getVendedor().getLogin().equals(comprador.getLogin())) {
                JOptionPane.showMessageDialog(this, "No puedes comprar tu propia oferta.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                // Llamamos a tu método del backend
                marketplace.comprarPrecioFijo(oferta.getIdOferta(), comprador);
                
                JOptionPane.showMessageDialog(this, "¡Compra exitosa!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                actualizarLista(); // Refrescar para quitar la oferta vendida
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error en la compra: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Solo los Clientes pueden comprar.", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void accionContraOfertar(Oferta oferta) {
        Object usuario = ventanaPrincipal.getUsuarioActivo();

        if (usuario instanceof Cliente) {
            Cliente ofertante = (Cliente) usuario;

            if (oferta.getVendedor().getLogin().equals(ofertante.getLogin())) {
                JOptionPane.showMessageDialog(this, "No puedes ofertar en tu propia publicación.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String input = JOptionPane.showInputDialog(this, "El precio actual es $" + oferta.getPrecio() + "\nIngresa tu contraoferta:");
            
            if (input != null && !input.isEmpty()) {
                try {
                    double nuevoPrecio = Double.parseDouble(input);
                    String idContra = "CO-" + System.currentTimeMillis();
                    
                    // Llamamos a tu método del backend
                    marketplace.contraOfertar(oferta.getIdOferta(), idContra, ofertante, nuevoPrecio);
                    
                    JOptionPane.showMessageDialog(this, "Contraoferta enviada al vendedor.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Por favor ingresa un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Solo los Clientes pueden ofertar.", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
        }
    }

    // ========================================================================
    // DATOS DE PRUEBA (MOCKS)
    // ========================================================================
    private void cargarDatosPrueba() {
        try {
            // Creamos objetos necesarios para que la oferta sea válida
            Cliente vendedorMock = new Cliente("Juan mecanico", "123");
            Venue venueMock = new Venue("V1", "Movistar Arena", "Bogotá", 1000);
            Evento eventoMock = new Evento("E1", "Juancho polo Tour", "Concierto", "2026-05-05", "20:00", venueMock);
            Localidad locMock = new Localidad("LOC1", "General", 200000, false, 100);
            
            ArrayList<Tiquete> tiquetes = new ArrayList<>();
            tiquetes.add(new TiqueteSimple(eventoMock, locMock, "TKT-MOCK-1"));
            
            // Publicamos una oferta de prueba
            marketplace.publicarOferta("OFERTA-01", vendedorMock, tiquetes, 180000); // Precio más barato que original
            
        } catch (Exception e) {
            System.err.println("Error cargando mocks: " + e.getMessage());
        }
    }
}