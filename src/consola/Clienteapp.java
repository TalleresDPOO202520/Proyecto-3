package consola;

import java.util.Scanner;

import marketplace.Marketplace;
import persistencia.Persistencia;
import usuarios.Administrador;
import usuarios.Cliente;

public class Clienteapp {

    private Marketplace mp;
    private String rolActual;
    private boolean ejecutando;
    private Scanner scanner;
    private Cliente clienteActivo;
    private Administrador adminActivo;

    public Clienteapp(Marketplace mp) {
        this.mp = mp;
        this.rolActual = null;
        this.ejecutando = true;
        this.scanner = new Scanner(System.in);
    }


    public void setCliente(Cliente c) {
        this.clienteActivo = c;
        this.rolActual = "cliente";
    }
    public void setAdmin(Administrador a) {
        this.adminActivo = a;
        this.rolActual = "admin";
    }

    public void inicio() {
        System.out.println("=== Bienvenido al Marketplace de Boletamaster ===");
        cargarDatos();

        if (rolActual == null) { 
            autenticarUsuario();
        }

        while (ejecutando) {
            mostrarMenu();
            System.out.print("Seleccione una opción: ");
            int op = leerEntero();
            manejarEntrada(op);
        }

        guardarDatos();
        System.out.println(" Sesión finalizada. ¡Gracias por usar Boletamaster!");
    }

    private void autenticarUsuario() {
        System.out.println("\nIngrese su rol (cliente/admin): ");
        String rol = scanner.nextLine().trim().toLowerCase();

        if (rol.equals("cliente")) {
            System.out.print("Login del cliente: ");
            String login = scanner.nextLine();
            System.out.print("Password: ");
            String pass = scanner.nextLine();
            this.clienteActivo = new Cliente(login, pass);
            this.rolActual = "cliente";
            System.out.println("Cliente autenticado: " + login);
        } else if (rol.equals("admin")) {
            System.out.print("Login del admin: ");
            String login = scanner.nextLine();
            System.out.print("Password: ");
            String pass = scanner.nextLine();
            this.adminActivo = new Administrador(login, pass, "ADM001", "Admin Principal");
            this.rolActual = "admin";
            System.out.println("Administrador autenticado: " + login);
        } else {
            System.out.println("Rol no reconocido. Iniciando como cliente por defecto.");
            this.clienteActivo = new Cliente("cliente_demo", "1234");
            this.rolActual = "cliente";
        }
    }

    public void mostrarMenu() {
        System.out.println("\n--- MENÚ PRINCIPAL (" + rolActual.toUpperCase() + ") ---");
        switch (rolActual) {
            case "cliente" -> {
                System.out.println("1. Listar ofertas activas");
                System.out.println("2. Contraofertar una oferta");
                System.out.println("3. Comprar al precio fijo");
                System.out.println("4. Cambiar a rol administrador");
                System.out.println("0. Salir");
            }
            case "admin" -> {
                System.out.println("1. Listar ofertas activas");
                System.out.println("2. Eliminar una oferta");
                System.out.println("3. Consultar finanzas");
                System.out.println("4. Cambiar a rol cliente");
                System.out.println("0. Salir");
            }
        }
    }

    public void manejarEntrada(int op) {
        switch (rolActual) {
            case "cliente" -> manejarCliente(op);
            case "admin" -> manejarAdmin(op);
        }
    }

    private void manejarCliente(int op) {
        switch (op) {
            case 1 -> listarOfertas();
            case 2 -> {
                System.out.print("ID de la oferta: ");
                String idOferta = scanner.nextLine();
                System.out.print("ID de la contraoferta: ");
                String idContra = scanner.nextLine();
                System.out.print("Precio propuesto: ");
                double precio = leerDouble();
                contraofertar(idOferta, idContra, precio);
            }
            case 3 -> {
                System.out.print("ID de la oferta a comprar: ");
                String idOferta = scanner.nextLine();
                comprar(idOferta);
            }
            case 4 -> cambiarRol("admin");
            case 0 -> ejecutando = false;
            default -> System.out.println("⚠️ Opción inválida.");
        }
    }

    private void manejarAdmin(int op) {
        switch (op) {
            case 1 -> listarOfertas();
            case 2 -> {
                System.out.print("ID de la oferta a eliminar: ");
                String idOferta = scanner.nextLine();
                mp.eliminarPorAdmin(idOferta, adminActivo);
            }
            case 3 -> {
                System.out.print("Filtro de finanzas: ");
                String filtro = scanner.nextLine();
                adminActivo.consultarFinanzas(filtro);
            }
            case 4 -> cambiarRol("cliente");
            case 0 -> ejecutando = false;
            default -> System.out.println("Opción inválida.");
        }
    }

    public void cargarDatos() {
        System.out.println("Cargando datos del marketplace...");
        var ofertasRaw = Persistencia.cargarOfertasCrudo();
        if (ofertasRaw.isEmpty()) {
            System.out.println("No se encontraron ofertas previas.");
        } else {
            System.out.println("Se cargaron " + ofertasRaw.size() + " ofertas desde el archivo.");
        }
    }

    public void guardarDatos() {
        System.out.println("Guardando datos...");
        Persistencia.guardarOfertas(mp.getOfertasActivas().values());
        System.out.println("Datos guardados correctamente");
    }

    public void listarOfertas() {
        if (mp.getOfertasActivas().isEmpty()) {
            System.out.println("No hay ofertas activas actualmente.");
        } else {
            System.out.println("\n=== Ofertas Activas ===");
            mp.getOfertasActivas().values().forEach(System.out::println);
        }
    }

    public void contraofertar(String idOferta, String idContra, double precio) {
        try {
            mp.contraOfertar(idOferta, idContra, clienteActivo, precio);
            System.out.println("Contraoferta enviada exitosamente.");
        } catch (Exception e) {
            System.err.println(" Error al realizar la contraoferta: " + e.getMessage());
        }
    }

    public void comprar(String idOferta) {
        try {
            mp.comprarPrecioFijo(idOferta, clienteActivo);
            System.out.println("Compra realizada exitosamente.");
        } catch (Exception e) {
            System.err.println("Error al realizar la compra: " + e.getMessage());
        }
    }

    public void cambiarRol(String nuevoRol) {
        if (nuevoRol.equalsIgnoreCase("cliente")) {
            this.rolActual = "cliente";
            System.out.println("Cambiado a modo CLIENTE.");
        } else if (nuevoRol.equalsIgnoreCase("admin")) {
            this.rolActual = "admin";
            System.out.println("Cambiado a modo ADMINISTRADOR.");
        } else {
            System.out.println("Rol no válido.");
        }
    }

    private int leerEntero() {
        while (!scanner.hasNextInt()) {
            System.out.print("Ingrese un número válido: ");
            scanner.next();
        }
        int num = scanner.nextInt();
        scanner.nextLine();
        return num;
    }

    private double leerDouble() {
        while (!scanner.hasNextDouble()) {
            System.out.print("Ingrese un valor numérico válido: ");
            scanner.next();
        }
        double num = scanner.nextDouble();
        scanner.nextLine();
        return num;
    }
}
