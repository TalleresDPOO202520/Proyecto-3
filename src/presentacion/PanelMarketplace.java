package presentacion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

import marketplace.Marketplace;
import marketplace.Oferta;
import usuarios.Cliente;
import tiquetes.Tiquete;
import tiquetes.TiqueteSimple;
import eventos.Evento;
import eventos.Localidad;
import eventos.Venue;

@SuppressWarnings("serial")
public class PanelMarketplace extends JPanel {

    private FPrincipal ventanaPrincipal;
    private Marketplace marketplace;
    private JPanel panelLista;

    public PanelMarketplace(FPrincipal principal) {
        this.ventanaPrincipal = principal;
        this.marketplace = principal.getMarketplace(); 
        cargarDatosPrueba();

        setLayout(new BorderLayout());
        setBackground(Color.CYAN);

        inicializarUI();
    }

    public void agregarBoletaReventa(Tiquete t) {

        try {
            String idOferta = "OF-" + System.currentTimeMillis();
            Cliente vendedor = (Cliente) ventanaPrincipal.getUsuarioActivo();

            ArrayList<Tiquete> lista = new ArrayList<>();
            lista.add(t);

       
            double precio = t.getLocalidad().getPrecio();

            marketplace.publicarOferta(idOferta, vendedor, lista, precio);

            actualizarLista();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error creando oferta: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    private void inicializarUI() {
        JLabel lblTitulo = new JLabel("Marketplace - Reventa de Tiquetes", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setBorder(new EmptyBorder(10, 0, 10, 0));
        add(lblTitulo, BorderLayout.NORTH);

        panelLista = new JPanel();
        panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));
        panelLista.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(panelLista);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        JButton btnRefrescar = new JButton("Actualizar Ofertas");
        btnRefrescar.addActionListener(e -> actualizarLista());
        add(btnRefrescar, BorderLayout.SOUTH);

        actualizarLista();
    }

    private void actualizarLista() {
        panelLista.removeAll();
        
        Map<String, Oferta> ofertas = marketplace.getOfertasActivas();

        if (ofertas.isEmpty()) {
            panelLista.add(new JLabel("No hay ofertas disponibles en este momento. Se debe esperar a que soporte valide las ofertas", SwingConstants.CENTER));
        } else {
            for (Oferta oferta : ofertas.values()) {
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

        String nombreEvento = "Evento Desconocido";
        if (!oferta.getTiquetes().isEmpty()) {
            nombreEvento = oferta.getTiquetes().get(0).getEvento().getNombre();
        }

        String info = String.format(
            "<html><b>%s</b><br>Vendedor: %s<br>Cantidad Tiquetes: %d</html>", 
            nombreEvento, 
            oferta.getVendedor().getLogin(), 
            oferta.getTiquetes().size()
        );
        
        JLabel lblInfo = new JLabel(info);
        lblInfo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tarjeta.add(lblInfo, BorderLayout.CENTER);

        JPanel panelDerecho = new JPanel(new GridLayout(3, 1, 5, 5));
        panelDerecho.setOpaque(false);

        JLabel lblPrecio = new JLabel("$ " + oferta.getPrecio(), SwingConstants.RIGHT);
        lblPrecio.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblPrecio.setForeground(new Color(0, 100, 0));
        panelDerecho.add(lblPrecio);

        JButton btnComprar = new JButton("Comprar");
        btnComprar.setBackground(new Color(255, 215, 0));
        btnComprar.setForeground(Color.black);
        btnComprar.addActionListener(e -> accionComprar(oferta));
        panelDerecho.add(btnComprar);

        JButton btnOfertar = new JButton("Ofertar");
        btnOfertar.setBackground(new Color(100, 149, 237));
        btnOfertar.setForeground(Color.black);
        btnOfertar.addActionListener(e -> accionContraOfertar(oferta));
        panelDerecho.add(btnOfertar);

        tarjeta.add(panelDerecho, BorderLayout.EAST);

        return tarjeta;
    }

    private void accionComprar(Oferta oferta) {
        Object usuario = ventanaPrincipal.getUsuarioActivo();

        if (usuario instanceof Cliente) {
            Cliente comprador = (Cliente) usuario;

            if (oferta.getVendedor().getLogin().equals(comprador.getLogin())) {
                JOptionPane.showMessageDialog(this, "No puedes comprar tu propia oferta.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                marketplace.comprarPrecioFijo(oferta.getIdOferta(), comprador);

             
                for (Tiquete t : oferta.getTiquetes()) {
                    ventanaPrincipal.registrarCompra(t);
                }

                JOptionPane.showMessageDialog(this, 
                        "¡Compra exitosa! Revisa la pestaña 'Mis Boletas'.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);

                actualizarLista();
                
                ventanaPrincipal.actualizarMisBoletas();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                        "Error en la compra: " + ex.getMessage(), 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
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

            String input = JOptionPane.showInputDialog(this,
                "El precio actual es $" + oferta.getPrecio() + "\nIngresa tu contraoferta:");

            if (input != null && !input.isEmpty()) {
                try {
                    double nuevoPrecio = Double.parseDouble(input);
                    String idContra = "CO-" + System.currentTimeMillis();

                    marketplace.contraOfertar(oferta.getIdOferta(), idContra, ofertante, nuevoPrecio);

                    JOptionPane.showMessageDialog(this, "Contraoferta enviada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        }
    }

    private void cargarDatosPrueba() {
        try {
        
            Cliente vendedorMock = new Cliente("Juan mecanico", "123");
            Venue venueMock = new Venue("V1", "Movistar Arena", "Bogotá", 1000);
            Evento eventoMock = new Evento("E1", "Juancho polo Tour", "Concierto", "2026-05-05", "20:00", venueMock);
            Localidad locMock = new Localidad("LOC1", "General", 200000, false, 100);

            ArrayList<Tiquete> tiquetes = new ArrayList<>();
            tiquetes.add(new TiqueteSimple(eventoMock, locMock, "TKT-MOCK-1"));

            marketplace.publicarOferta("OFERTA-01", vendedorMock, tiquetes, 180000);

            Cliente vendedor2 = new Cliente("EnriqueDiazFan", "987");
            Venue venue2 = new Venue("V2", "Estadio El Campín", "Bogotá", 45000);
            Evento evento2 = new Evento("E2", "La Caja Negra Tour", "Concierto", "2026-07-12", "19:30", venue2);
            Localidad loc2 = new Localidad("LOC2", "Platea Alta", 150000, false, 500);

            ArrayList<Tiquete> tks2 = new ArrayList<>();
            tks2.add(new TiqueteSimple(evento2, loc2, "TKT-ENRIQUE-1"));

            marketplace.publicarOferta("OFERTA-02", vendedor2, tks2, 140000);

            Cliente vendedor3 = new Cliente("PinillaASCII", "456");
            Venue venue3 = new Venue("V3", "Teatro Colón", "Bogotá", 1800);
            Evento evento3 = new Evento("E3", "Los Infaltables ASCII", "Show Digital", "2026-03-15", "18:00", venue3);
            Localidad loc3 = new Localidad("LOC3", "VIP", 250000, false, 80);

            ArrayList<Tiquete> tks3 = new ArrayList<>();
            tks3.add(new TiqueteSimple(evento3, loc3, "TKT-ASCII-1"));

            marketplace.publicarOferta("OFERTA-03", vendedor3, tks3, 240000);

  
            Cliente vendedor4 = new Cliente("DaniPro", "321");
            Venue venue4 = new Venue("V4", "Royal Center", "Bogotá", 5000);
            Evento evento4 = new Evento("E4", "La Gran Astucia", "Stand-Up", "2026-08-22", "21:00", venue4);
            Localidad loc4 = new Localidad("LOC4", "Preferencial", 120000, false, 300);

            ArrayList<Tiquete> tks4 = new ArrayList<>();
            tks4.add(new TiqueteSimple(evento4, loc4, "TKT-ASTUCIA-1"));

            marketplace.publicarOferta("OFERTA-04", vendedor4, tks4, 115000);

            
            Cliente vendedor5 = new Cliente("SantyTributo", "6969");
            Venue venue5 = new Venue("V5", "Teatro Municipal", "Bucaramanga", 900);
            Evento evento5 = new Evento("E5", "Tributo al Dogordito", "Rock Fusión", "2026-09-30", "20:45", venue5);
            Localidad loc5 = new Localidad("LOC5", "Golden Zone", 180000, false, 100);

            ArrayList<Tiquete> tks5 = new ArrayList<>();
            tks5.add(new TiqueteSimple(evento5, loc5, "TKT-DOGORDITO-1"));

            marketplace.publicarOferta("OFERTA-05", vendedor5, tks5, 170000);


        } catch (Exception e) {
            System.err.println("Error mock: " + e.getMessage());
        }
    }

    
}
