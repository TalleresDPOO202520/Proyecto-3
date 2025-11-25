package eventos;

import java.util.*;


public class Venue {
    private String idVenue;
    private String nombre;
    private String ubicacion;
    private int capacidadMaxima;
    private Set<String> fechasOcupadas; 

    public Venue(String idVenue, String nombre, String ubicacion, int capacidadMaxima) {
        this.idVenue = idVenue;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.capacidadMaxima = capacidadMaxima;
        this.fechasOcupadas = new HashSet<>();
    }

    public boolean validarDisponibilidad(String fecha) {
        return !fechasOcupadas.contains(fecha);
    }

    public void reservarFecha(String fecha) {
        fechasOcupadas.add(fecha);
    }

    public void liberarFecha(String fecha) {
        fechasOcupadas.remove(fecha);
    }

    public String getIdVenue() { return idVenue; }
    public String getNombre() { return nombre; }
    public String getUbicacion() { return ubicacion; }
    public int getCapacidadMaxima() { return capacidadMaxima; }
}

