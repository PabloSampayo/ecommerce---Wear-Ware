/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class ProductoRopa extends Producto {
    private static final long serialVersionUID = 1L;
    
    private String talla;
    private String color;

    public ProductoRopa(String id, String nombre, double precio, int stock, String talla, String color) {
        super(id, nombre, precio, stock);
        this.talla = talla;
        this.color = color;
    }

    @Override
    public String getTipo() {
        return "ROPA";
    }
    
    public String getTalla() { return talla; }
    public void setTalla(String talla) { this.talla = talla; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    @Override
    public String toString() {
        return super.toString() + " | Ropa | Talla: " + talla + " | Color: " + color;
    }
}