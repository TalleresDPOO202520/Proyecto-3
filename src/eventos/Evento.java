package eventos;

import java.util.*;
import tiquetes.Tiquete;


public class Evento {
    private String idEvento;
    private String nombre;
    private String tipo;       
    private String fecha;      
    private String hora;       
    private String estado;     

    private Venue venue;
    private List<Localidad> localidades;
    private List<Tiquete> tiquetesVendidos;

    public Evento(String idEvento, String nombre, String tipo, String fecha, String hora, Venue venue) {
        this.idEvento = idEvento;
        this.nombre = nombre;
        this.tipo = tipo;
        this.fecha = fecha;
        this.hora  = hora;
        this.estado = "PROGRAMADO";
        this.venue = venue;
        this.localidades = new ArrayList<>();
        this.tiquetesVendidos = new ArrayList<>();
    }


    public void agregarLocalidad(Localidad loc) {
        if (loc != null) {
            localidades.add(loc);
        }
    }


    public int tiquetesDisponibles(String idLocalidad) {
        Localidad loc = buscarLocalidad(idLocalidad);
        if (loc == null) return 0;

        long vendidos = tiquetesVendidos.stream()
            .filter(t -> t.getLocalidad().getIdLocalidad().equals(idLocalidad)) 
            .count();

        return loc.getCapacidad() - (int) vendidos;
    }

    private Localidad buscarLocalidad(String idLocalidad) {
        for (Localidad loc : localidades) {
            if (loc.getIdLocalidad().equals(idLocalidad)) return loc;
        }
        return null;
    }

    public void registrarVentaTiquete(Tiquete t) {
        if (t != null) {
            tiquetesVendidos.add(t);
        }
    }

    public void cancelarEvento() {
        this.estado = "CANCELADO";
    }

    public String getIdEvento() { return idEvento; }
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public String getFecha() { return fecha; }
    public String getHora() { return hora; }
    public String getEstado() { return estado; }
    public Venue getVenue() { return venue; }
    public List<Localidad> getLocalidades() { return localidades; }
    public List<Tiquete> getTiquetesVendidos() { return tiquetesVendidos; }
}

   