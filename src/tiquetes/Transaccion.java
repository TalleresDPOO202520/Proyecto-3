package tiquetes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Transaccion {
    private String idTransaccion;
    private String fechaHora;
    private double total;
    private String estado;      // "PENDIENTE"|"PAGADA"|"FALLIDA"|"REEMBOLSADA"
    private String metodoPago;  // "SALDO"|"PASARELADEPAGO"
    private List<Tiquete> tiquetes;

    public Transaccion(String idTransaccion, List<Tiquete> tiquetes, String metodoPago) {
        this.idTransaccion = idTransaccion;
        this.tiquetes = tiquetes;
        this.metodoPago = metodoPago;
        this.estado = "PENDIENTE";
        this.fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public double calcularTotal(PoliticaCargos cargos) {
        double suma = 0.0;
        for (Tiquete t : tiquetes) {
            suma += t.calcularPrecioTotal(cargos);
        }
        this.total = suma;
        return total;
    }

    public void marcarPagada() {
        this.estado = "PAGADA";
    }

    public void marcarFallida() {
        this.estado = "FALLIDA";
    }

    public void marcarReembolsada() {
        this.estado = "REEMBOLSADA";
    }

    public String getIdTransaccion() { return idTransaccion; }
    public String getFechaHora() { return fechaHora; }
    public double getTotal() { return total; }
    public String getEstado() { return estado; }
    public String getMetodoPago() { return metodoPago; }
    public List<Tiquete> getTiquetes() { return tiquetes; }

    @Override
    public String toString() {
        return "Transaccion{" +
                "id='" + idTransaccion + '\'' +
                ", fechaHora='" + fechaHora + '\'' +
                ", total=" + total +
                ", estado='" + estado + '\'' +
                ", metodoPago='" + metodoPago + '\'' +
                ", tiquetes=" + tiquetes.size() +
                '}';
    }
}

