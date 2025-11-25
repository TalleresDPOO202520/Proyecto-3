package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import usuarios.Cliente;
import tiquetes.Tiquete;
import marketplace.Oferta;
import marketplace.ContraOferta;

import java.util.ArrayList;
import java.util.HashMap;
import java.time.LocalDateTime;

public class OfertaTest {

    private Cliente vendedor;

    static class StubTiquete extends Tiquete {
        public StubTiquete(String id) { super(null, null, id); }
        @Override public double calcularPrecioTotal(tiquetes.PoliticaCargos c) { return 0.0; }
    }

    @BeforeEach
    void setUp() {
        vendedor = new Cliente("vend1", "pwd");
    }

    private ArrayList<Tiquete> tiquetesValidos(int n) {
        ArrayList<Tiquete> list = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            list.add(new StubTiquete("T" + i));
        }
        return list;
    }

    @Test
    void testConstructorValido() {
        ArrayList<Tiquete> tks = tiquetesValidos(2);
        Oferta o = new Oferta("O1", vendedor, tks, 150.0);

        assertEquals(vendedor, o.getVendedor());
        assertEquals(150.0, o.getPrecio());
        assertEquals(2, o.getTiquetes().size());
        assertEquals(0, o.estado);
        assertNotNull(o.fechaCreacion);
        assertTrue(o.fechaCreacion.isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(o.getContraOfertas().isEmpty());
    }

    @Test
    void testConstructorInvalido() {
        ArrayList<Tiquete> tks = tiquetesValidos(1);
        assertThrows(IllegalArgumentException.class, () -> new Oferta("O2", null, tks, 50.0));
        assertThrows(IllegalArgumentException.class, () -> new Oferta("O3", vendedor, null, 50.0));
        assertThrows(IllegalArgumentException.class, () -> new Oferta("O4", vendedor, new ArrayList<>(), 50.0));
    }

    @Test
    void testMarcarVendida() {
        Oferta o = new Oferta("O5", vendedor, tiquetesValidos(1), 100.0);
        o.marcarVendida();
        assertEquals(1, o.estado);
    }

    @Test
    void testMarcarEliminada() {
        Oferta o = new Oferta("O6", vendedor, tiquetesValidos(1), 100.0);
        o.marcarEliminada();
        assertEquals(2, o.estado);
    }

    @Test
    void testContraOfertasMap() {
        Oferta o = new Oferta("O7", vendedor, tiquetesValidos(1), 100.0);
        HashMap<String, ContraOferta> map = o.getContraOfertas();
        assertNotNull(map);
        map.put("C1", new ContraOferta("C1", new Cliente("comp1", "pwd"), 200.0));
        assertEquals(1, o.getContraOfertas().size());
    }
}
