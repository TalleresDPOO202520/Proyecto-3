package tiquetes;

import java.util.Map;


public class PoliticaCargos {
    private double cuotaEmisionFija;
    private int maxTiquetesPorTransaccion;
    private Map<String, Double> cargoServicioPorTipo; // Ej: "musical" -> 0.12

    public PoliticaCargos(Map<String, Double> cargoServicioPorTipo) {
        this.cargoServicioPorTipo = cargoServicioPorTipo;
    }


    public double cargoPara(String tipoEvento) {
        if (tipoEvento == null) return 0.0;
        return cargoServicioPorTipo.getOrDefault(tipoEvento.toLowerCase(), 0.0);
    }

    public double getCuotaEmisionFija() { return cuotaEmisionFija; }
    public void setCuotaEmisionFija(double cuota) { this.cuotaEmisionFija = cuota; }

    public int getMaxTiquetesPorTransaccion() { return maxTiquetesPorTransaccion; }
    public void setMaxTiquetesPorTransaccion(int maximo) { this.maxTiquetesPorTransaccion = maximo; }

    public Map<String, Double> getCargoServicioPorTipo() { return cargoServicioPorTipo; }
    public void setCargoServicioPorTipo(Map<String, Double> cargoServicioPorTipo) { 
        this.cargoServicioPorTipo = cargoServicioPorTipo; 
    }
}
