package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.Test;

import eventos.Evento;
import eventos.Localidad;
import eventos.Venue;
import tiquetes.PoliticaCargos;
import tiquetes.Tiquete;
import tiquetes.Transaccion;
import usuarios.Administrador;
import usuarios.Cliente;

public class Programa2 {

    //Helpers
	
    private PoliticaCargos configurarPolitica(Administrador admin) {
        admin.fijarCargoServicio("musical", 0.10); 
        admin.fijarCuotaEmision(2.0);              
        PoliticaCargos pc = admin.getPoliticaCargos();
        pc.setMaxTiquetesPorTransaccion(3);        
        return pc;
    }

    
    private Transaccion comprar(Cliente cliente, Evento evento, List<Tiquete> items, PoliticaCargos pc) {
        if (items.size() > pc.getMaxTiquetesPorTransaccion()) {
            Transaccion transaccion = new Transaccion(UUID.randomUUID().toString(), items, "SALDO");
            transaccion.calcularTotal(pc);
            transaccion.marcarFallida();
            return transaccion;
        }

        for (Tiquete tiq : items) {
            String idLocalidad = tiq.getLocalidad().getIdLocalidad();
            if (evento.tiquetesDisponibles(idLocalidad) <= 0) {
                Transaccion transaccion = new Transaccion(UUID.randomUUID().toString(), items, "SALDO");
                transaccion.calcularTotal(pc);
                transaccion.marcarFallida();
                return transaccion;
            }
        }

        Transaccion transaccionTotal = new Transaccion(UUID.randomUUID().toString(), items, "SALDO");
        double total = transaccionTotal.calcularTotal(pc);
        boolean cobrado = cliente.debitar(total);
        if (!cobrado) {
            transaccionTotal.marcarFallida();
            return transaccionTotal;
        }

        for (Tiquete tiq : items) {
            evento.registrarVentaTiquete(tiq);
        }
        transaccionTotal.marcarPagada();
        return transaccionTotal;
    }

    
    //Casos de prueba

    @Test
    void compraValida_con_localidadSimple() {
        Administrador admin = new Administrador("pedro_admin", "123", "A1", "Pedro");
        PoliticaCargos pc = configurarPolitica(admin);

        Venue venue = new Venue("V1", "Movistar Arena", "Bogotá", 13000);
        Evento evento = new Evento("E2", "Coachella", "musical", "2026-05-22", "19:30", venue);

        Localidad general = new Localidad("L-G", "General", 100.0, false, 5);
        evento.agregarLocalidad(general);

        List<Tiquete> disponibles = general.generarTiquetes(evento);
        List<Tiquete> carrito = Arrays.asList(disponibles.get(0), disponibles.get(1));

        Cliente cliente = new Cliente("ana2000", "456");
        cliente.abonar(1000.0);

        double base = general.getPrecio();
        double unit = base + (base * pc.cargoPara(evento.getTipo())) + pc.getCuotaEmisionFija();

        Transaccion transaccion = comprar(cliente, evento, carrito, pc);

        assertEquals("PAGADA", transaccion.getEstado(), "La transacción debe quedar pagada");
        assertEquals(2, evento.getTiquetesVendidos().size(), "Se deben registrar 2 ventas");
        assertEquals(unit * 2, transaccion.getTotal(), "Total incorrecto");
    }

    @Test
    void compraPaquete_con_localidadNumerada() {
        Administrador admin = new Administrador("juli_admin", "789", "A2", "Juliana");
        PoliticaCargos pc = configurarPolitica(admin);

        Venue venue = new Venue("V2", "Teatro del Parque", "Medellín", 5000);
        Evento evento = new Evento("E3", "Clásicos", "musical", "2026-06-10", "20:00", venue);

        Localidad palco = new Localidad("L-P", "Palco", 200.0, true, 3);
        evento.agregarLocalidad(palco);

        List<Tiquete> asientos = palco.generarTiquetes(evento);
        List<Tiquete> paquete = Arrays.asList(asientos.get(0), asientos.get(1), asientos.get(2));

        Cliente cliente = new Cliente("mario", "012");
        cliente.abonar(10000.0);

        double base = palco.getPrecio();
        double unit = base + (base * pc.cargoPara(evento.getTipo())) + pc.getCuotaEmisionFija();

        Transaccion transaccion = comprar(cliente, evento, paquete, pc);

        assertEquals("PAGADA", transaccion.getEstado(), "La transacción debe quedar pagada");
        assertEquals(unit * 3, transaccion.getTotal(), "Total incorrecto");
        assertEquals(0, evento.tiquetesDisponibles("L-P"), "No debe haber tiquetes disponibles");
    }

    @Test
    void compraRechazada_porSuperarMaximo() {
        Administrador admin = new Administrador("admin", "contraseña", "A1", "admin");
        PoliticaCargos pc = configurarPolitica(admin);

        Venue venue = new Venue("V3", "Coliseo Norte", "Cali", 10000);
        Evento evento = new Evento("E4", "Rock Fest", "musical", "2026-07-01", "18:00", venue);

        Localidad general = new Localidad("L-G", "General", 120.0, false, 6);
        evento.agregarLocalidad(general);

        List<Tiquete> disponibles = general.generarTiquetes(evento);
        List<Tiquete> carrito = Arrays.asList(disponibles.get(0), disponibles.get(1), disponibles.get(2), disponibles.get(3));

        Cliente cliente = new Cliente("cata", "pass");
        cliente.abonar(10000.0);
        double saldoAntes = cliente.getSaldoPlataforma();

        Transaccion transaccion = comprar(cliente, evento, carrito, pc);

        assertEquals("FALLIDA", transaccion.getEstado(), "La transacción debe fallar");
        assertEquals(0, evento.getTiquetesVendidos().size(), "No debe haber ventas");
        assertEquals(saldoAntes, cliente.getSaldoPlataforma(), "El saldo del cliente no debe cambiar");
    }

    @Test
    void compraRechazada_porSaldoInsuficiente() {
        Administrador admin = new Administrador("admin_dos", "dob", "A2", "administrador segundo");
        PoliticaCargos pc = configurarPolitica(admin);

        Venue venue = new Venue("V4", "Arena del Río", "Barranquilla", 45000);
        Evento evento = new Evento("E5", "Sinfonía del Caribe", "musical", "2026-08-15", "20:00", venue);

        Localidad vip = new Localidad("L-VIP", "VIP", 300.0, false, 2);
        evento.agregarLocalidad(vip);

        List<Tiquete> disponibles = vip.generarTiquetes(evento);
        List<Tiquete> carrito = Arrays.asList(disponibles.get(0), disponibles.get(1));

        Cliente cliente = new Cliente("dani", "W7^dY0Ql86!RrtD0Xxgc");
        cliente.abonar(50.0);

        Transaccion transaccion = comprar(cliente, evento, carrito, pc);

        assertEquals("FALLIDA", transaccion.getEstado(), "Transaccion debe fallar por saldo insuficiente");
        assertEquals(0, evento.getTiquetesVendidos().size(), "No deba haber ventas");
    }
}

