package consola;

import marketplace.Marketplace;
import usuarios.Cliente;

public class ClienteMain {
    public static void main(String[] args) {
        Marketplace mp = new Marketplace();
        Clienteapp app = new Clienteapp(mp);
        app.setCliente(new Cliente("cliente_demo", "1234")); // se puedeee cmabiarr
        app.inicio();
    }
}
