package marketplace;

import java.time.LocalDateTime;
import java.util.*;

import usuarios.Cliente;
import tiquetes.Tiquete;


public class Oferta{
	private String idOferta;
	public Cliente vendedor;
	public ArrayList<Tiquete> tiquetes;
	public double precio;
	public int estado; //0: publicado, 1: vendido, 2: eliminado
	public HashMap<String, ContraOferta> contraOfertas;
	public LocalDateTime fechaCreacion;
	
	
    public Oferta(String idOferta, Cliente vendedor, ArrayList<Tiquete> tiquetes, double precio) {
        if (vendedor == null || tiquetes == null || tiquetes.isEmpty())
            throw new IllegalArgumentException("Datos inválidos para crear oferta.");

        for (Tiquete t	: tiquetes) {
            if (!t.isTransferible() || t.isTransferido()) {
                throw new IllegalArgumentException("No se puede incluir un tiquete no transferible o ya transferido.");
            }
        }

        this.idOferta = idOferta;
        this.vendedor = vendedor;
        this.tiquetes = new ArrayList<Tiquete>(tiquetes);
        this.precio = precio;
        this.estado = 0;
        this.fechaCreacion = LocalDateTime.now();
        this.contraOfertas = new HashMap<>();
    }
    
    public Cliente getVendedor() {
    	return vendedor;
    }
    
    public ArrayList<Tiquete> getTiquetes(){
    	return tiquetes;
    }
    
    public double getPrecio() {
    	return precio;
    }
    
    public HashMap<String, ContraOferta> getContraOfertas(){
    	return contraOfertas;
    }
    
    public void marcarVendida() {
    	estado = 1;
    }
    
    public void marcarEliminada() {
    	estado = 2;
    }
    
    public String getIdOferta() {
        return idOferta;
    }

    public int getEstado() {
        return estado;
    }


		
}