package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.Test;

import eventos.Evento;
import eventos.Localidad;
import eventos.Venue;
import tiquetes.PoliticaCargos;
import tiquetes.Tiquete;
import usuarios.Cliente;


public class Programa3 {

    //Helpers

    private boolean transferir(Cliente origen, String login, String password, Tiquete tiquete, Cliente destino) {

    	if (!origen.autenticar(login, password)) return false;

        if (!tiquete.isTransferible()) return false;
        if (tiquete.isTransferido()) return false;

        tiquete.marcarTransferido();
        return true;
    }
    
    
    static class TiqueteDeluxeNoTransferible extends Tiquete {
        public TiqueteDeluxeNoTransferible(Evento evento, Localidad localidad, String id) {
            super(evento, localidad, id);
            this.transferible = false;
        }
        @Override
        public double calcularPrecioTotal(PoliticaCargos cargos) {
            double base = localidad.getPrecio();
            double porcentaje = cargos.cargoPara(evento.getTipo());
            return base + (base * porcentaje) + cargos.getCuotaEmisionFija();
        }
    }

    
	//Casos de prueba

    @Test
    void transferenciaNormal_con_credencialesCorrectas() {
        Venue venue = new Venue("V1", "Movistar Arena", "Bogotá", 13000);
        Evento evento = new Evento("E-N1", "Concierto", "musical", "2026-03-10", "20:00", venue);
        Localidad general = new Localidad("LG", "General", 100.0, false, 3);
        evento.agregarLocalidad(general);

        List<Tiquete> gen = general.generarTiquetes(evento);
        Tiquete tiquete = gen.get(0);

        Cliente origen = new Cliente("maria", "123");
        Cliente destino = new Cliente("bob", "456");

        boolean transferencia = transferir(origen, "alice", "pass123", tiquete, destino);

        assertTrue(transferencia, "Debe permitir transferir con credenciales correctas");
        assertTrue(tiquete.isTransferido(), "El tiquete queda marcado como transferido");
    }

    @Test
    void transferenciaNormal_con_credencialesIncorrectas() {
        Venue venue = new Venue("V2", "Teatro", "Medellín", 5000);
        Evento evento = new Evento("E6", "Standup", "comedia", "2026-04-01", "16:00", venue);
        Localidad general = new Localidad("L-G2", "General", 80.0, false, 2);
        evento.agregarLocalidad(general);

        List<Tiquete> gen = general.generarTiquetes(evento);
        Tiquete tiquete = gen.get(0);

        Cliente origen = new Cliente("jphn", "pw");
        Cliente destino = new Cliente("cena", "pw2");

        boolean transferencia = transferir(origen, "jphn", "wrong", tiquete, destino);

        assertFalse(transferencia, "Debe rechazar pq las credenciales son falsas");
        assertFalse(tiquete.isTransferido(), "El tiquete no debe marcarse como transferido");
    }

    @Test
    void transferenciaMultipleIndividual_y_soloUnaVez_porTiquete() {

        Venue venue = new Venue("V3", "Coliseo", "Cali", 10000);
        Evento evento = new Evento("E-M1", "Orquesta", "musical", "2026-05-05", "20:00", venue);
        Localidad palco = new Localidad("PAL", "Palco", 200.0, true, 2);
        evento.agregarLocalidad(palco);

        List<Tiquete> sillas = palco.generarTiquetes(evento);
        Tiquete silla1 = sillas.get(0);
        Tiquete silla2 = sillas.get(1);

        Cliente origen = new Cliente("origen", "123");
        Cliente destino1 = new Cliente("destino1", "a");
        Cliente destino2 = new Cliente("destino2", "b");

        boolean transferencia1 = transferir(origen, "origen", "123", silla1, destino1);
        assertTrue(transferencia1, "Debe permitir transferir el asiento 1 (primera vez)");
        assertTrue(silla1.isTransferido());

        boolean transferencia1_2 = transferir(origen, "origen", "123", silla1, destino2);
        assertFalse(transferencia1_2, "No debe permitir transferir el mismo tiquete más de una vez");

        boolean transferencia2 = transferir(origen, "origen", "123", silla2, destino2);
        assertTrue(transferencia2, "Debe permitir transferir el asiento 2 de forma individual");
        assertTrue(silla2.isTransferido());
    }

    @Test
    void transferenciaDeluxe_prohibida() {
        Venue venue = new Venue("V4", "Arena del Río", "Barranquilla", 45000);
        Evento evento = new Evento("E-D1", "Gala Deluxe", "musical", "2026-06-20", "21:00", venue);
        Localidad vip = new Localidad("VIP", "VIP", 500.0, false, 1);
        evento.agregarLocalidad(vip);

        Tiquete deluxe = new TiqueteDeluxeNoTransferible(evento, vip, "VIP1000");

        Cliente origen = new Cliente("lux", "gold");
        Cliente destino = new Cliente("guest", "silver");

        boolean transferencia = transferir(origen, "lux", "gold", deluxe, destino);

        assertFalse(transferencia, "Los tiquetes deluxe no se pueden transferir");
        assertFalse(deluxe.isTransferido(), "Debe permanecer sin transferir");
    }
}
