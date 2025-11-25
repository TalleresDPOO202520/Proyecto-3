package marketplace;

import java.util.*;
import usuarios.Cliente;
import usuarios.Administrador;
import tiquetes.Tiquete;

public class Marketplace {

    public HashMap<String, Oferta> ofertasActivas;

    public Marketplace() {
        this.ofertasActivas = new HashMap<>();
    }

    public void publicarOferta(String idOferta, Cliente vendedor, ArrayList<Tiquete> tiquetes, double precio) {
    	Oferta o = new Oferta(idOferta, vendedor, tiquetes, precio);
    	ofertasActivas.put(idOferta, o);
        System.out.println("Ofertas activas:" + ofertasActivas);
    }

    public HashMap<String, Oferta> getOfertasActivas() {
        return ofertasActivas;
    }

    public void eliminarPorVendedor(String idOferta, Cliente vendedor) {
    	ofertasActivas.get(idOferta).marcarEliminada();
        ofertasActivas.remove(idOferta);
        System.out.println("El vendedor " + vendedor + " eliminó su oferta con ID: " + idOferta);
    }

    public void eliminarPorAdmin(String idOferta, Administrador admin) {
    	ofertasActivas.get(idOferta).marcarEliminada();
    	ofertasActivas.remove(idOferta);
        System.out.println("El admin " + admin + " eliminó la oferta con ID: " + idOferta);
    }

    public void contraOfertar(String idOferta, String idContra, Cliente comprador, double precio) {
        ContraOferta co = new ContraOferta(idContra, comprador, precio);
        Oferta o = ofertasActivas.get(idOferta);
        o.getContraOfertas().put(idContra, co);
        System.out.println("Contra oferta publicada exitosamente");
    }

    public void aceptarContraOferta(String idOferta, String idContra, Cliente vendedor) {
        Oferta o = ofertasActivas.get(idOferta);
        HashMap<String, ContraOferta> co = o.getContraOfertas();
        co.get(idContra).marcarAceptada();
        System.out.println("Contra oferta aceptada exitosamente");
        
    }

    public void comprarPrecioFijo(String idOferta, Cliente comprador) {
        ofertasActivas.get(idOferta).marcarVendida();
        ofertasActivas.remove(idOferta);
        
    }

    public Oferta getOferta(String idOferta) {
    	return ofertasActivas.get(idOferta);
    }
    
}

