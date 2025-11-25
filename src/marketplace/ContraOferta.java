package marketplace;

import java.time.LocalDateTime;
import usuarios.Cliente;

public class ContraOferta {

    private String idContra;
    public Cliente comprador;
    public Double precioPropuesto;
    public int estado;              // 0=pendiente, 1=aceptada, 2=rechazada
    public LocalDateTime fecha;


    public ContraOferta(String idContra, Cliente comprador, Double precioPropuesto) {
        if (idContra == null || idContra.isEmpty())
            throw new IllegalArgumentException("El ID de la contraoferta no puede ser nulo o vacío.");
        if (comprador == null)
            throw new IllegalArgumentException("El comprador no puede ser nulo.");
        if (precioPropuesto == null || precioPropuesto <= 0)
            throw new IllegalArgumentException("El precio propuesto debe ser mayor a 0.");

        this.idContra = idContra;
        this.comprador = comprador;
        this.precioPropuesto = precioPropuesto;
        this.estado = 0; // pendiente por defecto
        this.fecha = LocalDateTime.now();
    }

    public String getIdContra() {
        return idContra;
    }

    public Cliente getComprador() {
        return comprador;
    }

    public Double getPrecioPropuesto() {
        return precioPropuesto;
    }

    public int getEstado() {
        return estado;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void marcarAceptada() {
        this.estado = 1;
        System.out.println("Contraoferta " + idContra + " aceptada para comprador: " + comprador.getLogin());
    }

    public void marcarRechazada() {
        this.estado = 2;
        System.out.println("Contraoferta " + idContra + " rechazada para comprador: " + comprador.getLogin());      
    }
}




