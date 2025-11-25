package tiquetes;

import eventos.Evento;
import eventos.Localidad;

public class TiqueteNumerado extends Tiquete {
    private final String numAsiento;

    public TiqueteNumerado(Evento evento, Localidad localidad, String numAsiento) {
        super(evento, localidad, evento.getIdEvento() + "-" + localidad.getIdLocalidad() + "-" + numAsiento);
        this.numAsiento = numAsiento;
    }

    @Override
    public double calcularPrecioTotal(PoliticaCargos cargos) {
        double precioBase = localidad.getPrecio();
        double porcentajeServicio = cargos.cargoPara(evento.getTipo());
        return precioBase + (precioBase * porcentajeServicio) + cargos.getCuotaEmisionFija();
    }

    public String getNumAsiento() { return numAsiento; }
}
