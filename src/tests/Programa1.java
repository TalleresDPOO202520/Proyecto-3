package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import eventos.Evento;
import eventos.Localidad;
import eventos.Venue;
import tiquetes.PoliticaCargos;
import tiquetes.Tiquete;
import tiquetes.TiqueteNumerado;


public class Programa1 {

	//Helpers	

 private PoliticaCargos politicaBasica() {
     Map<String, Double> mapa = new HashMap<>();
     mapa.put("musical", 0.10);
     PoliticaCargos pc = new PoliticaCargos(mapa);
     pc.setCuotaEmisionFija(2.0);
     pc.setMaxTiquetesPorTransaccion(10);
     return pc;
 }
 
 
 	//Casos de prueba

 @Test
 void crearEvento() {

     Venue venue = new Venue("V1", "Movistar Arena", "Bogotá", 5000);
     String fecha = "2025-12-01";

     assertTrue(venue.validarDisponibilidad(fecha), "El venue debe estar libre ese día");

     Evento evento = new Evento("E1", "Festival de Rock", "musical", fecha, "20:00", venue);
     venue.reservarFecha(fecha);

     assertEquals("E1", evento.getIdEvento());
     assertEquals(venue, evento.getVenue());
     assertFalse(venue.validarDisponibilidad(fecha), "No debe permitir otro evento el mismo día");
 }

 @Test
 void localidadSimple() {
     PoliticaCargos pc = politicaBasica();

     Venue venue = new Venue("V1", "Movistar Arena", "Bogotá", 13000);
     Evento evento = new Evento("E2", "Coachella", "musical", "2026-05-22", "19:30", venue);

     Localidad platino = new Localidad("L1", "Platino", 500.0, false, 3);
     evento.agregarLocalidad(platino);

     List<Tiquete> tickets = platino.generarTiquetes(evento);
     assertEquals(3, tickets.size());

     Set<String> ids = new HashSet<>();
     tickets.forEach(t -> ids.add(t.getIdTiquete()));
     assertEquals(3, ids.size(), "Cada tiquete debe tener id único");

     double esperado = tickets.get(0).calcularPrecioTotal(pc);
     tickets.forEach(t -> assertEquals(esperado, t.calcularPrecioTotal(pc)));
 }

 @Test
 void localidadNumerada() {

     Venue venue = new Venue("V2", "Estadio Metropolitano", "Barranquilla", 50000);
     Evento evento = new Evento("E3", "junior tu papá", "Futból", "2026-02-10", "20:00", venue);

     Localidad palcoA = new Localidad("L-PA", "Palco A", 200.0, true, 2);
     Localidad palcoB = new Localidad("L-PB", "Palco B", 200.0, true, 2);

     evento.agregarLocalidad(palcoA);
     evento.agregarLocalidad(palcoB);

     List<Tiquete> a = palcoA.generarTiquetes(evento);
     List<Tiquete> b = palcoB.generarTiquetes(evento);

     Set<String> asientosA = new HashSet<>();
     a.forEach(t -> asientosA.add(((TiqueteNumerado) t).getNumAsiento()));
     assertEquals(2, asientosA.size());

     Set<String> asientosB = new HashSet<>();
     b.forEach(t -> asientosB.add(((TiqueteNumerado) t).getNumAsiento()));
     assertEquals(2, asientosB.size());

     assertTrue(asientosA.contains("1") && asientosB.contains("1"),
             "El número de asiento puede repetirse en localidades distintas");
 }

 @Test
 void eventoConUnaSolaLocalidad() {
     PoliticaCargos pc = politicaBasica();

     Venue venue = new Venue("V3", "Coliseo Norte", "Cali", 10000);
     Evento evento = new Evento("E4", "taylor swift", "musical", "2026-02-01", "18:00", venue);

     Localidad general = new Localidad("L-G", "General", 50.0, false, 4);
     evento.agregarLocalidad(general);

     List<Tiquete> tickets = general.generarTiquetes(evento);

     assertEquals(1, evento.getLocalidades().size());
     assertEquals(4, tickets.size());

     double esperado = tickets.get(0).calcularPrecioTotal(pc);
     tickets.forEach(t -> assertEquals(esperado, t.calcularPrecioTotal(pc)));
 }
}
