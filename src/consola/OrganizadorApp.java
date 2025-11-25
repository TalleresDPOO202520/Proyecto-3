package consola;

import java.util.*;
import eventos.Evento;
import eventos.Localidad;
import eventos.Venue;
import usuarios.Organizador;

public class OrganizadorApp {

    private final Scanner sc = new Scanner(System.in);
    private final Map<String, Venue> venues = new HashMap<>();
    private final Map<String, Evento> eventos = new HashMap<>();
    private final Organizador org;

    public OrganizadorApp(Organizador org) {
        this.org = org;
    }

    public void inicio() {
        System.out.println("=== Consola Organizador (" + org.getLogin() + ") ===");
        boolean run = true;
        while (run) {
            System.out.println("\n1. Crear venue");
            System.out.println("2. Crear evento");
            System.out.println("3. Agregar localidad a evento");
            System.out.println("4. Generar tiquetes de una localidad");
            System.out.println("5. Listar eventos y localidades");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            int op = leerEntero();
            switch (op) {
                case 1 -> crearVenue();
                case 2 -> crearEvento();
                case 3 -> agregarLocalidad();
                case 4 -> generarTiquetes();
                case 5 -> listar();
                case 0 -> run = false;
                default -> System.out.println("Opción inválida");
            }
        }
        System.out.println("Fin sesión Organizador.");
    }

    private void crearVenue() {
        System.out.print("ID venue: "); String id = sc.nextLine().trim();
        System.out.print("Nombre: "); String nombre = sc.nextLine().trim();
        System.out.print("Ubicación: "); String ubi = sc.nextLine().trim();
        System.out.print("Capacidad máx: "); int cap = leerEntero();
        venues.put(id, new Venue(id, nombre, ubi, cap));
        System.out.println("Venue creado: " + nombre);
    }

    private void crearEvento() {
        System.out.print("ID evento: "); String id = sc.nextLine().trim();
        System.out.print("Nombre: "); String nombre = sc.nextLine().trim();
        System.out.print("Tipo: "); String tipo = sc.nextLine().trim();
        System.out.print("Fecha (YYYY-MM-DD): "); String fecha = sc.nextLine().trim();
        System.out.print("Hora (HH:MM): "); String hora = sc.nextLine().trim();
        System.out.print("ID venue existente: "); String idV = sc.nextLine().trim();

        Venue v = venues.get(idV);
        if (v == null) { System.out.println("El venue no existe."); return; }

        eventos.put(id, new Evento(id, nombre, tipo, fecha, hora, v));
        System.out.println("Evento creado: " + nombre);
    }

    private void agregarLocalidad() {
        System.out.print("ID evento: "); String idE = sc.nextLine().trim();
        Evento e = eventos.get(idE);
        if (e == null) { System.out.println("El evento no existe."); return; }

        System.out.print("ID localidad: "); String idL = sc.nextLine().trim();
        System.out.print("Nombre: "); String nom = sc.nextLine().trim();
        System.out.print("Precio base: "); double precio = leerDouble();
        System.out.print("¿Numerada? (s/n): "); boolean num = sc.nextLine().trim().equalsIgnoreCase("s");
        System.out.print("Capacidad: "); int cap = leerEntero();

        e.agregarLocalidad(new Localidad(idL, nom, precio, num, cap));
        System.out.println("Localidad agregada.");
    }

    private void generarTiquetes() {
        System.out.print("ID evento: "); String idE = sc.nextLine().trim();
        Evento e = eventos.get(idE);
        if (e == null) { System.out.println("El evento no existe."); return; }

        System.out.print("ID localidad: "); String idL = sc.nextLine().trim();
        var loc = e.getLocalidades().stream()
                .filter(x -> x.getIdLocalidad().equals(idL))
                .findFirst().orElse(null);
        if (loc == null) { System.out.println("La localidad no existe."); return; }

        int n = loc.generarTiquetes(e).size();
        System.out.println("Generados " + n + " tiquetes para " + loc.getNombre());
    }

    private void listar() {
        if (eventos.isEmpty()) { System.out.println("No hay eventos."); return; }
        for (Evento e : eventos.values()) {
            System.out.println("\n" + e.getIdEvento() + " - " + e.getNombre() + " (" + e.getTipo() + ")");
            e.getLocalidades().forEach(l ->
                System.out.println("  " + l.getIdLocalidad() + " | " + l.getNombre()
                    + " | $" + l.getPrecio() + " | cap=" + l.getCapacidad()
                    + " | numerada=" + l.isNumerada())
            );
        }
    }

    private int leerEntero() {
        while (!sc.hasNextInt()) { System.out.print("Número válido: "); sc.next(); }
        int n = sc.nextInt(); sc.nextLine(); return n;
    }

    private double leerDouble() {
        while (!sc.hasNextDouble()) { System.out.print("Número válido: "); sc.next(); }
        double d = sc.nextDouble(); sc.nextLine(); return d;
    }
}
