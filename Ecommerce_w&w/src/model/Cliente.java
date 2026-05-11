package model;

import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario {
    private static final long serialVersionUID = 1L;
    
    private Carrito carrito;
    private List<Pedido> historialPedidos;

    public Cliente(String id, String nombre, String email, String password) {
        super(id, nombre, email, password);
        this.carrito = new Carrito();
        this.historialPedidos = new ArrayList<>();
    }

    @Override
    public String getRol() {
        return "CLIENTE";
    }
    
    public Carrito getCarrito() { return carrito; }
    public void setCarrito(Carrito carrito) { this.carrito = carrito; }
    
    public List<Pedido> getHistorialPedidos() { return historialPedidos; }
    public void setHistorialPedidos(List<Pedido> historialPedidos) { this.historialPedidos = historialPedidos; }
    
    public void agregarPedido(Pedido pedido) {
        this.historialPedidos.add(pedido);
    }
}