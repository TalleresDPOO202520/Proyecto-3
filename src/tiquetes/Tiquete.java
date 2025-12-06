package tiquetes;

import eventos.Evento;
import eventos.Localidad;


public abstract class Tiquete {
    protected String idTiquete;
    protected boolean transferible = true;
    protected boolean transferido = false;
    protected boolean impreso = false; 
    protected Evento evento;
    protected Localidad localidad;

    protected Tiquete(Evento evento, Localidad localidad, String idTiquete) {
        this.evento = evento;
        this.localidad = localidad;
        this.idTiquete = idTiquete;
    }

    public abstract double calcularPrecioTotal(PoliticaCargos cargos);

    public String getIdTiquete() { return idTiquete; }
    public Evento getEvento() { return evento; }
    public Localidad getLocalidad() { return localidad; }
    public boolean isTransferido() { return transferido; }
    public boolean isTransferible() { return transferible; }
    public boolean isImpreso() { return impreso; } 
    
    public void marcarTransferido() {
        if (transferible && !impreso) { 
            this.transferido = true;
        }
    }


    public void marcarImpreso() { 
        this.impreso = true;
    }

    public String generarDatosQR() {
        String nombreLugar = "Sin lugar asignado";
        if (this.evento.getVenue() != null) {
            nombreLugar = this.evento.getVenue().getNombre(); 
        }

        
        StringBuilder sb = new StringBuilder();
        sb.append("=== BOLETA MASTER Kakashi===\n");
        sb.append("Evento: ").append(this.evento.getNombre()).append("\n");
        sb.append("Fecha:  ").append(this.evento.getFecha()).append("\n");
        sb.append("Hora:   ").append(this.evento.getHora()).append("\n");
        sb.append("Lugar:  ").append(nombreLugar).append("\n");
        sb.append("---------------------\n");
        sb.append("Localidad: ").append(this.localidad.getNombre()).append("\n");
        sb.append("Precio:    $").append(this.localidad.getPrecio()).append("\n");
        sb.append("ID Ticket: ").append(this.idTiquete).append("\n");
        sb.append("Estado:    ").append(this.impreso ? "Impreso" : "Digital");

        return sb.toString();
    }
}