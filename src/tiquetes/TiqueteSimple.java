package tiquetes;

import eventos.Evento;
import eventos.Localidad;

public class TiqueteSimple extends Tiquete {
    public TiqueteSimple(Evento evento, Localidad localidad, String idTiquete) {
        super(evento, localidad, idTiquete);
    }

    @Override
    public double calcularPrecioTotal(PoliticaCargos cargos) {
        double precioBase = localidad.getPrecio();
        double porcentaje = cargos.cargoPara(evento.getTipo()); 
        return precioBase + (precioBase * porcentaje) + cargos.getCuotaEmisionFija();
    }
}
