package consola;

import marketplace.Marketplace;
import usuarios.Administrador;

public class AdminMain {
    public static void main(String[] args) {
        Marketplace mp = new Marketplace();
        Clienteapp app = new Clienteapp(mp);
        app.setAdmin(new Administrador("admin", "admin", "ADM001", "Admin Principal"));
        app.inicio();
    }
}
