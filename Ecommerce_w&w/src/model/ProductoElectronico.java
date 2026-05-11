/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class ProductoElectronico extends Producto {
    private static final long serialVersionUID = 1L;
    
    private int garantiaMeses;
    private String marca;

    public ProductoElectronico(String id, String nombre, double precio, int stock, int garantiaMeses, String marca) {
        super(id, nombre, precio, stock);
        this.garantiaMeses = garantiaMeses;
        this.marca = marca;
    }

    @Override
    public String getTipo() {
        return "ELECTRONICO";
    }
    
    public int getGarantiaMeses() { return garantiaMeses; }
    public void setGarantiaMeses(int garantiaMeses) { this.garantiaMeses = garantiaMeses; }
    
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    
    @Override
    public String toString() {
        return super.toString() + " | Electrónico | Marca: " + marca + " | Garantía: " + garantiaMeses + " meses";
    }
}