package consola;

import usuarios.Organizador;

public class OrganizadorMain {
    public static void main(String[] args) {
        Organizador org = new Organizador("org_demo", "1234");
        new OrganizadorApp(org).inicio();
    }
}
