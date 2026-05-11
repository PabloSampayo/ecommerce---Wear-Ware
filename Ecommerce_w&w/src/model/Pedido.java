/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Pedido implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String clienteId;
    private List<ItemCarrito> items;
    private double total;
    private Date fecha;
    private String estado; // PENDIENTE, PAGADO, ENVIADO, ENTREGADO
    private String metodoPago;

    public Pedido(String id, String clienteId, List<ItemCarrito> items, String metodoPago) {
        this.id = id;
        this.clienteId = clienteId;
        this.items = new ArrayList<>(items);
        this.metodoPago = metodoPago;
        this.fecha = new Date();
        this.estado = "PENDIENTE";
        calcularTotal();
    }
    
    private void calcularTotal() {
        total = 0.0;
        for (ItemCarrito item : items) {
            total += item.getSubtotal();
        }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }
    
    public List<ItemCarrito> getItems() { return items; }
    
    public double getTotal() { return total; }
    
    public Date getFecha() { return fecha; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    
    @Override
    public String toString() {
        return "Pedido #" + id + " - " + fecha + " - $" + total + " - " + estado;
    }
}