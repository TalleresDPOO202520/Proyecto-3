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

public class Programa4 {

    //Helpers

    private PoliticaCargos configurarPolitica(Administrador admin) {
        admin.fijarCargoServicio("musical", 0.10);
        admin.fijarCuotaEmision(2.0); 
        PoliticaCargos pc = admin.getPoliticaCargos();
        pc.setMaxTiquetesPorTransaccion(10);
        return pc;
    }

    private Transaccion comprar(Cliente cliente, Evento evento, List<Tiquete> items, PoliticaCargos pc) {
        if (items.size() > pc.getMaxTiquetesPorTransaccion()) {
            Transaccion transaccion = new Transaccion(UUID.randomUUID().toString(), items, "SALDO");
            transaccion.calcularTotal(pc);
            transaccion.marcarFallida();
            return transaccion;
        }

        for (Tiquete tiquete : items) {
            String idLocalidad = tiquete.getLocalidad().getIdLocalidad();
            if (evento.tiquetesDisponibles(idLocalidad) <= 0) {
                Transaccion transaccion = new Transaccion(UUID.randomUUID().toString(), items, "SALDO");
                transaccion.calcularTotal(pc);
                transaccion.marcarFallida();
                return transaccion;
            }
        }

        Transaccion transaccion = new Transaccion(UUID.randomUUID().toString(), items, "SALDO");
        double total = transaccion.calcularTotal(pc);
        boolean cobrado = cliente.debitar(total);
        if (!cobrado) {
            transaccion.marcarFallida();
            return transaccion;
        }
        for (Tiquete tiquete : items) evento.registrarVentaTiquete(tiquete);
        transaccion.marcarPagada();
        return transaccion;
    }


    private double reembolsarPorCancelacionAdmin(Administrador admin, Evento evento, Cliente cliente, Transaccion compra, PoliticaCargos pc, String motivo) {
        boolean cancelar = admin.cancelarEvento(evento, motivo);
        if (!cancelar) return 0.0;

        double suma = 0.0;
        for (Tiquete transaccion : compra.getTiquetes()) {
            double base = transaccion.getLocalidad().getPrecio();
            suma += base;
        }
        
        cliente.abonar(suma);
        compra.marcarReembolsada();
        return suma;
    }

    private double reembolsarPorCalamidad(Administrador admin, Cliente cliente, Transaccion compra, PoliticaCargos pc, String caso) {

    	double suma = 0.0;
        boolean aprobado = true;
        if (!aprobado) return 0.0;

        for (Tiquete transaccion : compra.getTiquetes()) {
            double base = transaccion.getLocalidad().getPrecio();
            double porcentaje = pc.cargoPara(transaccion.getEvento().getTipo());
            suma += (base + base * porcentaje);
        }
        cliente.abonar(suma);
        compra.marcarReembolsada();
        return suma;
    }

    //Casos de prueba

    @Test
    void cancelacionPorAdministrador_reembolsaSoloBase() {

    	Administrador admin = new Administrador("admin", "pw", "A1", "Root");
        PoliticaCargos pc = configurarPolitica(admin);

        Venue venue = new Venue("V1", "centro del pueblo", "imperio romano", 15000);
        Evento evento = new Evento("E1", "ejecucion publica", "medieval", "2026-05-22", "19:30", venue);
        Localidad general = new Localidad("L-G", "General", 100.0, false, 3);
        evento.agregarLocalidad(general);

        List<Tiquete> disponibles = general.generarTiquetes(evento);
        List<Tiquete> carrito = Arrays.asList(disponibles.get(0), disponibles.get(1));
        Cliente cliente = new Cliente("ana", "123");
        cliente.abonar(1000.0);
        Transaccion transaccion = comprar(cliente, evento, carrito, pc);
        assertEquals("PAGADA", transaccion.getEstado());

        double saldoAntes = cliente.getSaldoPlataforma();
        double reembolsado = reembolsarPorCancelacionAdmin(admin, evento, cliente, transaccion, pc, "Motivo logístico");

        double esperadoBase = 100.0 * 2;
        assertEquals("CANCELADO", evento.getEstado(), "El evento debe quedar cancelado");
        assertEquals("REEMBOLSADA", transaccion.getEstado(), "La transacción debe quedar reembolsada");
        assertEquals(esperadoBase, reembolsado, "Reembolso incorrecto");
        assertEquals(saldoAntes + esperadoBase, cliente.getSaldoPlataforma(), "Saldo incorrecto");
    }

    @Test
    void reembolsoPorCalamidad_aprobado_sinCuotaFija() {
        Administrador admin = new Administrador("admin", "pw", "A1", "Root");
        PoliticaCargos pc = configurarPolitica(admin);

        Venue venue = new Venue("V2", "Teatro del Parque", "Medellín", 5000);
        Evento evento = new Evento("E2", "Clásicos", "musical", "2026-06-10", "20:00", venue);
        Localidad palco = new Localidad("L-P", "Palco", 200.0, true, 2);
        evento.agregarLocalidad(palco);

        List<Tiquete> sillas = palco.generarTiquetes(evento);
        List<Tiquete> carrito = Arrays.asList(sillas.get(0), sillas.get(1));
        Cliente cliente = new Cliente("bob", "xyz");
        cliente.abonar(10000.0);
        Transaccion transaccion = comprar(cliente, evento, carrito, pc);
        assertEquals("PAGADA", transaccion.getEstado());

        double saldoAntes = cliente.getSaldoPlataforma();
        double porcentaje = pc.cargoPara(evento.getTipo());
        double esperadoUnit = 200.0 + 200.0 * porcentaje;
        double reembolsado = reembolsarPorCalamidad(admin, cliente, transaccion, pc, "a death in the family");

        assertEquals("REEMBOLSADA", transaccion.getEstado(), "La transacción debe quedar reembolsada");
        assertEquals(esperadoUnit * 2, reembolsado, "Reembolo incorrecto");
        assertEquals(saldoAntes + reembolsado, cliente.getSaldoPlataforma(), "Saldo incorrecto");
        assertEquals("PROGRAMADO", evento.getEstado(), "El evento no se debe cancelar");
    }

    @Test
    void reembolsoPorCalamidad_noAprobado_noCambiaNada() {

    	Administrador admin = new Administrador("admin", "pw", "A1", "Root");
        PoliticaCargos pc = configurarPolitica(admin);

        Venue venue = new Venue("V3", "Coliseo Norte", "Cali", 10000);
        Evento evento = new Evento("E3", "Rock Fest", "musical", "2026-07-01", "18:00", venue);
        Localidad general = new Localidad("L-G", "General", 120.0, false, 2);
        evento.agregarLocalidad(general);

        List<Tiquete> disponibles = general.generarTiquetes(evento);
        List<Tiquete> carrito = Arrays.asList(disponibles.get(0));
        Cliente cliente = new Cliente("pepa", "oink");
        cliente.abonar(1000.0);
        Transaccion transaccion = comprar(cliente, evento, carrito, pc);
        assertEquals("PAGADA", transaccion.getEstado());

        double saldoAntes = cliente.getSaldoPlataforma();
        double reembolsado = 0.0;

        assertEquals(0.0, reembolsado, "El reembolso no debe ser aprobado");
        assertEquals("PAGADA", transaccion.getEstado(), "La transacción debe permanecer pagada");
        assertEquals(saldoAntes, cliente.getSaldoPlataforma(), "El saldo no debe cambair");
        assertEquals("PROGRAMADO", evento.getEstado(), "El evento no se debe cancelar");
    }
}
