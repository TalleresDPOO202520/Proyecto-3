package eventos;

import java.util.ArrayList;
import java.util.List;

import tiquetes.Tiquete;
import tiquetes.TiqueteNumerado;  
import tiquetes.TiqueteSimple; 

public class Localidad {
    private String idLocalidad;
    private String nombre;
    private double precio;
    private boolean numerada;
    private int capacidad;
    private List<Tiquete> tiquetes;

    public Localidad(String idLocalidad, String nombre, double precio, boolean numerada, int capacidad) {
        this.idLocalidad = idLocalidad;
        this.nombre = nombre;
        this.precio = precio;
        this.numerada = numerada;
        this.capacidad = capacidad;
        this.tiquetes = new ArrayList<>();
    }

    public List<Tiquete> generarTiquetes(Evento evento) {
        tiquetes.clear();
        for (int i = 1; i <= capacidad; i++) {
            if (numerada) {
                tiquetes.add(new TiqueteNumerado(evento, this, String.valueOf(i)));
            } else {
                String idTiquete = evento.getIdEvento() + "-" + idLocalidad + "-" + i;
                tiquetes.add(new TiqueteSimple(evento, this, idTiquete));
            }
        }
        return tiquetes;
    }


    public String getIdLocalidad() { return idLocalidad; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public boolean isNumerada() { return numerada; }
    public int getCapacidad() { return capacidad; }
    public List<Tiquete> getTiquetes() { return tiquetes; }
}
