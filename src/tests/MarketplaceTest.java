package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import marketplace.Marketplace;
import marketplace.Oferta;
import tiquetes.Tiquete;
import usuarios.Cliente;
import usuarios.Administrador;


public class MarketplaceTest {

    private Marketplace mp;
    private Cliente vendedor;
    private Cliente comprador;
    private Administrador admin;

    static class StubTiquete extends Tiquete {
        public StubTiquete(String id) {
            super(null, null, id);
        }
        @Override public double calcularPrecioTotal(tiquetes.PoliticaCargos cargos) { return 0.0; }
    }

    private ArrayList<Tiquete> tiquetesTransferibles(int n) {
        ArrayList<Tiquete> lista = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            lista.add(new StubTiquete("T" + i));
        }
        return lista;
    }

    @BeforeEach
    void setUp() {
        mp = new Marketplace();
        vendedor = new Cliente("vend1", "pwd");
        comprador = new Cliente("comp1", "pwd");
        admin = new Administrador("admin", "pwd", "A1", "Root");
    }

    @Test
    void testPublicarOferta() {
        ArrayList<Tiquete> tks = tiquetesTransferibles(2);
        mp.publicarOferta("OF1", vendedor, tks, 100.0);

        HashMap<String, Oferta> activas = mp.getOfertasActivas();
        assertTrue(activas.containsKey("OF1"));
        Oferta o = activas.get("OF1");
        assertNotNull(o);
        assertEquals(vendedor, o.getVendedor());
        assertEquals(100.0, o.getPrecio(), 0.0001);
        assertEquals(2, o.getTiquetes().size());
        assertEquals(0, o.estado);
        assertNotNull(o.fechaCreacion);
    }

    @Test
    void testEliminarPorVendedor() {
        ArrayList<Tiquete> tks = tiquetesTransferibles(1);
        mp.publicarOferta("OF2", vendedor, tks, 50.0);
        Oferta ref = mp.getOferta("OF2");
        assertNotNull(ref);

        mp.eliminarPorVendedor("OF2", vendedor);

        assertEquals(2, ref.estado);
        assertFalse(mp.getOfertasActivas().containsKey("OF2"));
    }

    @Test

    void testEliminarPorAdmin() {
        ArrayList<Tiquete> tks = tiquetesTransferibles(1);
        mp.publicarOferta("OF3", vendedor, tks, 75.0);
        Oferta ref = mp.getOferta("OF3");
        assertNotNull(ref);

        mp.eliminarPorAdmin("OF3", admin);

        assertEquals(2, ref.estado);
        assertFalse(mp.getOfertasActivas().containsKey("OF3"));
    }

    @Test

    void testContraOfertar() {
        ArrayList<Tiquete> tks = tiquetesTransferibles(1);
        mp.publicarOferta("OF4", vendedor, tks, 120.0);

        mp.contraOfertar("OF4", "CO1", comprador, 110.0);
        Oferta o = mp.getOferta("OF4");

        assertNotNull(o.getContraOfertas().get("CO1"));
    }

    @Test
    void testAceptarContraOferta() {
        ArrayList<Tiquete> tks = tiquetesTransferibles(1);
        mp.publicarOferta("OF5", vendedor, tks, 200.0);
        mp.contraOfertar("OF5", "CO2", comprador, 180.0);

        mp.aceptarContraOferta("OF5", "CO2", vendedor);
        Oferta o = mp.getOferta("OF5");

        assertEquals(1, o.getContraOfertas().get("CO2").estado);
    }

    @Test
    void testComprarPrecioFijo() {
        ArrayList<Tiquete> tks = tiquetesTransferibles(2);
        mp.publicarOferta("OF6", vendedor, tks, 300.0);

        Oferta ref = mp.getOferta("OF6");
        mp.comprarPrecioFijo("OF6", comprador);

        assertEquals(1, ref.estado);
        assertFalse(mp.getOfertasActivas().containsKey("OF6"));
    }
}
