package usuarios;

import eventos.Evento;
import eventos.Venue;
import tiquetes.PoliticaCargos;
import tiquetes.Tiquete;

import java.util.HashMap;
import java.util.Map;

public class Administrador extends Usuario {

    private String idAdmin;
    private String nombre;
    private PoliticaCargos politicaCargos;
    private Map<String, Double> cargosPorTipoEvento;
    private double cuotaEmision;

    public Administrador(String login, String password, String idAdmin, String nombre) {
        super(login, password);
        this.idAdmin = idAdmin;
        this.nombre = nombre;
        this.cargosPorTipoEvento = new HashMap<>();
        this.politicaCargos = new PoliticaCargos(cargosPorTipoEvento);
    }

    @Override
    public void abonar(double monto) {
        System.out.println("❌ El administrador no puede abonar saldo.");
    }

    @Override
    public boolean debitar(double monto) {
        System.out.println("❌ El administrador no puede debitar saldo.");
        return false;
    }

    public void fijarCargoServicio(String tipoEvento, double porcentaje) {
        if (porcentaje < 0 || porcentaje > 1)
            throw new IllegalArgumentException("El porcentaje debe estar entre 0 y 1");
        cargosPorTipoEvento.put(tipoEvento.toLowerCase(), porcentaje);
        politicaCargos.setCargoServicioPorTipo(cargosPorTipoEvento);
    }

    public void fijarCuotaEmision(double monto) {
        if (monto < 0)
            throw new IllegalArgumentException("La cuota de emisión no puede ser negativa");
        this.cuotaEmision = monto;
        politicaCargos.setCuotaEmisionFija(monto);
    }

    public PoliticaCargos getPoliticaCargos() {
        return politicaCargos;
    }

    public boolean aprobarVenue(Venue venue) {
        if (venue == null) return false;
        System.out.println("✅ Venue aprobado: " + venue.getNombre());
        return true;
    }

    public boolean cancelarEvento(Evento evento, String motivo) {
        if (evento == null || motivo == null || motivo.isEmpty()) return false;
        evento.cancelarEvento();
        System.out.println("⚠️ Evento '" + evento.getNombre() + "' cancelado. Motivo: " + motivo);
        return true;
    }

    public boolean autorizarReembolso(Tiquete tiquete, String caso) {
        if (tiquete == null || caso == null) return false;
        System.out.println("Reembolso autorizado para el caso: " + caso +
                           " (Tiquete: " + tiquete.getIdTiquete() + ")");
        return true;
    }

    public void consultarFinanzas(String filtro) {
        System.out.println("📊 Consultando finanzas con filtro: " + filtro);
        System.out.println("Cargos por tipo de evento: " + cargosPorTipoEvento);
        System.out.println("Cuota de emisión actual: " + cuotaEmision);
    }

    public String getIdAdmin() { return idAdmin; }
    public String getNombre() { return nombre; }
    public double getCuotaEmision() { return cuotaEmision; }
}

