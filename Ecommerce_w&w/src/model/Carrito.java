/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Carrito implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<ItemCarrito> items;
    private double total;

    public Carrito() {
        this.items = new ArrayList<>();
        this.total = 0.0;
    }

    public List<ItemCarrito> getItems() { return items; }
    
    public double getTotal() { 
        calcularTotal();
        return total; 
    }
    
    public void agregarProducto(Producto producto, int cantidad) throws Exception {
        if (cantidad <= 0) {
            throw new Exception("La cantidad debe ser mayor a 0");
        }
        if (producto.getStock() < cantidad) {
            throw new Exception("Stock insuficiente. Disponible: " + producto.getStock());
        }
        
        // Buscar si el producto ya está en el carrito
        for (ItemCarrito item : items) {
            if (item.getProducto().getId().equals(producto.getId())) {
                item.setCantidad(item.getCantidad() + cantidad);
                calcularTotal();
                return;
            }
        }
        
        items.add(new ItemCarrito(producto, cantidad));
        calcularTotal();
    }
    
    public void eliminarProducto(String idProducto) {
        items.removeIf(item -> item.getProducto().getId().equals(idProducto));
        calcularTotal();
    }
    
    public void modificarCantidad(String idProducto, int nuevaCantidad) throws Exception {
        if (nuevaCantidad <= 0) {
            eliminarProducto(idProducto);
            return;
        }
        
        for (ItemCarrito item : items) {
            if (item.getProducto().getId().equals(idProducto)) {
                if (item.getProducto().getStock() < nuevaCantidad) {
                    throw new Exception("Stock insuficiente. Disponible: " + item.getProducto().getStock());
                }
                item.setCantidad(nuevaCantidad);
                break;
            }
        }
        calcularTotal();
    }
    
    private void calcularTotal() {
        total = 0.0;
        for (ItemCarrito item : items) {
            total += item.getSubtotal();
        }
    }
    
    public void vaciar() {
        items.clear();
        total = 0.0;
    }
    
    public boolean isEmpty() {
        return items.isEmpty();
    }
}