package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import usuarios.Cliente;
import marketplace.ContraOferta;

import java.time.LocalDateTime;

public class ContraOfertaTest {

    private Cliente comprador;

    @BeforeEach
    void setUp() {
        comprador = new Cliente("user1", "pass123");
    }

    @Test
    void testConstructorValido() {
        ContraOferta co = new ContraOferta("C1", comprador, 200.0);

        assertEquals("C1", co.getIdContra());
        assertEquals(comprador, co.getComprador());
        assertEquals(200.0, co.getPrecioPropuesto());
        assertEquals(0, co.getEstado());
        assertNotNull(co.getFecha());
        assertTrue(co.getFecha().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void testIdInvalido() {
        assertThrows(IllegalArgumentException.class, () -> new ContraOferta(null, comprador, 100.0));
        assertThrows(IllegalArgumentException.class, () -> new ContraOferta("", comprador, 100.0));
    }

    @Test
    void testCompradorNulo() {
        assertThrows(IllegalArgumentException.class, () -> new ContraOferta("C2", null, 100.0));
    }

    @Test
    void testPrecioInvalido() {
        assertThrows(IllegalArgumentException.class, () -> new ContraOferta("C3", comprador, 0.0));
        assertThrows(IllegalArgumentException.class, () -> new ContraOferta("C4", comprador, -10.0));
    }

    @Test
    void testMarcarAceptada() {
        ContraOferta co = new ContraOferta("C5", comprador, 250.0);
        co.marcarAceptada();
        assertEquals(1, co.getEstado(), "La contraoferta debe quedar en estado aceptada (1)");
    }

    @Test
    void testMarcarRechazada() {
        ContraOferta co = new ContraOferta("C6", comprador, 300.0);
        co.marcarRechazada();
        assertEquals(2, co.getEstado());
    }
}
